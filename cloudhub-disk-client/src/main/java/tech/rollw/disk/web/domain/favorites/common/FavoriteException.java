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

package tech.rollw.disk.web.domain.favorites.common;

import tech.rollw.disk.common.BusinessRuntimeException;
import tech.rollw.disk.common.ErrorCode;

/**
 * @author RollW
 */
public class FavoriteException extends BusinessRuntimeException {
    public FavoriteException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FavoriteException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

    public FavoriteException(ErrorCode errorCode, String message, Throwable cause, Object... args) {
        super(errorCode, message, cause, args);
    }

    public FavoriteException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
