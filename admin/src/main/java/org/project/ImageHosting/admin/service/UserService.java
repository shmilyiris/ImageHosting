package org.project.ImageHosting.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.project.ImageHosting.admin.dao.entity.UserDO;
import org.project.ImageHosting.admin.dto.req.UserLoginReqDTO;
import org.project.ImageHosting.admin.dto.req.UserRegisterReqDTO;
import org.project.ImageHosting.admin.dto.req.UserUpdateReqDTO;
import org.project.ImageHosting.admin.dto.resp.UserLoginRespDTO;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> { // 继承IService，不需要单独写增删改查的代码

    /**
     * 根据用户名查看用户信息
     */
    public UserRespDTO getUserByUsername(String username);

    /**
     * 根据用户名查看是否已存在该用户名
     */
    public Boolean hasUsername(String username);

    /**
     * 用户注册
     */
    public void register(UserRegisterReqDTO reqParam);

    /**
     * 用户信息更新
     */
    public void update(UserUpdateReqDTO reqParam);

    /**
     * 用户登录状态校验
     */
    public Boolean checkLogin(String username, String token);

    /**
     * 用户登录
     */
    public UserLoginRespDTO login(UserLoginReqDTO reqParam);

    /**
     * 用户登出
     */
    public Boolean logout(String username, String token);
}
