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

package tech.rollw.disk.web.domain.storage;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(name = "disk_file_storage")
public class DiskFileStorage {
    @DataColumn(name = "file_id")
    @PrimaryKey
    private final String fileId;

    @DataColumn(name = "size")
    private final long fileSize;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "last_access_time")
    private final long lastAccessTime;

    public DiskFileStorage(String fileId,
                           long fileSize,
                           long createTime,
                           long lastAccessTime) {
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.createTime = createTime;
        this.lastAccessTime = lastAccessTime;
    }

    public String getFileId() {
        return fileId;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiskFileStorage that)) return false;
        return fileSize == that.fileSize && createTime == that.createTime && lastAccessTime == that.lastAccessTime && Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileSize, createTime, lastAccessTime);
    }

    @Override
    public String toString() {
        return "DiskFileStorage{" +
                "fileId='" + fileId + '\'' +
                ", fileSize=" + fileSize +
                ", createTime=" + createTime +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String fileId;
        private long fileSize;
        private long createTime;
        private long lastAccessTime;

        public Builder() {
        }

        public Builder(DiskFileStorage diskFileStorage) {
            this.fileId = diskFileStorage.fileId;
            this.fileSize = diskFileStorage.fileSize;
            this.createTime = diskFileStorage.createTime;
            this.lastAccessTime = diskFileStorage.lastAccessTime;
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
            return this;
        }

        public DiskFileStorage build() {
            return new DiskFileStorage(fileId, fileSize, createTime, lastAccessTime);
        }
    }
}
