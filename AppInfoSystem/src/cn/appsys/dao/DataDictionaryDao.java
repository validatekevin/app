package cn.appsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.entity.DataDictionary;

public interface DataDictionaryDao {
	public List<DataDictionary> getDataDictionaryList(@Param("typeCode")String typeCode);
}
