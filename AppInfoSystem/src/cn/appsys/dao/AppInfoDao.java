package cn.appsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.entity.AppInfo;

public interface AppInfoDao {
		public int add(AppInfo appInfo);
		public int modify(AppInfo appInfo);
		public int deleteAppInfoById(@Param(value="id")Integer delId);		
		public List<AppInfo> getAppInfoList(@Param(value="softwareName")String querySoftwareName,
											@Param(value="status")Integer queryStatus,
											@Param(value="categoryLevel1")Integer queryCategoryLevel1,
											@Param(value="categoryLevel2")Integer queryCategoryLevel2,
											@Param(value="categoryLevel3")Integer queryCategoryLevel3,
											@Param(value="flatformId")Integer queryFlatformId,
											@Param(value="devId")Integer devId,
											@Param(value="from")Integer currentPageNo,
											@Param(value="pageSize")Integer pageSize);
		public int getAppInfoCount(@Param(value="softwareName")String querySoftwareName,
				   @Param(value="status")Integer queryStatus,
				   @Param(value="categoryLevel1")Integer queryCategoryLevel1,
				   @Param(value="categoryLevel2")Integer queryCategoryLevel2,
				   @Param(value="categoryLevel3")Integer queryCategoryLevel3,
				   @Param(value="flatformId")Integer queryFlatformId,
				   @Param(value="devId")Integer devId);

		public AppInfo getAppInfo(@Param(value="id")Integer id,@Param(value="APKName")String APKName);

		public int deleteAppLogo(@Param(value="id")Integer id);
		public int updateVersionId(@Param(value="versionId")Integer versionId,@Param(value="id")Integer appId);
		public int updateSaleStatusByAppId(@Param(value="id")Integer appId);
		public int updateSatus(@Param(value="status")Integer status,@Param(value="id")Integer id);
}
