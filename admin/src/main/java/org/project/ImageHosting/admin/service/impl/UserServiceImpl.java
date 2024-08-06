package org.project.ImageHosting.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.project.ImageHosting.admin.dao.entity.UserDO;
import org.project.ImageHosting.admin.dao.mapper.UserMapper;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;
import org.project.ImageHosting.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Override
    public UserRespDTO getUserByUserName(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(userDO, res);
        return res;
    }
}
