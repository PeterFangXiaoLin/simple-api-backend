package com.my.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.project.annotation.AuthCheck;
import com.my.project.common.BaseResponse;
import com.my.project.common.ErrorCode;
import com.my.project.common.ResultUtils;
import com.my.project.constant.UserConstant;
import com.my.project.exception.BusinessException;
import com.my.project.mapper.UserInterfaceInfoMapper;
import com.my.project.model.entity.InterfaceInfo;
import com.my.project.model.entity.UserInterfaceInfo;
import com.my.project.model.vo.InterfaceInfoVO;
import com.my.project.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析工具接口
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        // 根据接口id分组
        Map<Long, List<UserInterfaceInfo>> collect = userInterfaceInfoList.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        // 把这些id用 in 查
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", collect.keySet());
        List<InterfaceInfo> listInterfaceInfo = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(listInterfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 转成vo
        List<InterfaceInfoVO> interfaceInfoVOList = listInterfaceInfo.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            long totalNum = collect.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceInfoVOList);
    }
}
