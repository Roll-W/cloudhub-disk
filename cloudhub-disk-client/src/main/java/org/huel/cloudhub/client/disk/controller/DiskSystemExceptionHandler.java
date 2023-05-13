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

package org.huel.cloudhub.client.disk.controller;

import com.google.common.base.Strings;
import com.google.common.base.VerifyException;
import org.huel.cloudhub.client.disk.common.ApiContextHolder;
import org.huel.cloudhub.client.disk.common.ParameterMissingException;
import org.huel.cloudhub.client.disk.system.ErrorRecord;
import org.huel.cloudhub.client.disk.system.ErrorRecordVo;
import org.huel.cloudhub.web.AuthErrorCode;
import org.huel.cloudhub.web.BusinessRuntimeException;
import org.huel.cloudhub.web.CommonErrorCode;
import org.huel.cloudhub.web.ErrorCode;
import org.huel.cloudhub.web.ErrorCodeFinder;
import org.huel.cloudhub.web.HttpResponseEntity;
import org.huel.cloudhub.web.IoErrorCode;
import org.huel.cloudhub.web.UserErrorCode;
import org.huel.cloudhub.web.WebCommonErrorCode;
import org.huel.cloudhub.web.data.page.Offset;
import org.huel.cloudhub.web.data.page.Page;
import org.huel.cloudhub.web.data.page.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import space.lingu.light.LightRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Handle {@link Exception}s
 *
 * @author RollW
 */
