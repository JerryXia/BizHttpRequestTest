package com.guqiankun.ssmtemplate.mapper;

import com.guqiankun.ssmtemplate.common.SysCommonMapper;
import com.guqiankun.ssmtemplate.model.po.AppSetting;
import java.util.List;

public interface AppSettingMapper extends SysCommonMapper {
    int insert(AppSetting record);

    List<AppSetting> selectAll();
}