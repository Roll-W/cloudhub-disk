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

package tech.rollw.disk.web.domain.operatelog;

import tech.rollw.disk.web.database.DataItem;
import tech.rollw.disk.web.domain.systembased.SystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

/**
 * 操作关联资源。
 * <p>
 * 对于部分操作，除直接操作的资源外，还会对其他资源产生影响。
 * 如删除文件夹时，会删除文件夹下的所有文件，这些文件也需要被记录在操作关联资源中。
 *
 * @author RollW
 */
@DataTable(name = "operation_log_association")
public class OperationLogAssociation implements DataItem {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    // operator id needs get from OperationLog

    @DataColumn(name = "operation_id")
    private final long operationId;

    @DataColumn(name = "resource_id")
    private final long resourceId;

    @DataColumn(name = "resource_kind")
    private final SystemResourceKind resourceKind;

    public OperationLogAssociation(Long id, long operationId,
                                   long resourceId,
                                   SystemResourceKind resourceKind) {
        this.id = id;
        this.operationId = operationId;
        this.resourceId = resourceId;
        this.resourceKind = resourceKind;
    }

    @Override
    public Long getId() {
        return id;
    }

    public long getOperationId() {
        return operationId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public SystemResourceKind getResourceKind() {
        return resourceKind;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private long operationId;
        private long resourceId;
        private SystemResourceKind resourceKind;

        public Builder() {
        }

        public Builder(OperationLogAssociation association) {
            this.id = association.id;
            this.operationId = association.operationId;
            this.resourceId = association.resourceId;
            this.resourceKind = association.resourceKind;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setOperationId(long operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder setResourceId(long resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder setResourceKind(SystemResourceKind resourceKind) {
            this.resourceKind = resourceKind;
            return this;
        }

        public OperationLogAssociation build() {
            return new OperationLogAssociation(id, operationId, resourceId, resourceKind);
        }
    }
}
