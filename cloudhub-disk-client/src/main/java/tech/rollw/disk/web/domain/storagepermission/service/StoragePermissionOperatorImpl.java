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

package tech.rollw.disk.web.domain.storagepermission.service;

import tech.rollw.disk.web.domain.operatelog.Operator;
import tech.rollw.disk.web.domain.operatelog.context.OperationContextHolder;
import tech.rollw.disk.web.domain.storagepermission.*;
import tech.rollw.disk.web.domain.storagepermission.common.StoragePermissionErrorCode;
import tech.rollw.disk.web.domain.storagepermission.common.StoragePermissionException;
import tech.rollw.disk.web.domain.userstorage.AttributedStorage;
import tech.rollw.disk.web.domain.userstorage.common.StorageErrorCode;
import tech.rollw.disk.web.domain.userstorage.common.StorageException;
import tech.rollw.disk.common.BusinessRuntimeException;

import java.util.HashSet;
import java.util.List;

/**
 * @author RollW
 */
public class StoragePermissionOperatorImpl implements StoragePermissionAction {
    private final AttributedStorage storage;
    private StoragePermission.Builder storagePermissionBuilder;
    private final StoragePermissionActionDelegate delegate;
    private StoragePermission storagePermission;
    private boolean checkDeleted;

    public StoragePermissionOperatorImpl(StoragePermissionActionDelegate delegate,
                                         AttributedStorage storage,
                                         StoragePermission storagePermission,
                                         boolean checkDeleted) {
        this.storage = storage;
        this.delegate = delegate;
        this.storagePermission = storagePermission;
        this.storagePermissionBuilder = storagePermission == null
                ? null : storagePermission.toBuilder();
        this.checkDeleted = checkDeleted;
    }

    @Override
    public void setCheckDeleted(boolean checkDeleted) {
        this.checkDeleted = checkDeleted;
    }

    @Override
    public boolean isCheckDeleted() {
        return checkDeleted;
    }

    @Override
    public StoragePermissionAction update()
            throws BusinessRuntimeException {
        return this;
    }

    @Override
    public StoragePermissionAction delete()
            throws BusinessRuntimeException {
        checkDeleted();
        storagePermissionBuilder
                .setPermissionType(PublicPermissionType.PRIVATE);
        OperationContextHolder.getContext()
                .addSystemResource(storagePermission)
                .addSystemResource(storage)
                .setOriginContent(String.valueOf(storagePermission.getPermissionType()))
                .setChangedContent(String.valueOf(PublicPermissionType.PRIVATE));
        return internalUpdate();
    }

    @Override
    public StoragePermissionAction setUserPermission(Operator operator, List<PermissionType> permissionTypes) throws BusinessRuntimeException {
        checkDeleted();
        if (operator.getOperatorId() == storage.getOwnerId()) {
            throw new StoragePermissionException(StoragePermissionErrorCode.ERROR_PERMISSION_NOT_ALLOW_USER);
        }
        if (permissionTypes == null || permissionTypes.isEmpty()) {
            throw new StoragePermissionException(StoragePermissionErrorCode.ERROR_PERMISSION_TYPE_EMPTY);
        }
        StorageUserPermission existing =
                delegate.getUserStoragePermission(operator, storage);
        if (existing != null) {
            if (checkEqual(permissionTypes, existing)) {
                return this;
            }
            StorageUserPermission updated = existing.toBuilder()
                    .setUpdateTime(System.currentTimeMillis())
                    .setPermissionTypes(reducePermissionTypes(permissionTypes))
                    .build();
            delegate.updateUserStoragePermission(updated);
            OperationContextHolder.getContext()
                    .addSystemResource(existing)
                    .addSystemResource(storage);
            return this;
        }
        StorageUserPermission storageUserPermission = StorageUserPermission.builder()
                .setStorageId(storage.getStorageId())
                .setStorageType(storage.getStorageType())
                .setUserId(operator.getOperatorId())
                .setPermissionTypes(reducePermissionTypes(permissionTypes))
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        long id = delegate
                .createUserStoragePermission(storageUserPermission);
        StorageUserPermission created = storageUserPermission.toBuilder()
                .setId(id)
                .build();
        OperationContextHolder.getContext()
                .addSystemResource(created)
                .addSystemResource(storage);
        return this;
    }

