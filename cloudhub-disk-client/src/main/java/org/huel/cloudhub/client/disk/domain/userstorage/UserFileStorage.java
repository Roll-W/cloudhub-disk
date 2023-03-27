package org.huel.cloudhub.client.disk.domain.userstorage;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;

/**
 * 用户文件夹下的文件。
 *
 * @author RollW
 */
@DataTable(name = "user_file_storage", indices = {
        @Index(value = {"name", "directory_id"}, unique = true)
})
public class UserFileStorage {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "name")
    private final String name;

    @DataColumn(name = "owner")
    private final long owner;

    @DataColumn(name = "owner_type")
    private final OwnerType ownerType;

    @DataColumn(name = "file_id")
    private final String fileId;

    @DataColumn(name = "directory_id")
    private final long directoryId;

    @DataColumn(name = "mime_type")
    private final String mimeType;

    @DataColumn(name = "file_category")
    private final FileType fileCategory;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    public UserFileStorage(Long id, String name,
                           long owner, OwnerType ownerType,
                           String fileId,
                           long directoryId,
                           String mimeType, FileType fileCategory,
                           long createTime,
                           long updateTime) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.ownerType = ownerType;
        this.fileId = fileId;
        this.directoryId = directoryId;
        this.mimeType = mimeType;
        this.fileCategory = fileCategory;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getOwner() {
        return owner;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public String getFileId() {
        return fileId;
    }

    public long getDirectoryId() {
        return directoryId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public FileType getFileCategory() {
        return fileCategory;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private long owner;
        private OwnerType ownerType;
        private String fileId;
        private long directoryId;
        private String mimeType;
        private FileType fileCategory;
        private long createTime;
        private long updateTime;

        public Builder() {
        }

        public Builder(UserFileStorage userFileStorage) {
            this.id = userFileStorage.id;
            this.name = userFileStorage.name;
            this.owner = userFileStorage.owner;
            this.ownerType = userFileStorage.ownerType;
            this.fileId = userFileStorage.fileId;
            this.directoryId = userFileStorage.directoryId;
            this.mimeType = userFileStorage.mimeType;
            this.fileCategory = userFileStorage.fileCategory;
            this.createTime = userFileStorage.createTime;
            this.updateTime = userFileStorage.updateTime;
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

        public Builder setOwnerType(OwnerType ownerType) {
            this.ownerType = ownerType;
            return this;
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setDirectoryId(long directoryId) {
            this.directoryId = directoryId;
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

        public UserFileStorage build() {
            return new UserFileStorage(id, name, owner, ownerType, fileId,
                    directoryId, mimeType, fileCategory, createTime, updateTime);
        }
    }
}
