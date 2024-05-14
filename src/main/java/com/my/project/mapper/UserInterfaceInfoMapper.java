package com.my.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.project.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author helloworld
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Mapper
* @createDate 2024-05-10 10:45:37
* @Entity com.my.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    // select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info
    // group by interfaceInfoId order by totalNum desc limit 3;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




