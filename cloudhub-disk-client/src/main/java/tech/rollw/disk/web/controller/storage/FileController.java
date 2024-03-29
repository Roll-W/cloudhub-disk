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

package tech.rollw.disk.web.controller.storage;

import com.google.common.base.Strings;
import tech.rollw.disk.web.common.ApiContextHolder;
import tech.rollw.disk.web.controller.Api;
import tech.rollw.disk.web.controller.HttpRangeUtils;
import tech.rollw.disk.web.controller.ParameterHelper;
import tech.rollw.disk.web.domain.authentication.AuthenticationException;
import tech.rollw.disk.web.domain.operatelog.BuiltinOperationType;
import tech.rollw.disk.web.domain.operatelog.context.BuiltinOperate;
import tech.rollw.disk.web.domain.storage.StorageService;
import tech.rollw.disk.web.domain.user.LegalUserType;
import tech.rollw.disk.web.domain.user.UserIdentity;
import tech.rollw.disk.web.domain.userstorage.*;
import tech.rollw.disk.web.domain.userstorage.common.StorageErrorCode;
import tech.rollw.disk.web.domain.userstorage.common.StorageException;
import tech.rollw.disk.web.domain.userstorage.dto.FileInfo;
import tech.rollw.disk.web.domain.userstorage.dto.FileStorageInfo;
import tech.rollw.disk.web.domain.userstorage.dto.SimpleStorageOwner;
import tech.rollw.disk.web.controller.storage.vo.StorageVo;
import tech.rollw.disk.common.AuthErrorCode;
import tech.rollw.disk.common.HttpResponseEntity;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author RollW
 */
@Api
public class FileController {
    public static final String ACCEPT_TYPE = "X-CFS-Accept-Type";
    public static final String DISPOSITION_TYPE = "X-CFS-Disposition-Type";

    private final UserFileStorageService userFileStorageService;
    private final UserStorageSearchService userStorageSearchService;
    private final StorageDownloadTokenProvider storageDownloadTokenProvider;
    private final StorageService storageService;
    private final StorageActionService storageActionService;

    public FileController(UserFileStorageService userFileStorageService,
                          UserStorageSearchService userStorageSearchService,
                          StorageDownloadTokenProvider storageDownloadTokenProvider,
                          StorageService storageService,
                          StorageActionService storageActionService) {
        this.userFileStorageService = userFileStorageService;
        this.userStorageSearchService = userStorageSearchService;
        this.storageDownloadTokenProvider = storageDownloadTokenProvider;
        this.storageService = storageService;
        this.storageActionService = storageActionService;
    }

    @BuiltinOperate(BuiltinOperationType.CREATE_FILE)
    @PutMapping(value = "/{ownerType}/{ownerId}/disk/{directory}/{fileName}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpResponseEntity<StorageVo> uploadFile(@PathVariable("directory") Long directoryId,
                                                    @PathVariable("ownerId") Long ownerId,
                                                    @PathVariable("ownerType") String type,
                                                    @PathVariable("fileName") String fileName,
                                                    @RequestPart("file") MultipartFile file) throws IOException {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        UserIdentity userIdentity = context.userInfo();

        if (userIdentity.getUserId() != ownerId) {
            throw new AuthenticationException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }

        LegalUserType legalUserType = LegalUserType.from(type);
        StorageOwner storageOwner = new SimpleStorageOwner(ownerId, legalUserType);
        FileStorageInfo fileStorageInfo = new FileStorageInfo(
                fileName, directoryId, storageOwner, userIdentity);
        String mimeType = file.getContentType();

        FileStreamInfo fileStreamInfo = new FileStreamInfo(
                file.getInputStream(),
                mimeType,
                file.getSize(),
                FileType.fromMimeType(mimeType)
        );
        AttributedStorage storage = userFileStorageService.uploadFile(
                fileStorageInfo, fileStreamInfo);

        return HttpResponseEntity.success(
                StorageVo.from(storage)
        );
    }

    @BuiltinOperate(BuiltinOperationType.CREATE_FILE)
    @PostMapping(value = "/{ownerType}/{ownerId}/disk/{directory}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpResponseEntity<StorageVo> uploadFile(
            @PathVariable("directory") Long directoryId,
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("ownerType") String type,
            @RequestPart("file") MultipartFile file) throws IOException {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        UserIdentity userIdentity = context.userInfo();

        if (userIdentity.getUserId() != ownerId) {
            throw new AuthenticationException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }

        LegalUserType legalUserType = LegalUserType.from(type);
        StorageOwner storageOwner = new SimpleStorageOwner(ownerId, legalUserType);
        FileStorageInfo fileStorageInfo = new FileStorageInfo(
                file.getOriginalFilename(), directoryId, storageOwner, userIdentity);
        String mimeType = file.getContentType();

        FileStreamInfo fileStreamInfo = new FileStreamInfo(
                file.getInputStream(),
                mimeType,
                file.getSize(),
                FileType.fromMimeType(mimeType)
        );
        AttributedStorage storage = userFileStorageService.uploadFile(
                fileStorageInfo, fileStreamInfo);

        return HttpResponseEntity.success(
                StorageVo.from(storage)
        );
    }


