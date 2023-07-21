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

package tech.rollw.disk.web.domain.userstats.vo;

import tech.rollw.disk.web.domain.user.LegalUserType;
import tech.rollw.disk.web.domain.userstats.dto.UserStatisticsDetail;
import tech.rollw.disk.common.LongKeyValue;

import java.util.List;

/**
 * @author RollW
 */
public record UserStatisticsVo(
        long id,
        long userId,
        LegalUserType userType,
        List<LongKeyValue> statistics
) {

    public static UserStatisticsVo from(
            UserStatisticsDetail userStatisticsDetail) {
        return new UserStatisticsVo(
                userStatisticsDetail.id(),
                userStatisticsDetail.userId(),
                userStatisticsDetail.userType(),
                LongKeyValue.from(userStatisticsDetail.statistics())
        );
    }
}
