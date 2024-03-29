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

package tech.rollw.disk.web.domain.share;

import tech.rollw.disk.web.database.DataItem;
import tech.rollw.disk.web.domain.systembased.SystemResource;
import tech.rollw.disk.web.domain.systembased.SystemResourceKind;
import tech.rollw.disk.web.domain.userstorage.StorageType;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(name = "user_share")
public class UserShare implements SystemResource, DataItem {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "share_id")
    private final String shareId;

    @DataColumn(name = "password")
    private final String password;

    @DataColumn(name = "storage_id")
    private final long storageId;

    @DataColumn(name = "storage_type")
    private final StorageType storageType;

    @DataColumn(name = "user_id")
    private final long userId;
    // who create this share

    @DataColumn(name = "expire_time")
    private final long expireTime;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    public UserShare(Long id, String shareId, String password,
                     long storageId,
                     StorageType storageType, long userId,
                     long expireTime, long createTime, long updateTime) {
        this.id = id;
        this.shareId = shareId;
        this.password = password;
        this.storageId = storageId;
        this.storageType = storageType;
        this.userId = userId;
        this.expireTime = expireTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getShareId() {
        return shareId;
    }

    public String getPassword() {
        return password;
    }

    public long getStorageId() {
        return storageId;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public long getUserId() {
        return userId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public long getResourceId() {
        return getId();
    }

    @Override
    public SystemResourceKind getSystemResourceKind() {
        return SystemResourceKind.STORAGE_SHARE;
    }

    public boolean isPublic() {
        return password == null || password.isEmpty();
    }

    public static final class Builder {
        private Long id;
        private String shareId;
        private String password;
        private long storageId;
        private StorageType storageType;
        private long userId;
        private long expireTime;
        private long createTime;
        private long updateTime;

        public Builder() {
        }

        public Builder(UserShare userShare) {
            this.id = userShare.id;
            this.shareId = userShare.shareId;
            this.password = userShare.password;
            this.storageId = userShare.storageId;
            this.storageType = userShare.storageType;
            this.userId = userShare.userId;
            this.expireTime = userShare.expireTime;
            this.createTime = userShare.createTime;
            this.updateTime = userShare.updateTime;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setShareId(String shareId) {
            this.shareId = shareId;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setStorageId(long storageId) {
            this.storageId = storageId;
            return this;
        }

        public Builder setStorageType(StorageType storageType) {
            this.storageType = storageType;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setExpireTime(long expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        public Builder setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public UserShare build() {
            return new UserShare(
                    id, shareId, password, storageId,
                    storageType, userId, expireTime, createTime, updateTime
            );
        }
    }
}