    private boolean checkEqual(List<PermissionType> permissionTypes, StorageUserPermission existing) {
        if (existing.getPermissionTypes().size() != permissionTypes.size()) {
            return false;
        }
        return new HashSet<>(existing.getPermissionTypes()).containsAll(permissionTypes);
    }

    private List<PermissionType> reducePermissionTypes(List<PermissionType> permissionTypes) {
        if (permissionTypes.contains(PermissionType.DENIED)) {
            return List.of(PermissionType.DENIED);
        }
        return permissionTypes.stream().distinct().toList();
    }

    @Override
    public StoragePermissionAction removeUserPermission(
            Operator operator) throws BusinessRuntimeException {
        checkDeleted();
        StorageUserPermission existing =
                delegate.getUserStoragePermission(operator, storage);
        if (existing == null || existing.isDeleted()) {
            return this;
        }
        StorageUserPermission updated = existing.toBuilder()
                .setUpdateTime(System.currentTimeMillis())
                .setDeleted(true)
                .setPermissionTypes(List.of())
                .build();
        delegate.updateUserStoragePermission(updated);
        OperationContextHolder.getContext()
                .addSystemResource(existing)
                .addSystemResource(storage);
        return this;
    }

    @Override
    public StoragePermissionAction setPermission(
            PublicPermissionType publicPermissionType) throws BusinessRuntimeException {
        if (storagePermission == null) {
            createStoragePermission(publicPermissionType);
            return this;
        }

        checkDeleted();
        if (storagePermission.getPermissionType() == publicPermissionType) {
            return this;
        }
        storagePermissionBuilder
                .setPermissionType(publicPermissionType);

        OperationContextHolder.getContext()
                .addSystemResource(storagePermission)
                .addSystemResource(storage)
                .setOriginContent(String.valueOf(storagePermission.getPermissionType()))
                .setChangedContent(String.valueOf(publicPermissionType));
        return internalUpdate();
    }

    private void createStoragePermission(PublicPermissionType publicPermissionType) {
        long time = System.currentTimeMillis();
        storagePermissionBuilder = StoragePermission.builder()
                .setStorageId(storage.getStorageId())
                .setStorageType(storage.getStorageType())
                .setPermissionType(publicPermissionType)
                .setDeleted(false)
                .setCreateTime(time)
                .setUpdateTime(time);
        long id = delegate.createStoragePermission(storagePermissionBuilder.build());
        storagePermission = storagePermissionBuilder
                .setId(id).build();
        OperationContextHolder.getContext()
                .addSystemResource(storagePermission)
                .addSystemResource(storage)
                .setChangedContent(String.valueOf(publicPermissionType));

    }

    @Override
    public AttributedStorage getRelatedStorage() {
        return storage;
    }

    @Override
    public StoragePermissionAction getSystemResource() {
        return this;
    }

    @Override
    public StoragePermission getStoragePermission() {
        return storagePermission;
    }

    @Override
    public long getResourceId() {
        return storagePermission.getId();
    }

    private StoragePermissionAction internalUpdate() {
        StoragePermission updated = storagePermissionBuilder
                .setUpdateTime(System.currentTimeMillis())
                .build();
        storagePermission = updated;
        delegate.updateStoragePermission(updated);
        return this;
    }

    private void checkDeleted() {
        if (checkDeleted && storage.isDeleted()) {
            throw new StorageException(StorageErrorCode.ERROR_FILE_ALREADY_DELETED);
        }
    }
}
