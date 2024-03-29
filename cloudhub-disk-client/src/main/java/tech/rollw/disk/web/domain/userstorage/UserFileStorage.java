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

package tech.rollw.disk.web.domain.userstorage;

import tech.rollw.disk.web.database.DataItem;
import tech.rollw.disk.web.domain.user.LegalUserType;
import space.lingu.NonNull;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.LightConfiguration;
import space.lingu.light.PrimaryKey;

/**
 * 用户文件夹下的文件。
 *
 * @author RollW
 */
@DataTable(name = "user_file_storage", indices = {
        @Index(value = {"name", "directory_id"}, unique = true),
        @Index(value = {"owner", "owner_type"})
})
public class UserFileStorage implements AttributedStorage, DataItem {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "name")
    @LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "255")
    private final String name;

    @DataColumn(name = "owner")
    private final long owner;

    @DataColumn(name = "owner_type")
    private final LegalUserType ownerType;

    @DataColumn(name = "file_id")
    private final String fileId;

    @DataColumn(name = "directory_id")
    private final long folderId;

    @DataColumn(name = "mime_type")
    private final String mimeType;

    @DataColumn(name = "file_category")
    private final FileType fileCategory;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    @DataColumn(name = "deleted")
    private final boolean deleted;

    public UserFileStorage(Long id, String name,
                           long owner, LegalUserType ownerType,
                           String fileId,
                           long folderId,
                           String mimeType, FileType fileCategory,
                           long createTime,
                           long updateTime, boolean deleted) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.ownerType = ownerType;
        this.fileId = fileId;
        this.folderId = folderId;
        this.mimeType = mimeType;
        this.fileCategory = fileCategory;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getOwner() {
        return owner;
    }

    @Override
    public long getStorageId() {
        return getId();
    }

    @NonNull
    @Override
    public StorageType getStorageType() {
        return StorageType.FILE;
    }

    @Override
    public Long getParentId() {
        return getFolderId();
    }

    @Override
    public long getOwnerId() {
        return getOwner();
    }

    @NonNull
    @Override
    public LegalUserType getOwnerType() {
        return ownerType;
    }

    public String getFileId() {
        return fileId;
    }

    public long getFolderId() {
        return folderId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public FileType getFileCategory() {
        return fileCategory;
    }

    @Override
    public FileType getFileType() {
        return getFileCategory();
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String name;
        private long owner;
        private LegalUserType ownerType;
        private String fileId;
        private long folderId;
        private String mimeType;
        private FileType fileCategory;
        private long createTime;
        private long updateTime;
        private boolean deleted;

        public Builder() {
        }

        public Builder(UserFileStorage userFileStorage) {
            this.id = userFileStorage.id;
            this.name = userFileStorage.name;
            this.owner = userFileStorage.owner;
            this.ownerType = userFileStorage.ownerType;
            this.fileId = userFileStorage.fileId;
            this.folderId = userFileStorage.folderId;
            this.mimeType = userFileStorage.mimeType;
            this.fileCategory = userFileStorage.fileCategory;
            this.createTime = userFileStorage.createTime;
            this.updateTime = userFileStorage.updateTime;
            this.deleted = userFileStorage.deleted;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setOwner(long owner) {
            this.owner = owner;
            return this;
        }

        public Builder setOwnerType(LegalUserType ownerType) {
            this.ownerType = ownerType;
            return this;
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setFolderId(long folderId) {
            this.folderId = folderId;
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder setFileCategory(FileType fileCategory) {
            this.fileCategory = fileCategory;
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

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public UserFileStorage build() {
            return new UserFileStorage(id, name, owner, ownerType, fileId,
                    folderId, mimeType, fileCategory, createTime, updateTime, deleted);
        }
    }
}
