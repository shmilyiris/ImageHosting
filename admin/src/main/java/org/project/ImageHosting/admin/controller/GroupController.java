package org.project.ImageHosting.admin.controller;

import lombok.RequiredArgsConstructor;
import org.project.ImageHosting.admin.common.convention.result.Result;
import org.project.ImageHosting.admin.common.convention.result.Results;
import org.project.ImageHosting.admin.dto.req.ImageGroupSaveReqDTO;
import org.project.ImageHosting.admin.dto.req.ImageGroupSortReqDTO;
import org.project.ImageHosting.admin.dto.req.ImageGroupUpdateReqDTO;
import org.project.ImageHosting.admin.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // RestController是一个组合注解（@Controller + @ResponseBody）
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/api/imghost/admin/v1/group")
    public Result<Void> save(@RequestBody ImageGroupSaveReqDTO reqParam) {
        groupService.saveGroup(reqParam.getName());
        return Results.success();
    }

    @PutMapping("/api/imghost/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody ImageGroupUpdateReqDTO reqParam) {
        groupService.updateGroup(reqParam);
        return Results.success();
    }

    @DeleteMapping("/api/imghost/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return Results.success();
    }

    @PostMapping("/api/imghost/admin/v1/group")
    public Result<Void> sortGroup(@RequestBody List<ImageGroupSortReqDTO> reqParam) {
        groupService.sortGroup(reqParam);
        return Results.success();
    }

}
