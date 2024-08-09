package org.project.ImageHosting.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.project.ImageHosting.admin.common.convention.exception.ClientException;
import org.project.ImageHosting.admin.common.convention.exception.ServiceException;
import org.project.ImageHosting.admin.common.enums.UserErrorCodeEnum;
import org.project.ImageHosting.admin.dao.entity.UserDO;
import org.project.ImageHosting.admin.dao.mapper.UserMapper;
import org.project.ImageHosting.admin.dto.req.UserRegisterReqDTO;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;
import org.project.ImageHosting.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import static org.project.ImageHosting.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static org.project.ImageHosting.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static org.project.ImageHosting.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;


/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor // 构造器方式注入
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null)
            throw new ClientException(UserErrorCodeEnum.USER_NULL);

        UserRespDTO res = new UserRespDTO();
        BeanUtils.copyProperties(userDO, res);
        return res;
    }

    @Override
    public Boolean hasUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        return baseMapper.selectOne(queryWrapper) != null;
//        return userRegisterCachePenetrationBloomFilter.contains(username); // 布隆过滤器判断是否存在
    }

    @Override
    public void register(UserRegisterReqDTO reqParam) {
        if (hasUsername(reqParam.getUsername()))
            throw new ClientException(USER_NAME_EXIST);

        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + reqParam.getUsername());
        try {
            // 业务逻辑
            if (lock.tryLock()) {
                int inserted = baseMapper.insert(BeanUtil.toBean(reqParam, UserDO.class));
                if (inserted < 1) {
                    throw new ClientException(USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(reqParam.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        } finally {
            // 释放锁
            lock.unlock();
        }

    }

}
