package cn.appsys.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.AppInfoDao;
import cn.appsys.dao.AppVersionDao;
import cn.appsys.entity.AppVersion;
import cn.appsys.service.AppVersionService;
@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {
	@Resource
	private AppInfoDao appInfoDao;
	@Resource
	private AppVersionDao appVersionDao;
	@Override
	public List<AppVersion> getAppVersionList(Integer appId) {
		// TODO Auto-generated method stub
		return appVersionDao.getAppVersionList(appId);
	}

	@Override
	public boolean appsysadd(AppVersion appVersion) {
		// TODO Auto-generated method stub
		boolean flag = false;
		Integer versionId = null;
		if(appVersionDao.add(appVersion) > 0){
			versionId = appVersion.getId();
			flag = true;
		}
		if(appInfoDao.updateVersionId(versionId, appVersion.getAppId()) > 0 && flag){
			flag = true;
		}
		return flag;
	}

	@Override
	public AppVersion getAppVersionById(Integer id) {
		// TODO Auto-generated method stub
		return appVersionDao.getAppVersionById(id);
	}

	@Override
	public boolean modify(AppVersion appVersion) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(appVersionDao.modify(appVersion) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteApkFile(Integer id) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(appVersionDao.deleteApkFile(id) > 0){
			flag = true;
		}
		return flag;
	}

}
