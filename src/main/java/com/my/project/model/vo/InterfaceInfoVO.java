package com.my.project.model.vo;

import com.my.project.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户视图
 *
 * @TableName user
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {
    /**
     * 总调用次数
     */
    private Long totalNum;

    private static final long serialVersionUID = 1L;
}