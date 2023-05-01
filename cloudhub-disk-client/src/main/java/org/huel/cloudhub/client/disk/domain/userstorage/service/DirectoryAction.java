package org.huel.cloudhub.client.disk.domain.userstorage.service;

import org.huel.cloudhub.client.disk.domain.operatelog.context.OperationContextHolder;
import org.huel.cloudhub.client.disk.domain.user.LegalUserType;
import org.huel.cloudhub.client.disk.domain.userstorage.StorageAction;
import org.huel.cloudhub.client.disk.domain.userstorage.StorageType;
import org.huel.cloudhub.client.disk.domain.userstorage.UserDirectory;
import org.huel.cloudhub.client.disk.domain.userstorage.common.StorageErrorCode;
import org.huel.cloudhub.client.disk.domain.userstorage.common.StorageException;
import space.lingu.NonNull;

/**
 * @author RollW
 */
public class DirectoryAction implements StorageAction {
    private final UserDirectory.Builder directoryBuilder;
    private final DirectoryActionDelegate directoryActionDelegate;

    private UserDirectory directory;

    public DirectoryAction(UserDirectory directory,
                           DirectoryActionDelegate directoryActionDelegate) {
        this.directoryBuilder = directory.toBuilder();
        this.directory = directory;
        this.directoryActionDelegate = directoryActionDelegate;
    }

    @Override
    public long getStorageId() {
        return directory.getStorageId();
    }

    @Override
    @NonNull
    public StorageType getStorageType() {
        return directory.getStorageType();
    }

    @Override
    public Long getParentId() {
        return directory.getParentId();
    }

    @Override
    public long getOwnerId() {
        return directory.getOwnerId();
    }

    @Override
    @NonNull
    public LegalUserType getOwnerType() {
        return directory.getOwnerType();
    }

    @Override
    public String getName() {
        return directory.getName();
    }

    @Override
    public long getCreateTime() {
        return directory.getCreateTime();
    }

    @Override
    public long getUpdateTime() {
        return directory.getUpdateTime();
    }

    @Override
    public void delete() throws StorageException {
        directoryBuilder.setDeleted(true);
        update();
    }

    @Override
    public void create() throws StorageException {
        insert();
    }

    @Override
    public void rename(String newName) throws StorageException {
        directoryBuilder.setName(newName);
        update();
    }

    @Override
    public void move(long newParentId) throws StorageException {
        if (directory.getParentId() == newParentId) {
            throw new StorageException(StorageErrorCode.ERROR_SAME_DIRECTORY);
        }
        directoryBuilder.setParentId(newParentId);
        update();
    }

    @Override
    public StorageAction copy(long newParentId)
            throws StorageException {
        if (directory.getParentId() == newParentId) {
            throw new StorageException(StorageErrorCode.ERROR_SAME_DIRECTORY);
        }
        UserDirectory copy = directoryBuilder.build().toBuilder()
                .setId(null)
                .setParentId(newParentId)
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        Long id = directoryActionDelegate.createDirectory(copy);
        copy = copy.toBuilder()
                .setId(id)
                .build();
        OperationContextHolder.getContext()
                .addSystemResource(copy)
                .setChangedContent(copy.getName());
        return new DirectoryAction(copy, directoryActionDelegate);
    }

    private void insert() {
        UserDirectory insertedDirectory = directoryBuilder
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        if (insertedDirectory.getId() != null) {
            throw new StorageException(StorageErrorCode.ERROR_DIRECTORY_EXISTED);
        }

        Long id = directoryActionDelegate.createDirectory(insertedDirectory);
        directory = directoryBuilder
                .setId(id)
                .build();
        OperationContextHolder.getContext()
                .addSystemResource(directory)
                .setChangedContent(directory.getName());
    }

    private void update() {
        UserDirectory updatedDirectory = directoryBuilder
                .setUpdateTime(System.currentTimeMillis())
                .build();
        directoryActionDelegate.updateDirectory(updatedDirectory);
        directory = updatedDirectory;
        OperationContextHolder.getContext()
                .addSystemResource(directory)
                .setChangedContent(directory.getName());
    }
}
