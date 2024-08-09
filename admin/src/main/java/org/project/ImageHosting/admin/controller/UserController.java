package org.project.ImageHosting.admin.controller;

import lombok.RequiredArgsConstructor;
import org.project.ImageHosting.admin.common.convention.result.Result;
import org.project.ImageHosting.admin.common.convention.result.Results;
import org.project.ImageHosting.admin.dto.req.UserLoginReqDTO;
import org.project.ImageHosting.admin.dto.req.UserRegisterReqDTO;
import org.project.ImageHosting.admin.dto.req.UserUpdateReqDTO;
import org.project.ImageHosting.admin.dto.resp.UserLoginRespDTO;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;
import org.project.ImageHosting.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        // 返回实体异常处理
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名判断是否存在
     */
    @GetMapping("/api/imghost/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/imghost/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO reqParam) {
        userService.register(reqParam);
        return Results.success();
    }

    /**
     * 用户信息更新
     */
    @PutMapping("/api/imghost/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO reqParam) {
        userService.update(reqParam);
        return Results.success();
    }

    @GetMapping("/api/imghost/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam("username") String username, @RequestParam("token") String token) {
        return Results.success(userService.checkLogin(username, token));
    }

    @PostMapping("/api/imghost/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO reqParam) {
        return Results.success(userService.login(reqParam));
    }

    @DeleteMapping("/api/imghost/v1/user/logout")
    public Result<Boolean> logout(@RequestParam("username") String username, @RequestParam("token") String token) {
        return Results.success(userService.logout(username, token));
    }

}
