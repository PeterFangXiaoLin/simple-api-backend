package com.my.project.service.impl.inner;

import com.my.project.service.InnerUserInterfaceInfoService;
import com.my.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 注入 service
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
