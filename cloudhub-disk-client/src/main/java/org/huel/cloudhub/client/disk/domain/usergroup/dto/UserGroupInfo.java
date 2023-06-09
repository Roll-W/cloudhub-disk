package org.huel.cloudhub.client.disk.domain.usergroup.dto;

import org.huel.cloudhub.client.disk.domain.usergroup.GroupSettingKeys;
import org.huel.cloudhub.client.disk.domain.usergroup.UserGroup;

import java.util.Map;

/**
 * @author RollW
 */
public record UserGroupInfo(
        long id,
        String name,
        String description,
        Map<String, String> settings,
        long createTime,
        long updateTime
) {

    public static final UserGroupInfo DEFAULT =
            UserGroupInfo.from(GroupSettingKeys.DEFAULT);

    public static UserGroupInfo from(UserGroup userGroup) {
        return new UserGroupInfo(
                userGroup.getId(),
                userGroup.getName(),
                userGroup.getDescription(),
                userGroup.getSettings(),
                userGroup.getCreateTime(),
                userGroup.getUpdateTime()
        );
    }
}