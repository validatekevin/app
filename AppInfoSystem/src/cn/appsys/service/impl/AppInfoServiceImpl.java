package cn.appsys.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.AppInfoDao;
import cn.appsys.dao.AppVersionDao;
import cn.appsys.entity.AppInfo;
import cn.appsys.entity.AppVersion;
import cn.appsys.service.AppInfoService;
@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private AppInfoDao appInfoDao;
	@Resource
	private AppVersionDao appVersionDao;
	@Override
	public boolean add(AppInfo appInfo) {
		boolean flag = false;
		if(appInfoDao.add(appInfo) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean modify(AppInfo appInfo) {
		boolean flag = false;
		if(appInfoDao.modify(appInfo) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteAppInfoById(Integer delId) throws Exception {
		boolean flag = false;
		if(appInfoDao.deleteAppInfoById(delId) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName, Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId,
			Integer currentPageNo, Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		return appInfoDao.getAppInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId, (currentPageNo-1)*pageSize, pageSize);
	}

	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId)
			throws Exception {
		// TODO Auto-generated method stub
		return appInfoDao.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,devId);
	}

	@Override
	public AppInfo getAppInfo(Integer id, String APKName) throws Exception {
		// TODO Auto-generated method stub
		return appInfoDao.getAppInfo(id,APKName);
	}

	@Override
	public boolean deleteAppLogo(Integer id) throws Exception {
		boolean flag = false;
		if(appInfoDao.deleteAppLogo(id) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean appsysdeleteAppById(Integer id) throws Exception {
		boolean flag = false;
		int versionCount = appVersionDao.getVersionCountByAppId(id);
		List<AppVersion> appVersionList = null;
		if(versionCount > 0){
			appVersionList = appVersionDao.getAppVersionList(id);
			for(AppVersion appVersion:appVersionList){
				if(appVersion.getApkLocPath() != null && !appVersion.getApkLocPath().equals("")){
					File file = new File(appVersion.getApkLocPath());
					if(file.exists()){
						if(!file.delete())
							throw new Exception();
					}
				}
			}						
			appVersionDao.deleteVersionByAppId(id);
		}
		AppInfo appInfo = appInfoDao.getAppInfo(id, null);
		if(appInfo.getLogoLocPath() != null && !appInfo.getLogoLocPath().equals("")){
			File file = new File(appInfo.getLogoLocPath());
			if(file.exists()){
				if(!file.delete())
					throw new Exception();
			}
		}
		if(appInfoDao.deleteAppInfoById(id) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfo) throws Exception {
		Integer operator = appInfo.getModifyBy();
		if(operator < 0 || appInfo.getId() < 0 ){
			throw new Exception();
		}
		
		AppInfo app = appInfoDao.getAppInfo(appInfo.getId(), null);
		if(null == app){
			return false;
		}else{
			switch (app.getStatus()) {
				case 2: //当状态为审核通过时，可以进行上架操作
					onSale(appInfo,operator,4,2);
					break;
				case 5://当状态为下架时，可以进行上架操作
					onSale(appInfo,operator,4,2);
					break;
				case 4://当状态为上架时，可以进行下架操作
					offSale(appInfo,operator,5);
					break;

			default:
				return false;
			}
		}
		return true;
	}
	private void onSale(AppInfo appInfo,Integer operator,Integer appInfStatus,Integer versionStatus) throws Exception{
		offSale(appInfo,operator,appInfStatus);
		setSaleSwitchToAppVersion(appInfo,operator,versionStatus);
	}
	
	
	private boolean offSale(AppInfo appInfo,Integer operator,Integer appInfStatus) throws Exception{
		AppInfo _appInfo = new AppInfo();
		_appInfo.setId(appInfo.getId());
		_appInfo.setStatus(appInfStatus);
		_appInfo.setModifyBy(operator);
		_appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
		appInfoDao.modify(_appInfo);
		return true;
	}
	
	/**
	 * set sale method to on or off
	 * @param appInfo
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	private boolean setSaleSwitchToAppVersion(AppInfo appInfo,Integer operator,Integer saleStatus) throws Exception{
		AppVersion appVersion = new AppVersion();
		appVersion.setId(appInfo.getVersionId());
		appVersion.setPublishStatus(saleStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date(System.currentTimeMillis()));
		appVersionDao.modify(appVersion);
		return false;
	}
}
