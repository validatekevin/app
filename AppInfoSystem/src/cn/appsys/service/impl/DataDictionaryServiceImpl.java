package cn.appsys.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.DataDictionaryDao;
import cn.appsys.entity.DataDictionary;
import cn.appsys.service.DataDictionaryService;
@Service("dataDictionaryService")
public class DataDictionaryServiceImpl implements DataDictionaryService {
	@Resource
	private DataDictionaryDao dao;
	@Override
	public List<DataDictionary> getDataDictionaryList(String typeCode) {
		// TODO Auto-generated method stub
		return dao.getDataDictionaryList(typeCode);
	}

}
