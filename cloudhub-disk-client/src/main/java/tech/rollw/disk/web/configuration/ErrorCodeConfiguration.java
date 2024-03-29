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

package tech.rollw.disk.web.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.rollw.disk.common.*;
import tech.rollw.disk.web.domain.favorites.common.FavoriteErrorCode;
import tech.rollw.disk.web.domain.share.common.UserShareErrorCode;
import tech.rollw.disk.web.domain.storagepermission.common.StoragePermissionErrorCode;
import tech.rollw.disk.web.domain.tag.common.ContentTagErrorCode;
import tech.rollw.disk.web.domain.usergroup.common.UserGroupErrorCode;
import tech.rollw.disk.web.domain.userstorage.common.StorageErrorCode;

/**
 * @author RollW
 */
@Configuration
public class ErrorCodeConfiguration {

    @Bean
    public ErrorCodeMessageProvider errorCodeMessageProvider(MessageSource messageSource) {
        return new ErrorCodeMessageProviderImpl(messageSource);
    }

    @Bean
    public ErrorCodeFinderChain errorCodeFinderChain() {
        return ErrorCodeFinderChain.start(
                WebCommonErrorCode.getFinderInstance(),
                AuthErrorCode.getFinderInstance(),
                DataErrorCode.getFinderInstance(),
                IoErrorCode.getFinderInstance(),
                UserErrorCode.getFinderInstance(),
                StorageErrorCode.getFinderInstance(),
                StoragePermissionErrorCode.getFinderInstance(),
                UserShareErrorCode.getFinderInstance(),
                UserGroupErrorCode.getFinderInstance(),
                FavoriteErrorCode.getFinderInstance(),
                ContentTagErrorCode.getFinderInstance()
        );
    }

}
