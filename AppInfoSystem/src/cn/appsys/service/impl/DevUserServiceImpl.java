package cn.appsys.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.DevUserDao;
import cn.appsys.entity.DevUser;
import cn.appsys.service.DevUserService;
@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {
	@Resource
	private DevUserDao devUserDao;
	@Override
	public DevUser login(String devCode, String devPassword) {
		// TODO Auto-generated method stub
		DevUser user = null;
		user =devUserDao.getLoginUser(devCode);
		if(null != user){
			if(!user.getDevPassword().equals(devPassword))
				user = null;
		}
		return user;
	}

}
