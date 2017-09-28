package cn.appsys.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.AppCategoryDao;
import cn.appsys.entity.AppCategory;
import cn.appsys.service.AppCategoryService;
@Service("appCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService {
	@Resource
	private AppCategoryDao dao;
	@Override
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return dao.getAppCategoryListByParentId(parentId);
	}

}
