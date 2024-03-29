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

package tech.rollw.disk.web.domain.tag.common;

import tech.rollw.disk.common.BusinessRuntimeException;
import tech.rollw.disk.common.ErrorCode;
import tech.rollw.disk.common.ErrorCodeFinder;
import tech.rollw.disk.common.ErrorCodeMessageProvider;
import space.lingu.NonNull;

import java.util.List;

/**
 *
 * @author RollW
 */
public enum ContentTagErrorCode implements ErrorCode, ErrorCodeFinder, ErrorCodeMessageProvider {
    ERROR_CONTENT_TAG("B3500", 400),
    ERROR_TAG_EXIST("B3501", 400),
    ERROR_TAG_NOT_EXIST("B3502", 404),
    ERROR_TAG_NAME_EMPTY("B3503", 400),

    ERROR_TAG_GROUP_EXIST("B3504", 400),
    ERROR_TAG_GROUP_NOT_EXIST("B3505", 404),
    ERROR_TAG_GROUP_NAME_EMPTY("B3506", 400),
    ERROR_TAG_GROUP_NAME_EXIST("B3507", 400),

    ERROR_NAME_INVALID("B3508", 400),
    ERROR_DESCRIPTION_TOO_LONG("B3509", 400),

    ERROR_TAG_GROUP_NOT_EMPTY("B3510", 400),
    ERROR_TAG_GROUP_NOT_FOUND("B3511", 404),
    ERROR_TAG_GROUP_DELETED("B3512", 400),

    ERROR_TAG_DELETED("B3513", 400),
    ;

    private final String value;
    private final int status;

    ContentTagErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ContentTagError: %s, code: %s".formatted(name(), getCode());
    }

    @NonNull
    @Override
    public String getCode() {
        return value;
    }

    @NonNull
    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public ErrorCode fromThrowable(Throwable e, ErrorCode defaultErrorCode) {
        if (e instanceof BusinessRuntimeException sys) {
            return sys.getErrorCode();
        }
        return null;
    }

    @Override
    public ErrorCode findErrorCode(String codeValue) {
        return ErrorCodeFinder.from(values(), codeValue);
    }

    private static final List<ErrorCode> CODES = List.of(values());

    @Override
    public List<ErrorCode> listErrorCodes() {
        return CODES;
    }

    public static ErrorCodeFinder getFinderInstance() {
        return ERROR_CONTENT_TAG;
    }
}
