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

package tech.rollw.disk.web.domain.operatelog.service;

import tech.rollw.disk.web.common.ApiContextHolder;
import tech.rollw.disk.web.domain.operatelog.Action;
import tech.rollw.disk.web.domain.operatelog.OperateLogger;
import tech.rollw.disk.web.domain.operatelog.OperateType;
import tech.rollw.disk.web.domain.operatelog.OperateTypeFinder;
import tech.rollw.disk.web.domain.operatelog.OperateTypeFinderFactory;
import tech.rollw.disk.web.domain.operatelog.OperationLog;
import tech.rollw.disk.web.domain.operatelog.OperationLogAssociation;
import tech.rollw.disk.web.domain.operatelog.OperationLogCountProvider;
import tech.rollw.disk.web.domain.operatelog.OperationService;
import tech.rollw.disk.web.domain.operatelog.Operator;
import tech.rollw.disk.web.domain.operatelog.dto.Operation;
import tech.rollw.disk.web.domain.operatelog.dto.OperationLogDto;
import tech.rollw.disk.web.domain.operatelog.repository.OperationLogAssociationRepository;
import tech.rollw.disk.web.domain.operatelog.repository.OperationLogRepository;
import tech.rollw.disk.web.domain.systembased.SystemResource;
import tech.rollw.disk.web.domain.systembased.SystemResourceKind;
import tech.rollw.disk.common.data.page.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author RollW
 */
