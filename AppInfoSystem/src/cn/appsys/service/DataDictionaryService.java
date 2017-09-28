package cn.appsys.service;

import java.util.List;

import cn.appsys.entity.DataDictionary;

public interface DataDictionaryService {
	public List<DataDictionary> getDataDictionaryList(String typeCode);
}
