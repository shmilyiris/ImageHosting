package org.project.ImageHosting.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.project.ImageHosting.admin.dao.entity.UserDO;
import org.project.ImageHosting.admin.dto.req.UserRegisterReqDTO;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> { // 继承IService，不需要单独写增删改查的代码

    /**
     * 根据用户名查看用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    public UserRespDTO getUserByUsername(String username);

    /**
     * 根据用户名查看是否已存在该用户名
     * @param username 用户名
     * @return True or False
     */
    public Boolean hasUsername(String username);

    /**
     * 用户注册
     * @param reqParam 用户注册输入参数
     */
    public void register(UserRegisterReqDTO reqParam);
}