    @PostMapping("/{type}/{ownerId}/disk/file/{fileId}/token")
    public HttpResponseEntity<String> getDownloadFileToken(
            @PathVariable("fileId") Long fileId,
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("type") String type) {
        StorageOwner storageOwner = ParameterHelper.buildStorageOwner(ownerId, type);
        AttributedStorage storage =
                userStorageSearchService.findFile(fileId, storageOwner);
        if (storage.isDeleted()) {
            throw new StorageException(StorageErrorCode.ERROR_FILE_NOT_EXIST);
        }

        return HttpResponseEntity.success(
                storageDownloadTokenProvider.getDownloadToken(storage));
    }

    @GetMapping("/quickfire/disk/{token}")
    public void downloadFileByToken(@PathVariable("token") String token,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        StorageIdentity storageIdentity =
                storageDownloadTokenProvider.verifyDownloadToken(token);
        FileInfo fileInfo =
                userStorageSearchService.findFile(storageIdentity.getStorageId());
        downloadFile(fileInfo, request, response);
    }

    @GetMapping("/{ownerType}/{ownerId}/disk/file/{fileId}")
    public void downloadFile(@PathVariable("fileId") Long fileId,
                             @PathVariable("ownerId") Long ownerId,
                             @PathVariable("ownerType") String type,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        StorageOwner storageOwner = ParameterHelper.buildStorageOwner(ownerId, type);
        FileInfo storage =
                userStorageSearchService.findFile(fileId, storageOwner);
        if (storage.isDeleted()) {
            throw new StorageException(StorageErrorCode.ERROR_FILE_NOT_EXIST);
        }
        downloadFile(storage, request, response);
    }

    private void downloadFile(FileInfo fileInfo,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        String dispositionType = getDispositionType(request);
        String contentType = getResponseType(fileInfo, request);
        response.setContentType(contentType);

        if (fileInfo.getFileType() == FileType.TEXT) {
            response.setCharacterEncoding("utf-8");
        }
        List<HttpRange> ranges = HttpRangeUtils.tryGetsRange(request);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("Content-Security-Policy", "frame-ancestors 'self' localhost:* 127.0.0.1:*");
        response.setHeader("Content-Disposition",
                dispositionType + ";filename*=utf-8''" + getEncodedFileName(fileInfo.getName()));
        long length = storageService.getFileSize(fileInfo.getFileId());

        if (!ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(length);
            long end = range.getRangeEnd(length);
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
            response.setHeader("Content-Length", String.valueOf(end - start + 1));
            storageService.getFile(
                    fileInfo.getFileId(),
                    response.getOutputStream(),
                    start,
                    end
            );
            return;
        }
        response.setHeader("Content-Length", String.valueOf(length));
        storageService.getFile(fileInfo.getFileId(), response.getOutputStream());
    }

    private String getEncodedFileName(String fileName){
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    private String getResponseType(FileInfo fileInfo, HttpServletRequest request) {
        String contentType = request.getHeader(ACCEPT_TYPE);
        if (Strings.isNullOrEmpty(contentType)) {
            return fileInfo.mimeType();
        }
        return contentType;
    }

    private String getDispositionType(HttpServletRequest request) {
        String dispositionType = request.getHeader(DISPOSITION_TYPE);
        String param = request.getParameter("disposition");
        if (Strings.isNullOrEmpty(dispositionType)) {
            dispositionType = param;
        }
        if (Strings.isNullOrEmpty(dispositionType)) {
            return "attachment";
        }
        return dispositionType;
    }

    @BuiltinOperate(BuiltinOperationType.DELETE_FILE)
    @DeleteMapping("/{ownerType}/{ownerId}/disk/file/{fileId}")
    public HttpResponseEntity<Void> deleteFile(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("ownerType") String type,
            @PathVariable("fileId") Long fileId) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        UserIdentity userIdentity = context.userInfo();
        if (userIdentity.getUserId() != ownerId) {
            throw new AuthenticationException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        StorageOwner storageOwner = ParameterHelper.buildStorageOwner(ownerId, type);
        AttributedStorage storage = userStorageSearchService.findFile(fileId, storageOwner);
        StorageAction storageAction =
                storageActionService.openStorageAction(storage);
        storageAction.delete();
        return HttpResponseEntity.success();
    }


}
