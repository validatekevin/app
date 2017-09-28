package cn.appsys.service;

import java.util.List;

import cn.appsys.entity.AppCategory;

public interface AppCategoryService {
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId);
}
