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

import tech.rollw.disk.common.HttpResponseEntity;
import tech.rollw.disk.web.controller.AdminApi;
import tech.rollw.disk.web.domain.share.ShareSearchService;
import tech.rollw.disk.web.domain.share.dto.SharePasswordInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class ShareManageController {
    private final ShareSearchService shareSearchService;

    public ShareManageController(ShareSearchService shareSearchService) {
        this.shareSearchService = shareSearchService;
    }

    @GetMapping("/shares")
    public HttpResponseEntity<List<SharePasswordInfo>> getShareInfos() {
        // TODO:
        return HttpResponseEntity.success();
    }

    @GetMapping("/users/{userId}/shares")
    public HttpResponseEntity<List<SharePasswordInfo>> getShareInfos(
            @PathVariable("userId") long userId) {
        List<SharePasswordInfo> shareInfos =
                shareSearchService.findByUserId(userId);
        return HttpResponseEntity.success(shareInfos);
    }
}
