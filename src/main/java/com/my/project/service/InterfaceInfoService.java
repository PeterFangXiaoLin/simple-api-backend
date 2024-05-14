package com.my.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.project.model.entity.InterfaceInfo;

/**
* @author helloworld
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-04-13 16:47:58
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add 是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
