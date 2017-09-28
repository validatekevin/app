package cn.appsys.dao;

import org.apache.ibatis.annotations.Param;

import cn.appsys.entity.DevUser;

public interface DevUserDao {
	public DevUser getLoginUser(@Param("devCode")String devCode);
}
