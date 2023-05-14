package org.huel.cloudhub.client.disk.database.dao;

import org.huel.cloudhub.client.disk.domain.operatelog.OperationLog;
import org.huel.cloudhub.client.disk.domain.systembased.SystemResourceKind;
import org.huel.cloudhub.web.data.page.Offset;
import space.lingu.light.Dao;
import space.lingu.light.Query;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface OperationLogDao extends AutoPrimaryBaseDao<OperationLog> {
    @Query("SELECT * FROM operation_log WHERE operate_resource_id = {resourceId} AND resource_kind = {resourceKind} ORDER BY id DESC")
    List<OperationLog> getOperationLogsByResourceId(long resourceId, SystemResourceKind resourceKind);

    @Query("SELECT * FROM operation_log WHERE operate_resource_id = {resourceId} AND resource_kind = {resourceKind} ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<OperationLog> getOperationLogsByResourceId(long resourceId, SystemResourceKind resourceKind, Offset offset);

    @Query("SELECT * FROM operation_log WHERE operator = {operator} ORDER BY id DESC")
    List<OperationLog> getByOperator(long operator);

    @Query("SELECT * FROM operation_log WHERE operator = {operator} ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<OperationLog> getByOperator(long operator, Offset offset);

    @Override
    @Query("SELECT * FROM operation_log WHERE deleted = 0")
    List<OperationLog> getActives();

    @Override
    @Query("SELECT * FROM operation_log WHERE deleted = 1")
    List<OperationLog> getInactives();

    @Override
    @Query("SELECT * FROM operation_log WHERE id = {id}")
    OperationLog getById(long id);

    @Override
    @Query("SELECT * FROM operation_log WHERE id IN ({ids}) ORDER BY id DESC")
    List<OperationLog> getByIds(List<Long> ids);

    @Override
    @Query("SELECT COUNT(*) FROM operation_log WHERE deleted = 0")
    int countActive();

    @Override
    @Query("SELECT COUNT(*) FROM operation_log WHERE deleted = 1")
    int countInactive();

    @Override
    @Query("SELECT * FROM operation_log ORDER BY id DESC")
    List<OperationLog> get();

    @Override
    @Query("SELECT COUNT(*) FROM operation_log")
    int count();

    @Query("SELECT COUNT(*) FROM operation_log WHERE operator = {operator} ORDER BY id DESC")
    int count(long operator);

    @Query("SELECT COUNT(*) FROM operation_log WHERE operate_resource_id = {resourceId} AND resource_kind = {resourceKind} ORDER BY id DESC")
    int count(long resourceId, SystemResourceKind resourceKind);

    @Override
    @Query("SELECT * FROM operation_log ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<OperationLog> get(Offset offset);

    @Override
    default String getTableName() {
        return "operation_log";
    }
}
