package org.project.ImageHosting.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.project.ImageHosting.admin.dao.entity.UserDO;
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
    public UserRespDTO getUserByUserName(String username);
}