@Service
public class OperateLogServiceImpl implements OperateLogger, OperationService,
        OperationLogCountProvider {
    private static final Logger logger = LoggerFactory.getLogger(OperateLogServiceImpl.class);

    private final OperationLogRepository operationLogRepository;
    private final OperationLogAssociationRepository operationLogAssociationRepository;

    private final OperateTypeFinder operateTypeFinder;

    public OperateLogServiceImpl(OperationLogRepository operationLogRepository,
                                 OperationLogAssociationRepository operationLogAssociationRepository,
                                 OperateTypeFinderFactory operateTypeFinderFactory) {
        this.operationLogRepository = operationLogRepository;
        this.operationLogAssociationRepository = operationLogAssociationRepository;
        this.operateTypeFinder = operateTypeFinderFactory.getOperateTypeFinder();
    }

    @Override
    public void recordOperate(Action action, SystemResource systemResource,
                              OperateType operateType,
                              String originContent, String changedContent,
                              List<SystemResource> associateResources) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        Operator operator = apiContext.userInfo();
        String address = apiContext.ip();

        Operation operation = new Operation(
                operator,
                systemResource,
                operateType,
                address,
                System.currentTimeMillis(),
                originContent,
                changedContent,
                associateResources
        );
        recordOperate(operation);
    }

    @Override
    public void recordOperate(Action action, SystemResource systemResource,
                              OperateType operateType,
                              String originContent, List<SystemResource> associateResources) {
        recordOperate(action, systemResource, operateType, originContent, null, associateResources);
    }

    @Override
    public void recordOperate(Action action, SystemResource systemResource,
                              OperateType operateType,
                              List<SystemResource> associateResources) {
        recordOperate(action, systemResource, operateType,
                null, associateResources);
    }

    @Override
    public void recordOperate(Operation operation) {
        recordOperation(operation);
    }

    @Override
    public void recordOperation(Operation operation) {
        OperationLog operationLog = OperationLog.builder()
                .setOperator(operation.operator().getOperatorId())
                .setOperateTime(operation.timestamp())
                .setAction(operation.operateType().getAction())
                .setOperateType(operation.operateType().getTypeId())
                .setOperateResourceId(operation.systemResource().getResourceId())
                .setSystemResourceKind(operation.systemResource().getSystemResourceKind())
                .setOriginContent(operation.originContent())
                .setChangedContent(operation.changedContent())
                .setAddress(operation.address())
                .build();
        logger.debug("Log operation: {}", operationLog);
        long id = operationLogRepository.insert(operationLog);
        buildAssociation(operation, id);
    }

    @Override
    public List<OperationLogDto> getOperations(Pageable pageable) {
        List<OperationLog> operationLogs =
                operationLogRepository.get(pageable.toOffset());
        return getOperationLogDtosWithAssociates(operationLogs);
    }

    @Override
    public List<OperationLogDto> getOperationsByUserId(long userId) {
        List<OperationLog> operationLogs =
                operationLogRepository.getByOperator(userId);
        return operationLogs.stream().map(operationLog -> {
            OperateType operateType = operateTypeFinder
                    .getOperateType(operationLog.getOperateType());
            return OperationLogDto.from(operationLog, operateType);
        }).toList();
    }

    @Override
    public List<OperationLogDto> getOperationsByUserId(long userId,
                                                       Pageable pageable) {
        List<OperationLog> operationLogs =
                operationLogRepository.getByOperator(userId, pageable);
        return getOperationLogDtosWithAssociates(operationLogs);
    }

    @Override
    public List<OperationLogDto> getOperationsByResource(SystemResource systemResource) {
        List<OperationLog> operationLogs = operationLogRepository.getOperationLogsByResourceId(
                systemResource.getResourceId(),
                systemResource.getSystemResourceKind()
        );
        if (operationLogs == null || operationLogs.isEmpty()) {
            return List.of();
        }
        return getOperationLogDtosWithAssociates(operationLogs);
    }

    @Override
    public List<OperationLogDto> getOperationsByResource(SystemResource systemResource,
                                                         Pageable pageable) {
        List<OperationLog> operationLogs = operationLogRepository.getOperationLogsByResourceId(
                systemResource.getResourceId(),
                systemResource.getSystemResourceKind(),
                pageable
        );
        if (operationLogs == null || operationLogs.isEmpty()) {
            return List.of();
        }
        return getOperationLogDtosWithAssociates(operationLogs);
    }

    private List<OperationLogDto> getOperationLogDtosWithAssociates(List<OperationLog> operationLogs) {
        List<Long> ids = operationLogs.stream().map(OperationLog::getId).toList();
        List<OperationLogAssociation> associations =
                operationLogAssociationRepository.getByOperationIds(ids);

        List<OperationLogDto> operationLogDtos = operationLogs.stream().map(operationLog -> {
            OperateType operateType = operateTypeFinder
                    .getOperateType(operationLog.getOperateType());
            return OperationLogDto.from(operationLog, operateType);
        }).toList();

        List<OperationLogDto> result = new ArrayList<>(operationLogDtos);

        associations.stream().map(association -> {
            OperationLog operationLog = operationLogs.stream()
                    .filter(log -> log.getId().equals(association.getOperationId()))
                    .findFirst().orElse(null);
            if (operationLog == null) {
                return null;
            }
            OperateType operateType = operateTypeFinder
                    .getOperateType(operationLog.getOperateType());
            return OperationLogDto.from(operationLog, association, operateType);
        }).filter(Objects::nonNull).forEach(result::add);

        return result;
    }

    private void buildAssociation(Operation operation, long id) {
        List<SystemResource> associateResources = operation.associatedResources();
        if (associateResources == null || associateResources.isEmpty()) {
            return;
        }
        List<OperationLogAssociation> associations = new ArrayList<>();
        for (SystemResource associateResource : associateResources) {
            OperationLogAssociation association = OperationLogAssociation.builder()
                    .setOperationId(id)
                    .setResourceId(associateResource.getResourceId())
                    .setResourceKind(associateResource.getSystemResourceKind())
                    .build();
            associations.add(association);
        }
        operationLogAssociationRepository.insert(associations);
    }

    @Override
    public long getOperationLogCount(long operatorId) {
        return operationLogRepository.countByOperator(operatorId);
    }

    @Override
    public long getOperationLogCount(long resourceId, SystemResourceKind resourceKind) {
        return operationLogRepository.countByResourceId(resourceId, resourceKind);
    }
}
