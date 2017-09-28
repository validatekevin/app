package cn.appsys.dao;

import org.apache.ibatis.annotations.Param;

import cn.appsys.entity.BackendUser;

public interface BackEndUserDao {
	public BackendUser getLoginUser(@Param("userCode")String userCode);
}
