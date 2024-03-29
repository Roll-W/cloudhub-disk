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

package tech.rollw.disk.web.database.dao;

import tech.rollw.disk.web.domain.user.User;
import tech.rollw.disk.web.domain.user.dto.UserInfo;
import tech.rollw.disk.common.data.page.Offset;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Query;
import space.lingu.light.Transaction;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface UserDao extends AutoPrimaryBaseDao<User> {
    @Transaction
    @Delete("UPDATE user SET enabled = {enabled} WHERE id = {userId}")
    void updateEnabledByUser(long userId, boolean enabled);

    @Transaction
    @Delete("UPDATE user SET enabled = 1 WHERE id = {user.getId()}")
    void updateEnabledByUser(User user);

    @Query("SELECT * FROM user WHERE id = {id}")
    User selectById(long id);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    List<User> get(int offset, int limit);

    @Query("SELECT * FROM user WHERE enabled = {enabled} ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    List<User> getWithEnableState(int offset, int limit, boolean enabled);

    @Query("SELECT * FROM user WHERE account_canceled = {canceled} ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    List<User> getWithCanceledState(int offset, int limit, boolean canceled);

    @Query("SELECT * FROM user WHERE enabled = {enabled} AND account_canceled = {canceled} ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    List<User> getWithEnableCanceledState(int offset, int limit, boolean enabled, boolean canceled);

    @Query("SELECT * FROM user WHERE id = {id}")
    User getUserById(long id);

    @Query("SELECT id, username, email, role FROM user WHERE id = {id}")
    UserInfo getUserInfoById(long id);

    @Query("SELECT * FROM user WHERE email = {email}")
    User getUserByEmail(String email);

    @Query("SELECT id FROM user WHERE email = {email}")
    Long getUserIdByEmail(String email);

    @Query("SELECT * FROM user WHERE username = {name}")
    User getUserByName(String name);

    @Query("SELECT * FROM user WHERE nickname = {nickname}")
    List<User> getUsersByNickname(String nickname);

    @Query("SELECT * FROM user WHERE nickname LIKE CONCAT('%', {nickname}, '%')")
    List<User> getUsersLikeNickname(String nickname);

    @Query("SELECT * FROM user WHERE username LIKE CONCAT('%', {username}, '%')")
    List<User> getUsersLikeUsername(String username);

    @Query("SELECT id FROM user WHERE username = {name}")
    Long getUserIdByName(String name);

    @Query("SELECT 1 FROM user")
    Integer hasUsers();

    @Query("SELECT * FROM user WHERE id IN ({ids})")
    List<User> getUsersByIds(List<Long> ids);

    @Query("SELECT COUNT(*) FROM user")
    long getCount();

    @Query("SELECT COUNT(*) FROM user WHERE enabled = {enabled}")
    long getCountWithEnableState(boolean enabled);

    @Query("SELECT COUNT(*) FROM user WHERE canceled = {canceled}")
    long getCountWithCanceledState(boolean canceled);

    @Override
    @Query("SELECT * FROM user WHERE deleted = 0")
    List<User> getActives();

    @Override
    @Query("SELECT * FROM user WHERE deleted = 1")
    List<User> getInactives();

    @Override
    @Query("SELECT * FROM user WHERE id = {id}")
    User getById(long id);

    @Override
    @Query("SELECT * FROM user WHERE id IN ({ids})")
    List<User> getByIds(List<Long> ids);

    @Override
    @Query("SELECT COUNT(*) FROM user WHERE deleted = 0")
    int countActive();

    @Override
    @Query("SELECT COUNT(*) FROM user WHERE deleted = 1")
    int countInactive();

    @Override
    @Query("SELECT * FROM user")
    List<User> get();

    @Override
    @Query("SELECT COUNT(*) FROM user")
    int count();

    @Override
    @Query("SELECT * FROM user LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<User> get(Offset offset);

    @Override
    default String getTableName() {
        return "user";
    }
}