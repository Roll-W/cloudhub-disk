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

package tech.rollw.disk.web.domain.systembased.validate;

import tech.rollw.disk.web.domain.systembased.SystemResourceSupportable;
import tech.rollw.disk.common.BusinessRuntimeException;
import tech.rollw.disk.common.ErrorCode;
import space.lingu.NonNull;

/**
 * @author RollW
 */
public interface Validator extends SystemResourceSupportable {
    default void validateThrows(String value, @NonNull FieldType fieldType)
            throws BusinessRuntimeException {
        ErrorCode errorCode = validate(value, fieldType);
        if (errorCode.failed()) {
            throw new BusinessRuntimeException(errorCode);
        }
    }

    @NonNull
    ErrorCode validate(String value, @NonNull FieldType fieldType);

}
