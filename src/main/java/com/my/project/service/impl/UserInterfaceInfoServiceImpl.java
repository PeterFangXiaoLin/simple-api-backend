package com.my.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.project.common.ErrorCode;
import com.my.project.exception.BusinessException;
import com.my.project.mapper.UserInterfaceInfoMapper;
import com.my.project.model.entity.UserInterfaceInfo;
import com.my.project.service.UserInterfaceInfoService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
* @author helloworld
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service实现
* @createDate 2024-05-10 10:45:37
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private RedissonClient redissonClient;

    private static final String lock = "simple:api:invoke:%s:%s";

    @Override
    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，用户id、接口id 不能为空
        if (add) {
            // 用户id
            if (userInterfaceInfo.getUserId() == null || userInterfaceInfo.getUserId() <= 0L) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
            }

            // 接口id
            if (userInterfaceInfo.getInterfaceInfoId() == null || userInterfaceInfo.getInterfaceInfoId() <= 0L) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在");
            }
        }

        // 剩余调用次数 >= 0
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数不合法");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("totalNum = totalNum + 1, leftNum = leftNum - 1");

        // 使用 redisson 分布式锁，防止数据库出现问题
        RLock rLock = redissonClient.getLock(String.format(lock, userId, interfaceInfoId));
        try {
            if (rLock.tryLock(10L, TimeUnit.SECONDS)) {
                return update(updateWrapper);
            }
        } catch (InterruptedException e) {
            log.error("try lock error", e);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }

        return false;
    }
}




