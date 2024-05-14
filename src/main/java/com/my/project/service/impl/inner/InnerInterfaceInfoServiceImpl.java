package com.my.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.project.common.ErrorCode;
import com.my.project.exception.BusinessException;
import com.my.project.mapper.InterfaceInfoMapper;
import com.my.project.model.entity.InterfaceInfo;
import com.my.project.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);

        // 这里还可以再校验请求参数
        // 向数据库查，由于没有实现mybatis-plus 所以引入mapper
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
