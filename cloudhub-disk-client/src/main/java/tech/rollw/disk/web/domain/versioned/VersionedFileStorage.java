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

package tech.rollw.disk.web.domain.versioned;

import tech.rollw.disk.web.database.DataItem;
import tech.rollw.disk.web.domain.systembased.SystemResource;
import tech.rollw.disk.web.domain.systembased.SystemResourceKind;
import tech.rollw.disk.web.domain.userstorage.StorageType;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(name = "versioned_file_storage", indices = {
        @Index(value = {"storage_id", "version"}, unique = true)
})
public class VersionedFileStorage implements SystemResource, DataItem {
    private static final long INVALID_VERSION = 0;

    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "storage_id")
    private final long storageId;

    @DataColumn(name = "storage_type")
    private final StorageType storageType;

    @DataColumn(name = "version")
    private final long version;

    @DataColumn(name = "file_id")
    private final String fileId;

    @DataColumn(name = "operator")
    private final long operator;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "deleted")
    private final boolean deleted;

    public VersionedFileStorage(Long id, long storageId,
                                StorageType storageType,
                                long version,
                                String fileId,
                                long operator,
                                long createTime,
                                boolean deleted) {
        this.id = id;
        this.storageId = storageId;
        this.storageType = storageType;
        this.version = version;
        this.fileId = fileId;
        this.operator = operator;
        this.createTime = createTime;
        this.deleted = deleted;
    }

    @Override
    public Long getId() {
        return id;
    }

    public long getStorageId() {
        return storageId;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public long getVersion() {
        return version;
    }

    public String getFileId() {
        return fileId;
    }

    public long getOperator() {
        return operator;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isDeleted() {
        return deleted;
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
        return SystemResourceKind.VERSIONED_FILE;
    }

    public static final class Builder {
        private Long id;
        private long storageId;
        private StorageType storageType;
        private long version;
        private String fileId;
        private long operator;
        private long createTime;
        private boolean deleted;

        public Builder() {
        }

        public Builder(VersionedFileStorage versionedFileStorage) {
            this.id = versionedFileStorage.id;
            this.storageId = versionedFileStorage.storageId;
            this.storageType = versionedFileStorage.storageType;
            this.version = versionedFileStorage.version;
            this.fileId = versionedFileStorage.fileId;
            this.operator = versionedFileStorage.operator;
            this.createTime = versionedFileStorage.createTime;
            this.deleted = versionedFileStorage.deleted;
        }

        public Builder setId(Long id) {
            this.id = id;
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

        public Builder setVersion(long version) {
            this.version = version;
            return this;
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setOperator(long operator) {
            this.operator = operator;
            return this;
        }

        public Builder setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public VersionedFileStorage build() {
            return new VersionedFileStorage(id, storageId, storageType,
                    version, fileId, operator, createTime, deleted);
        }
    }
}
