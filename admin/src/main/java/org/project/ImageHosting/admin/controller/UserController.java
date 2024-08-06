package org.project.ImageHosting.admin.controller;

import lombok.RequiredArgsConstructor;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;
import org.project.ImageHosting.admin.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制层
 */
@RestController // RestController是一个组合注解（@Controller + @ResponseBody）
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/api/imghost/v1/user/{username}")
    public UserRespDTO getUserByUserName(@PathVariable("username") String username) {
        return userService.getUserByUserName(username);
    }
}
