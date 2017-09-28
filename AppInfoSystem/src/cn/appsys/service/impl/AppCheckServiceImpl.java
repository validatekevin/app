package cn.appsys.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.appsys.dao.AppInfoDao;
import cn.appsys.entity.AppInfo;
import cn.appsys.service.AppCheckService;
@Service("appCheckService")
public class AppCheckServiceImpl implements AppCheckService {
	@Resource
	private AppInfoDao appDao;
	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer currentPageNo,
			Integer pageSize) {
		// TODO Auto-generated method stub
		return appDao.getAppInfoList(querySoftwareName, 1, queryCategoryLevel1, queryCategoryLevel2, 
                queryCategoryLevel3, queryFlatformId, null, (currentPageNo-1)*pageSize, pageSize);
	}

	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId) {
		// TODO Auto-generated method stub
		return appDao.getAppInfoCount(querySoftwareName, 1, queryCategoryLevel1, queryCategoryLevel2, 
				queryCategoryLevel3, queryFlatformId, null);
	}

	@Override
	public AppInfo getAppInfo(Integer id) {
		// TODO Auto-generated method stub
		return appDao.getAppInfo(id, null);
	}

	@Override
	public boolean updateSatus(Integer status, Integer id) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(appDao.updateSatus(status, id) > 0 ){
			flag = true;
		}
		return flag;
	}

}
