package cn.appsys.service;

import cn.appsys.entity.BackendUser;

public interface BackEndUserService {
	public BackendUser login(String userCode,String userPassword);
}
