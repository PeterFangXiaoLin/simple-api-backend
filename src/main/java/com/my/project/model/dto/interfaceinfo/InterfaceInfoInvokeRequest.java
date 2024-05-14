package com.my.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    private Long id;

    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}
