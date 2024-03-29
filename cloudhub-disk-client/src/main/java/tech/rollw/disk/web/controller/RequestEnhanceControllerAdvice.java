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

package tech.rollw.disk.web.controller;

import tech.rollw.disk.web.common.ApiContextHolder;
import tech.rollw.disk.common.RequestMetadata;
import tech.rollw.disk.common.RequestUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@ControllerAdvice
public class RequestEnhanceControllerAdvice {

    @ModelAttribute
    public RequestMetadata requestMetadata(HttpServletRequest request) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        if (apiContext != null && apiContext.ip() != null) {
            return new RequestMetadata(
                    apiContext.ip(),
                    request.getHeader("User-Agent"),
                    apiContext.timestamp()
            );
        }
        return new RequestMetadata(
                RequestUtils.getRemoteIpAddress(request),
                request.getHeader("User-Agent"),
                System.currentTimeMillis()
        );
    }
}