@ControllerAdvice
@RestController
public class DiskSystemExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DiskSystemExceptionHandler.class);
    private final ErrorCodeFinder errorCodeFinder;
    private final MessageSource messageSource;

    public DiskSystemExceptionHandler(ErrorCodeFinder errorCodeFinder,
                                      MessageSource messageSource) {
        this.errorCodeFinder = errorCodeFinder;
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public HttpResponseEntity<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        HttpMethod method = HttpMethod.resolve(e.getHttpMethod());
        if (method == HttpMethod.OPTIONS) {
            return new HttpResponseEntity<>(HttpStatus.OK);
        }
        return HttpResponseEntity.of(
                CommonErrorCode.ERROR_NOT_FOUND,
                e.getMessage()
        );
    }


    @ExceptionHandler(LightRuntimeException.class)
    public HttpResponseEntity<Void> handle(LightRuntimeException e) {
        logger.error("Unexpected sql error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(ParameterMissingException.class)
    public HttpResponseEntity<Void> handle(ParameterMissingException e) {
        return HttpResponseEntity.of(
                WebCommonErrorCode.ERROR_PARAM_MISSING,
                e.getMessage()
        );
    }

    @ExceptionHandler({
            BindException.class,
            MethodArgumentNotValidException.class,
    })
    public HttpResponseEntity<Void> handleParamException(Exception e) {
        return HttpResponseEntity.of(
                WebCommonErrorCode.ERROR_PARAM_FAILED,
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public HttpResponseEntity<Void> handleMethodTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        return HttpResponseEntity.of(
                WebCommonErrorCode.ERROR_PARAM_FAILED,
                e.getMessage()
        );
    }

    @ExceptionHandler(BusinessRuntimeException.class)
    public HttpResponseEntity<Void> handle(BusinessRuntimeException e) {
        logger.trace("System runtime error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                tryGetMessage(e)
        );
    }

    private String tryGetMessage(BusinessRuntimeException e) {
        if (Strings.isNullOrEmpty(e.getMessage())) {
            return null;
        }
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Locale locale = context == null ? Locale.getDefault() : context.locale();
        try {
            return messageSource.getMessage(
                    e.getMessage(),
                    e.getArgs(),
                    locale
            );
        } catch (Exception ex) {
            return tryFormatMessage(e.getMessage(), e.getArgs());
        }
    }

    private String tryFormatMessage(String message, Object[] args) {
        if (args == null || args.length == 0) {
            return message;
        }
        try {
            return MessageFormat.format(message, args);
        } catch (Exception e) {
            return message;
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpResponseEntity<Void> handle(NullPointerException e) {
        logger.error("Null exception : %s".formatted(e.toString()), e);
        recordErrorLog(CommonErrorCode.ERROR_NULL, e);
        return HttpResponseEntity.of(
                CommonErrorCode.ERROR_NULL,
                e.getMessage()
        );
    }

    // treat VerifyException as parameter failed
    @ExceptionHandler(VerifyException.class)
    public HttpResponseEntity<Void> handle(VerifyException e) {
        return HttpResponseEntity.of(
                WebCommonErrorCode.ERROR_PARAM_FAILED,
                e.getMessage()
        );
    }

    @ExceptionHandler(FileNotFoundException.class)
    public HttpResponseEntity<Void> handle(FileNotFoundException e) {
        recordErrorLog(IoErrorCode.ERROR_FILE_NOT_FOUND, e);
        return HttpResponseEntity.of(
                IoErrorCode.ERROR_FILE_NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(IOException.class)
    public HttpResponseEntity<Void> handle(IOException e) {
        logger.error("IO Error: %s".formatted(e.toString()), e);
        ErrorRecord errorRecord = recordErrorLog(e);
        return HttpResponseEntity.of(
                errorRecord.errorCode(),
                e.getMessage()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public HttpResponseEntity<Void> handleAuthException(AccessDeniedException e) {
        return HttpResponseEntity.of(AuthErrorCode.ERROR_UNAUTHORIZED_USE);
    }

    @ExceptionHandler(DisabledException.class)
    public HttpResponseEntity<Void> handleAuthException(DisabledException e) {
        return HttpResponseEntity.of(UserErrorCode.ERROR_USER_DISABLED);
    }



    @ExceptionHandler(AuthenticationException.class)
    public HttpResponseEntity<Void> handleAuthException(AuthenticationException e) {
        if (e instanceof InsufficientAuthenticationException) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        logger.error("Auth Error: %s, type: %s".formatted(e.toString(), e.getClass().getCanonicalName()), e);
        return HttpResponseEntity.of(AuthErrorCode.ERROR_NOT_HAS_ROLE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HttpResponseEntity<Void> handleHttpMessageNotReadableException() {
        return HttpResponseEntity.of(WebCommonErrorCode.ERROR_BODY_MISSING);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public HttpResponseEntity<Void> handleHttpException() {
        return HttpResponseEntity.of(WebCommonErrorCode.ERROR_METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public HttpResponseEntity<Void> handle(Exception e) {
        logger.error("Error: %s".formatted(e.toString()), e);
        ErrorRecord errorRecord = recordErrorLog(e);
        return HttpResponseEntity.of(
                errorRecord.errorCode(),
                e.getMessage()
        );
    }

    private final Deque<ErrorRecord> errorRecords = new LinkedBlockingDeque<>();

    private ErrorRecord recordErrorLog(Throwable throwable) {
        ErrorCode errorCode = errorCodeFinder.fromThrowable(throwable);
        return recordErrorLog(errorCode, throwable);
    }

    private ErrorRecord recordErrorLog(ErrorCode errorCode, Throwable throwable) {
        long time = System.currentTimeMillis();
        ErrorRecord errorRecord = new ErrorRecord(errorCode, throwable, time);
        putErrorRecord(errorRecord);
        return errorRecord;
    }

    @Async
    void putErrorRecord(ErrorRecord errorRecord) {
        errorRecords.addLast(errorRecord);
        if (errorRecords.size() > 100) {
            errorRecords.removeFirst();
        }
    }

    @GetMapping("/api/v1/admin/system/errors")
    public HttpResponseEntity<List<ErrorRecordVo>> getErrorRecords(Pageable pageable) {
        Offset offset = pageable.toOffset();

        List<ErrorRecordVo> errorRecordVos = errorRecords.stream()
                .skip(offset.offset())
                .limit(offset.limit())
                .map(ErrorRecordVo::from)
                .toList();

        return HttpResponseEntity.success(
                Page.of(
                        offset,
                        errorRecords.size(),
                        errorRecordVos
                )
        );
    }
}
