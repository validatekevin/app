package cn.appsys.service;

import java.util.List;

import cn.appsys.entity.AppVersion;

public interface AppVersionService {
	
		public List<AppVersion> getAppVersionList(Integer appId);
		
		public boolean appsysadd(AppVersion appVersion);
		
		public AppVersion getAppVersionById(Integer id);
			
		public boolean modify(AppVersion appVersion);
				
		public boolean deleteApkFile(Integer id);
}
