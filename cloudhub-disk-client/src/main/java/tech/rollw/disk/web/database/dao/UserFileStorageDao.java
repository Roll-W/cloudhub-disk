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

import tech.rollw.disk.web.domain.user.LegalUserType;
import tech.rollw.disk.web.domain.userstorage.FileType;
import tech.rollw.disk.web.domain.userstorage.StorageOwner;
import tech.rollw.disk.web.domain.userstorage.UserFileStorage;
import tech.rollw.disk.common.data.page.Offset;
import space.lingu.Nullable;
import space.lingu.light.Dao;
import space.lingu.light.Query;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface UserFileStorageDao extends AutoPrimaryBaseDao<UserFileStorage> {

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType}")
    List<UserFileStorage> get(long owner, LegalUserType ownerType);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND directory_id = {directoryId}")
    List<UserFileStorage> getByDirectoryId(long directoryId, long owner, LegalUserType ownerType);

    @Query("SELECT * FROM user_file_storage WHERE directory_id = {directoryId}")
    List<UserFileStorage> getByDirectoryId(long directoryId);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND file_category = {fileType} AND deleted = 0")
    List<UserFileStorage> getByType(long owner, LegalUserType ownerType, FileType fileType);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND directory_id = {directoryId} AND name = {name}")
    UserFileStorage getById(long owner, LegalUserType ownerType, long directoryId, String name);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds} AND owner = {storageOwner.getOwnerId()} AND owner_type = {storageOwner.getOwnerType()}")
    List<UserFileStorage> getByIds(List<Long> storageIds, StorageOwner storageOwner);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds} AND file_category = {fileType}")
    List<UserFileStorage> getByIdsAndType(List<Long> storageIds, FileType fileType);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds} AND file_category = {fileType} AND owner = {storageOwner.getOwnerId()} AND owner_type = {storageOwner.getOwnerType()}")
    List<UserFileStorage> getByIdsAndType(List<Long> storageIds, FileType fileType, StorageOwner storageOwner);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND deleted = 1")
    List<UserFileStorage> getDeletedByOwner(long owner, LegalUserType ownerType);

    @Override
    @Query("SELECT * FROM user_file_storage WHERE deleted = 0")
    List<UserFileStorage> getActives();

    @Override
    @Query("SELECT * FROM user_file_storage WHERE deleted = 1")
    List<UserFileStorage> getInactives();

    @Override
    @Query("SELECT * FROM user_file_storage WHERE id = {id}")
    UserFileStorage getById(long id);

    @Override
    @Query("SELECT * FROM user_file_storage WHERE id IN ({ids})")
    List<UserFileStorage> getByIds(List<Long> ids);

    @Override
    @Query("SELECT COUNT(*) FROM user_file_storage WHERE deleted = 0")
    int countActive();

    @Override
    @Query("SELECT COUNT(*) FROM user_file_storage WHERE deleted = 1")
    int countInactive();

    @Override
    @Query("SELECT * FROM user_file_storage")
    List<UserFileStorage> get();

    @Override
    @Query("SELECT COUNT(*) FROM user_file_storage")
    int count();

    @Override
    @Query("SELECT * FROM user_file_storage LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<UserFileStorage> get(Offset offset);

    @Override
    default String getTableName() {
        return "user_file_storage";
    }

    @Query("SELECT * FROM user_file_storage WHERE id = {fileId} AND owner = {ownerId} AND owner_type = {ownerType}")
    UserFileStorage getById(long fileId, long ownerId, LegalUserType ownerType);

    @Query("SELECT * FROM user_file_storage WHERE name = {name} AND directory_id = {parentId}")
    UserFileStorage getByName(String name, long parentId);

    @Query("SELECT * FROM user_file_storage WHERE name LIKE CONCAT('%', {name}, '%') AND owner = {owner} AND owner_type = {legalUserType} AND deleted = 0")
    List<UserFileStorage> findFilesLike(String name, long owner, LegalUserType legalUserType);

    @Query("SELECT * FROM user_file_storage WHERE name LIKE CONCAT('%', {name}, '%') AND owner = {storageOwner.getOwnerId()} AND owner_type = {storageOwner.getOwnerType()} AND deleted = 0")
    List<UserFileStorage> findFilesLike(StorageOwner storageOwner, String name, FileType fileType);

    @Query("SELECT * FROM user_file_storage WHERE name LIKE CONCAT('%', {name}, '%') AND owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND file_category = {fileType} " +
            "AND create_time <= {before} " +
            "AND create_time >= {after} " +
            "AND deleted = 0")
    List<UserFileStorage> findFilesLikeAndBetween(
            StorageOwner storageOwner,
            String name,
            FileType fileType,
            long before,
            long after);

    @Query("SELECT * FROM user_file_storage WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND file_category = {fileType} " +
            "AND create_time <= {before} " +
            "AND create_time >= {after} " +
            "AND deleted = 0")
    List<UserFileStorage> findFilesBetween(StorageOwner storageOwner, FileType fileType, long before, long after);

    @Query("SELECT * FROM user_file_storage WHERE name LIKE CONCAT('%', {name}, '%') " +
            "AND owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND create_time <= {before} " +
            "AND create_time >= {after} " +
            "AND deleted = 0")
    List<UserFileStorage> findFilesLikeAndBetween(
            StorageOwner storageOwner,
            String name,
            long before,
            long after);

    @Query("SELECT * FROM user_file_storage WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND create_time <= {before} " +
            "AND create_time >= {after} " +
            "AND deleted = 0")
    List<UserFileStorage> findFilesBetween(StorageOwner storageOwner, long before, long after);

    long MIN_TIMESTAMP = -1;
    long MAX_TIMESTAMP = Long.MAX_VALUE;

    default List<UserFileStorage> findFilesByConditions(
            StorageOwner storageOwner,
            @Nullable String name,
            @Nullable FileType fileType,
            @Nullable Long before,
            @Nullable Long after) {
        if (name == null && fileType == null && before == null && after == null) {
            return List.of();
        }

        if (name == null) {
            name = "";
        }
        if (before == null) {
            before = MAX_TIMESTAMP;
        }
        if (after == null) {
            after = MIN_TIMESTAMP;
        }

        return findFiles(storageOwner, name, fileType, before, after);
    }

    default List<UserFileStorage> findFiles(
            StorageOwner storageOwner,
            String name,
            FileType fileType,
            Long before,
            Long after) {
        if (name.isEmpty() && fileType == null) {
            return findFilesBetween(storageOwner, before, after);
        }
        if (name.isEmpty()) {
            return findFilesBetween(storageOwner, fileType, before, after);
        }
        if (fileType == null) {
            return findFilesLikeAndBetween(storageOwner, name, before, after);
        }
        return findFilesLikeAndBetween(storageOwner, name, fileType, before, after);
    }

    @Query("SELECT * FROM user_file_storage " +
            "WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND deleted = 0 " +
            "LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<UserFileStorage> getActiveByOwner(StorageOwner storageOwner, Offset offset);

    @Query("SELECT * FROM user_file_storage " +
            "WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND deleted = 0")
    List<UserFileStorage> getActiveByOwner(StorageOwner storageOwner);

    @Query("SELECT * FROM user_file_storage " +
            "WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<UserFileStorage> getByOwner(StorageOwner storageOwner, Offset offset);

    @Query("SELECT * FROM user_file_storage " +
            "WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} ")
    List<UserFileStorage> getByOwner(StorageOwner storageOwner);


    @Query("SELECT COUNT(*) FROM user_file_storage " +
            "WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} " +
            "AND deleted = 0")
    int countActiveByOwner(StorageOwner storageOwner);

    @Query("SELECT COUNT(*) FROM user_file_storage " +
            "WHERE owner = {storageOwner.getOwnerId()} " +
            "AND owner_type = {storageOwner.getOwnerType()} ")
    int countByOwner(StorageOwner storageOwner);
}
