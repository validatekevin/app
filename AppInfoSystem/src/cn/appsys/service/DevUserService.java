package cn.appsys.service;

import cn.appsys.entity.DevUser;

public interface DevUserService {
	public DevUser login(String devCode,String devPassword);
}
