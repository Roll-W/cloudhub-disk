package org.huel.cloudhub.client.disk.controller.tag;

import org.huel.cloudhub.client.disk.controller.AdminApi;
import org.huel.cloudhub.client.disk.controller.tag.vo.CreateTagGroupRequest;
import org.huel.cloudhub.client.disk.controller.tag.vo.TagGroupVo;
import org.huel.cloudhub.client.disk.domain.operatelog.BuiltinOperationType;
import org.huel.cloudhub.client.disk.domain.operatelog.context.BuiltinOperate;
import org.huel.cloudhub.client.disk.domain.tag.ContentTag;
import org.huel.cloudhub.client.disk.domain.tag.ContentTagService;
import org.huel.cloudhub.client.disk.domain.tag.dto.ContentTagInfo;
import org.huel.cloudhub.client.disk.domain.tag.dto.TagGroupDto;
import org.huel.cloudhub.client.disk.system.pages.PageableInterceptor;
import org.huel.cloudhub.web.HttpResponseEntity;
import org.huel.cloudhub.web.data.page.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class ContentTagController {
    private final ContentTagService contentTagService;
    private final PageableInterceptor pageableInterceptor;

    public ContentTagController(ContentTagService contentTagService,
                                PageableInterceptor pageableInterceptor) {
        this.contentTagService = contentTagService;
        this.pageableInterceptor = pageableInterceptor;
    }

    @GetMapping("/tags")
    public HttpResponseEntity<List<ContentTagInfo>> getTags(
            Pageable pageable) {
        List<ContentTagInfo> contentTagInfos =
                contentTagService.getTags(pageable);
        return HttpResponseEntity.success(
                pageableInterceptor.interceptPageable(
                        contentTagInfos,
                        pageable,
                        ContentTag.class
                )
        );
    }

    @GetMapping("/tags/{tagId}")
    public HttpResponseEntity<ContentTagInfo> getTag(
            @PathVariable("tagId") Long id) {
        return HttpResponseEntity.success(
                contentTagService.getTagById(id)
        );
    }

    @GetMapping("/tags/groups")
    public HttpResponseEntity<List<TagGroupVo>> getTagGroups(
            Pageable pageable) {
        List<TagGroupDto> tagGroupDtos =
                contentTagService.getTagGroups(pageable);

        return HttpResponseEntity.success(
                pageableInterceptor.interceptPageable(
                        tagGroupDtos.stream()
                                .map(TagGroupVo::from)
                                .toList(),
                        pageable,
                        ContentTag.class
                )
        );
    }

    @GetMapping("/tags/groups/{groupId}")
    public HttpResponseEntity<TagGroupDto> getTagGroup(
            @PathVariable("groupId") Long groupId) {

        return HttpResponseEntity.success(
                contentTagService.getTagGroupById(groupId)
        );
    }

    @PostMapping("/tags/groups")
    @BuiltinOperate(BuiltinOperationType.CREATE_TAG_GROUP)
    public HttpResponseEntity<Void> createTagGroup(
            @RequestBody CreateTagGroupRequest request) {
        contentTagService.createContentTagGroup(
                request.name(),
                request.description(),
                request.keywordSearchScope()
        );
        return HttpResponseEntity.success();
    }

    @PutMapping("/tags/groups/{groupId}")
    @BuiltinOperate(BuiltinOperationType.UPDATE_TAG_GROUP)
    public HttpResponseEntity<Void> updateTagGroup() {
        return HttpResponseEntity.success();
    }


    @PostMapping("/tags/groups/{groupId}/infile")
    @BuiltinOperate(BuiltinOperationType.CREATE_TAG)
    public HttpResponseEntity<Void> importTags(
            @PathVariable("groupId") Long groupId,
            @RequestPart(name = "file") MultipartFile file) throws IOException {
        contentTagService.importFromKeywordsFile(file.getInputStream(), groupId);

        return HttpResponseEntity.success();
    }

    @GetMapping("/tags/groups/{groupId}/infile")
    public void exportTags(HttpServletResponse servletResponse) {
        // exports
    }
}
