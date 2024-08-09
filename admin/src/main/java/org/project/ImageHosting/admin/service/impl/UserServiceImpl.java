package org.project.ImageHosting.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.project.ImageHosting.admin.common.convention.exception.ClientException;
import org.project.ImageHosting.admin.common.convention.exception.ServiceException;
import org.project.ImageHosting.admin.common.enums.UserErrorCodeEnum;
import org.project.ImageHosting.admin.dao.entity.UserDO;
import org.project.ImageHosting.admin.dao.mapper.UserMapper;
import org.project.ImageHosting.admin.dto.req.UserLoginReqDTO;
import org.project.ImageHosting.admin.dto.req.UserRegisterReqDTO;
import org.project.ImageHosting.admin.dto.req.UserUpdateReqDTO;
import org.project.ImageHosting.admin.dto.resp.UserLoginRespDTO;
import org.project.ImageHosting.admin.dto.resp.UserRespDTO;
import org.project.ImageHosting.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.project.ImageHosting.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static org.project.ImageHosting.admin.common.enums.UserErrorCodeEnum.*;


/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor // 构造器方式注入
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

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
        if (!lock.tryLock())
            throw new ClientException(USER_NAME_EXIST);
        try {
            // 业务逻辑: 新增用户
            int inserted = baseMapper.insert(BeanUtil.toBean(reqParam, UserDO.class)    );
            if (inserted < 1)
                throw new ClientException(USER_SAVE_ERROR);
            userRegisterCachePenetrationBloomFilter.add(reqParam.getUsername());
        } catch (DuplicateKeyException ex) {
            throw new ClientException(USER_EXIST);
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO reqParam) {
        if (!hasUsername(reqParam.getUsername()))
            throw new ClientException(USER_NULL);
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, reqParam.getUsername());
        baseMapper.update(BeanUtil.toBean(reqParam, UserDO.class), updateWrapper);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get("login_"+username, token) != null;
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO reqParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, reqParam.getUsername())
                .eq(UserDO::getPassword, reqParam.getPassword());
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null)
            throw new ClientException(USER_NULL);
        boolean hasLogin = Boolean.TRUE.equals(stringRedisTemplate.hasKey("login_" + reqParam.getUsername()));
        if (hasLogin)
            throw new ClientException(USER_LOGIN);

        // 生成token
        String uuid = UUID.randomUUID().toString();
        /*
        Hash:
            key: login_username
            value:
                key: token(uuid)
                value: JSON字符串-用户信息
         */
        stringRedisTemplate.opsForHash().put(
                "login_"+reqParam.getUsername(),
                uuid,
                JSON.toJSONString(userDO)
        );
        // 设置token过期时间
        stringRedisTemplate.expire("login_"+reqParam.getUsername(), 30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);
    }

    public Boolean logout(String username, String token) {
        if (checkLogin(username, token)) {
            stringRedisTemplate.delete("login_"+username);
            return true;
        }
        throw new ClientException(USER_NOT_LOGIN);
    }

}
