package com.my.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.project.common.ErrorCode;
import com.my.project.exception.BusinessException;
import com.my.project.mapper.InterfaceInfoMapper;
import com.my.project.model.entity.InterfaceInfo;
import com.my.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author helloworld
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-04-13 16:47:58
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        // 创建时，接口名称、接口地址 不能为空
        if (add) {
            if (StringUtils.isBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称为空");
            }
            if (StringUtils.isBlank(url)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口地址为空");
            }
        }

        // 接口名称 不能太长
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }

        // 请求类型 只能是 get or post
        if (StringUtils.isNotBlank(method) && !"GET".equals(method) && !"POST".equals(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求类型非法");
        }

        // 接口描述 不能太长
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述过长");
        }

        // 其它校验待定 todo
    }
}




