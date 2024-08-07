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

package tech.rollw.disk.web.controller.share;

import tech.rollw.disk.web.controller.storage.vo.StorageVo;
import tech.rollw.disk.web.domain.share.dto.SharePasswordInfo;
import tech.rollw.disk.web.domain.userstorage.AttributedStorage;

/**
 * @author RollW
 */
public record ShareStorageInfoVo(
        long id,
        long creatorId,
        String shareCode,
        boolean isPublic,
        long expireTime,
        long createTime,
        StorageVo storage
) {

    public static ShareStorageInfoVo from(SharePasswordInfo sharePasswordInfo,
                                          AttributedStorage storage) {
        return new ShareStorageInfoVo(
                sharePasswordInfo.id(),
                sharePasswordInfo.creatorId(),
                sharePasswordInfo.shareCode(),
                sharePasswordInfo.isPublic(),
                sharePasswordInfo.expireTime(),
                sharePasswordInfo.createTime(),
                StorageVo.from(storage)
        );
    }
}
