package org.huel.cloudhub.client.disk.database.dao;

import org.huel.cloudhub.client.disk.domain.user.LegalUserType;
import org.huel.cloudhub.client.disk.domain.userstorage.FileType;
import org.huel.cloudhub.client.disk.domain.userstorage.StorageOwner;
import org.huel.cloudhub.client.disk.domain.userstorage.UserFileStorage;
import org.huel.cloudhub.web.data.page.Offset;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Query;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface UserFileStorageDao extends AutoPrimaryBaseDao<UserFileStorage> {
    @Delete("DELETE FROM user_file_storage")
    void clearTable();

    @Query("SELECT * FROM user_file_storage")
    List<UserFileStorage> get();

    @Query("SELECT * FROM user_file_storage LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<UserFileStorage> get(Offset offset);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType}")
    List<UserFileStorage> get(long owner, LegalUserType ownerType);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND directory_id = {directoryId}")
    List<UserFileStorage> getByDirectoryId(long directoryId, long owner, LegalUserType ownerType);

    @Query("SELECT * FROM user_file_storage WHERE directory_id = {directoryId}")
    List<UserFileStorage> getByDirectoryId(long directoryId);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND file_type = {fileType}")
    List<UserFileStorage> getByType(long owner, LegalUserType ownerType, FileType fileType);

    @Query("SELECT * FROM user_file_storage WHERE id = {id}")
    UserFileStorage getById(long id);

    @Query("SELECT * FROM user_file_storage WHERE owner = {owner} AND owner_type = {ownerType} AND directory_id = {directoryId} AND name = {name}")
    UserFileStorage getById(long owner, LegalUserType ownerType, long directoryId, String name);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds}")
    List<UserFileStorage> getByIds(List<Long> storageIds);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds} AND owner = {storageOwner.getOwnerId()} AND owner_type = {storageOwner.getOwnerType}")
    List<UserFileStorage> getByIds(List<Long> storageIds, StorageOwner storageOwner);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds} AND file_type = {fileType}")
    List<UserFileStorage> getByIdsAndType(List<Long> storageIds, FileType fileType);

    @Query("SELECT * FROM user_file_storage WHERE id IN {storageIds} AND file_type = {fileType} AND owner = {storageOwner.getOwnerId()} AND owner_type = {storageOwner.getOwnerType}")
    List<UserFileStorage> getByIdsAndType(List<Long> storageIds, FileType fileType, StorageOwner storageOwner);
}
