package cn.appsys.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.BackEndUserDao;
import cn.appsys.entity.BackendUser;
import cn.appsys.service.BackEndUserService;
@Service("backEndUserService")
public class BackEndUserServiceImpl implements BackEndUserService {
	@Resource
	private BackEndUserDao backendUserDao;
	@Override
	public BackendUser login(String userCode, String userPassword) {
		// TODO Auto-generated method stub
		return backendUserDao.getLoginUser(userCode);
	}

}
