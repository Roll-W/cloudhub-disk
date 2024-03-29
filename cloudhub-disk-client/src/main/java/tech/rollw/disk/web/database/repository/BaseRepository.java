/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.rollw.disk.web.database.repository;

import tech.rollw.disk.web.database.DataItem;
import tech.rollw.disk.web.database.dao.AutoPrimaryBaseDao;
import tech.rollw.disk.web.domain.systembased.ContextThread;
import tech.rollw.disk.web.domain.systembased.ContextThreadAware;
import tech.rollw.disk.web.domain.systembased.paged.PageableContext;
import tech.rollw.disk.web.system.pages.CountableDao;
import tech.rollw.disk.common.data.page.Offset;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
public abstract class BaseRepository<T extends DataItem> implements CountableDao<T> {
    protected final AutoPrimaryBaseDao<T> primaryBaseDao;
    protected final ContextThreadAware<PageableContext> pageableContextThreadAware;
    protected final Cache cache;

    protected BaseRepository(AutoPrimaryBaseDao<T> primaryBaseDao,
                             ContextThreadAware<PageableContext> pageableContextThreadAware,
                             CacheManager cacheManager) {
        this.primaryBaseDao = primaryBaseDao;
        this.pageableContextThreadAware = pageableContextThreadAware;
        this.cache = cacheManager.getCache("TB-" + primaryBaseDao.getTableName());
    }

    protected BaseRepository(AutoPrimaryBaseDao<T> primaryBaseDao,
                             ContextThreadAware<PageableContext> pageableContextThreadAware) {
        this.primaryBaseDao = primaryBaseDao;
        this.pageableContextThreadAware = pageableContextThreadAware;
        this.cache = null;
    }

    public long insert(T t) {
        invalidateCache(t);
        return primaryBaseDao.insertReturns(t);
    }

    public long[] insert(List<T> ts) {
        for (T t : ts) {
            invalidateCache(t);
        }
        return primaryBaseDao.insertReturns(ts);
    }

    public void update(T t) {
        primaryBaseDao.update(t);
        cacheResult(t);
    }

    public void update(List<T> ts) {
        primaryBaseDao.update(ts);
        cacheResult(ts);
    }

    public List<T> getActives() {
        return cacheResult(primaryBaseDao.getActives());
    }

    public List<T> getInactives() {
        return cacheResult(primaryBaseDao.getInactives());
    }

    public T getById(long id) {
        T t = getFromCache(id);
        if (t != null) {
            return t;
        }
        T queried = primaryBaseDao.getById(id);
        cacheResult(queried);

        return queried;
    }

    public List<T> getByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        CacheResult<T> ts = searchFromCache(ids);
        if (ts.missedIds().isEmpty()) {
            return ts.ts();
        }
        List<T> missed = cacheResult(
                primaryBaseDao.getByIds(ts.missedIds())
        );

        List<T> result = new ArrayList<>(ts.ts());
        result.addAll(missed);

        return result;
    }

    public int countActive() {
        return primaryBaseDao.countActive();
    }

    public int countInactive() {
        return primaryBaseDao.countInactive();
    }

    public int count() {
        return primaryBaseDao.count();
    }

    public List<T> get() {
        ContextThread<PageableContext> contextThread =
                pageableContextThreadAware.getContextThread();
        if (!contextThread.hasContext()) {
            return cacheResult(primaryBaseDao.get());
        }
        PageableContext pageableContext = contextThread.getContext();
        return get(pageableContext.toOffset());
    }

    public List<T> get(Offset offset) {
        ContextThread<PageableContext> contextThread =
                pageableContextThreadAware.getContextThread();
        if (contextThread.hasContext()) {
            PageableContext pageableContext = contextThread.getContext();
            long count = getCount();
            pageableContext.setTotal(count);
        }

        return cacheResult(primaryBaseDao.get(offset));
    }

    private long[] calcIds(Offset offset) {
        long[] ids = new long[offset.limit()];
        for (int i = 0; i < offset.limit(); i++) {
            ids[i] = offset.offset() + (long) i;
        }
        return ids;
    }

    public void invalidateCache() {
        if (cache == null) {
            return;
        }
        cache.clear();
    }

    protected void invalidateCache(T t) {
        if (cache == null) {
            return;
        }
        if (t == null || t.getId() == null) {
            return;
        }
        cache.evict(t.getId());
    }

    protected T cacheResult(T t) {
        if (t == null) {
            return null;
        }
        if (cache == null) {
            return t;
        }
        cache.put(t.getId(), t);
        return t;
    }

    protected List<T> cacheResult(List<T> t) {
        if (t == null || t.isEmpty()) {
            return t;
        }

        if (cache == null) {
            return t;
        }
        for (T t1 : t) {
            cacheResult(t1);
        }
        return t;
    }

    protected T getFromCache(long id) {
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper wrapper = cache.get(id);
        if (wrapper == null) {
            return null;
        }
        return (T) wrapper.get();
    }

    protected CacheResult<T> searchFromCache(List<Long> ids) {
        if (cache == null) {
            return new CacheResult<>(List.of(), ids);
        }
        List<T> ts = new ArrayList<>();
        List<Long> missedIds = new ArrayList<>();
        for (Long id : ids) {
            T t = getFromCache(id);
            if (t != null) {
                ts.add(t);
            } else {
                missedIds.add(id);
            }
        }
        return new CacheResult<>(ts, missedIds);
    }

    protected record CacheResult<T>(
            List<T> ts,
            List<Long> missedIds
    ) {
    }

    @Override
    public long getCount() {
        return count();
    }

    @Override
    public long getActiveCount() {
        return countActive();
    }

    @Override
    public Class<T> getCountableType() {
        return getEntityClass();
    }

    protected abstract Class<T> getEntityClass();
}
