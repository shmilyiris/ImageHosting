package org.project.ImageHosting.admin.controller;

import lombok.RequiredArgsConstructor;
import org.project.ImageHosting.admin.common.convention.result.Result;
import org.project.ImageHosting.admin.common.convention.result.Results;
import org.project.ImageHosting.admin.dto.req.ImageGroupSaveReqDTO;
import org.project.ImageHosting.admin.service.GroupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/api/imghost/admin/v1/group")
    public Result<Void> save(@RequestBody ImageGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }
}
