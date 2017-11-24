package com.guqiankun.ssmtemplate.model.vo.AppSetting;

import org.hibernate.validator.constraints.NotEmpty;

@lombok.Data
public class ListRequest {

    @NotEmpty
    private String id;

}