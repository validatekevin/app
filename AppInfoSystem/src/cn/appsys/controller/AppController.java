package cn.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.entity.AppCategory;
import cn.appsys.entity.AppInfo;
import cn.appsys.entity.AppVersion;
import cn.appsys.entity.DataDictionary;
import cn.appsys.entity.DevUser;
import cn.appsys.service.AppCategoryService;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppVersionService;
import cn.appsys.service.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppController {
	@Resource
	private AppInfoService appInfoService;
	@Resource 
	private DataDictionaryService dataDictionaryService;
	@Resource 
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;
	@RequestMapping(value="/list")
	public String getAppInfoList(Model model,HttpSession session,
							@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
							@RequestParam(value="queryStatus",required=false) String _queryStatus,
							@RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
							@RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
							@RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
							@RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
							@RequestParam(value="pageIndex",required=false) String pageIndex){
		
		
		Integer devId = ((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
		List<AppInfo> appInfoList = null;
		List<DataDictionary> statusList = null;
		List<DataDictionary> flatFormList = null;
		List<AppCategory> categoryLevel1List = null;//�г�һ�������б�ע�����������������б�ͨ���첽ajax��ȡ
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		//ҳ������
		int pageSize = Constants.pageSize;
		//��ǰҳ��
		Integer currentPageNo = 1;
		
		if(pageIndex != null){
			try{
				currentPageNo = Integer.valueOf(pageIndex);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		Integer queryStatus = null;
		if(_queryStatus != null && !_queryStatus.equals("")){
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if(_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")){
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if(_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")){
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if(_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")){
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if(_queryFlatformId != null && !_queryFlatformId.equals("")){
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		
		//����������
		int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//��ҳ��
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		//������ҳ��βҳ
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		try {
			appInfoList = appInfoService.getAppInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId, currentPageNo, pageSize);
			statusList = this.getDataDictionaryList("APP_STATUS");
			flatFormList = this.getDataDictionaryList("APP_FLATFORM");
			categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages", pages);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);
		
		//���������б�����������б�---����
		if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
			categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")){
			categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		return "developer/appinfolist";
	}
	/**
	 * �����ֵ��б�
	 * getDataDictionaryList
	 * */
	public List<DataDictionary> getDataDictionaryList(String typeCode){
		List<DataDictionary> dataDictionaryList = null;
		try {
			dataDictionaryList = dataDictionaryService.getDataDictionaryList(typeCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDictionaryList;
	}
	@RequestMapping(value="/datadictionarylist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<DataDictionary> getDataDicList (@RequestParam String tcode){
		return this.getDataDictionaryList(tcode);
	}
	/**
	 * �����б�
	 * getCategoryList
	 * */
	public List<AppCategory> getCategoryList (String pid){
		List<AppCategory> categoryLevelList = null;
		try {
			categoryLevelList = appCategoryService.getAppCategoryListByParentId(pid==null?null:Integer.parseInt(pid));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryLevelList;
	}
	/**
	 * �����༶���г��б�ó�App�б�
	 * getAppCategoryList
	 * */
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<AppCategory> getAppCategoryList (@RequestParam String pid){
		if(pid.equals("")) pid = null;
		return getCategoryList(pid);
	}
	//���app��Ϣ
	@RequestMapping(value="appinfoadd",method=RequestMethod.GET)
	public String add(@ModelAttribute("appInfo")AppInfo appinfo){
		return "developer/appinfoadd";
	}
	/**	  
	 * @param appInfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="appinfoaddsave",method=RequestMethod.POST)
	public String addSave(AppInfo appInfo,HttpSession session,HttpServletRequest request,@RequestParam(value="a_logoPicPath",required= false) MultipartFile attach){
		String logoPicPath =  null;
		String logoLocPath =  null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();//ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName);//ԭ�ļ���׺
			int filesize = 500000;
			if(attach.getSize() > filesize){//�ϴ���С���ó��� 50k
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
				return "developer/appinfoadd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){
				 String fileName = appInfo.getAPKName() + ".jpg";
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
					return "developer/appinfoadd";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setStatus(1);
		try {
			if(appInfoService.add(appInfo)){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfoadd";		
	}
	//���app�汾��Ϣ
	@RequestMapping(value="appversionadd",method=RequestMethod.GET)
	public String addVersion(@RequestParam(value="id")String appId,
			 @RequestParam(value="error",required= false)String fileUploadError,
			 AppVersion appVersion,Model model){
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}
		appVersion.setAppId(Integer.parseInt(appId));
		List<AppVersion> appVersionList = null;
		try {
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
			appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId),null)).getSoftwareName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appVersion);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appversionadd";
	}
	/**
	 * ��Ӱ汾��Ϣ������
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="appversionsave",method=RequestMethod.POST)
	public String addVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
			@RequestParam(value="a_downloadLink",required= false) MultipartFile attach ){
		String downloadLink =  null;
		String apkLocPath = null;
		String apkFileName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();//ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName);//ԭ�ļ���׺
			if(prefix.equalsIgnoreCase("apk")){
				 String apkName = null;
				 try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
				 } catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				 }
				 if(apkName == null || "".equals(apkName)){
					 return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
							 +"&error=error1";
				 }
				 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
				 File targetFile = new File(path,apkFileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
							 +"&error=error2";
				} 
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else{
				return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
						 +"&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if(appVersionService.appsysadd(appVersion)){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();		
	}
	//������
	@RequestMapping(value="/{appid}/save",method=RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String appid,HttpSession session){
		HashMap<String,Object> map = new HashMap<String,Object>();
		Integer appId = 0;
		try {
			appId = Integer.parseInt(appid);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			appId=0;
		}
		map.put("errorCode","0");
		map.put("appId",appid);
		if(appId>0){
			try {
				DevUser devUser =(DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
				AppInfo appInfo = new AppInfo();
				appInfo.setId(appId);
				appInfo.setModifyBy(devUser.getId());
				if(appInfoService.appsysUpdateSaleStatusByAppId(appInfo)){
					map.put("resultMsg", "success");
				}else{
					map.put("resultMsg", "success");
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
				//errorCode:0Ϊ����
				map.put("errorCode", "param000001");
			}
		return map;
	}
	/**
	 * �鿴Ӧ���Ƿ����
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkNameIsExit(@RequestParam String APKName){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(APKName)){
			resultMap.put("APKName", "empty");
		}else{
			AppInfo appInfo = null;
			try {
				appInfo = appInfoService.getAppInfo(null, APKName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != appInfo)
				resultMap.put("APKName", "exist");
			else
				resultMap.put("APKName", "noexist");
		}
		return JSONArray.toJSONString(resultMap);
	}
	//��ʾ��ͼ
	@RequestMapping(value="/appview/{id}",method=RequestMethod.GET)
	public String view(@PathVariable String id,Model model){
		AppInfo appInfo = null;
		List<AppVersion> appList =null;
		 try {
			appInfo = appInfoService.getAppInfo(Integer.parseInt(id),null);
			 appList = appVersionService.getAppVersionList(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 model.addAttribute("appList",appList);
		 model.addAttribute(appInfo);
		
		return "developer/appinfoview";
	}
	//�޸�app��Ϣ
	@RequestMapping(value="/appinfomodify",method=RequestMethod.GET)
	public String modifyAppInfo(@RequestParam("id") String id,@RequestParam(value="error",required= false)String fileUploadError,Model model ){
		AppInfo appInfo = null;
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}else if(null != fileUploadError && fileUploadError.equals("error4")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_4;
		}
		try {
			appInfo = appInfoService.getAppInfo(Integer.parseInt(id),null);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(appInfo);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appinfomodify";
	}
	/**
	 * �޸Ĳ�����
	 * @param appInfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
	public String modifySave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
							@RequestParam(value="attach",required= false) MultipartFile attach){		
		String logoPicPath =  null;
		String logoLocPath =  null;
		String APKName = appInfo.getAPKName();
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");			
			String oldFileName = attach.getOriginalFilename();//ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName);//ԭ�ļ���׺
			int filesize = 500000;
			if(attach.getSize() > filesize){//�ϴ���С���ó��� 50k
            	 return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
						 +"&error=error4";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//�ϴ�ͼƬ��ʽ
				 String fileName = APKName + ".jpg";//�ϴ�LOGOͼƬ����:apk����.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
							+"&error=error2";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
            }else{
            	return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
						 +"&error=error3";
            }
		}
		appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		try {
			if(appInfoService.modify(appInfo)){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}
	/**
	 * �޸�app�汾
	 * @param versionId
	 * @param appId
	 * @param fileUploadError
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appversionmodify",method=RequestMethod.GET)
	public String modifyAppVersion(@RequestParam("vid") String versionId,
			@RequestParam("aid") String appId,
			@RequestParam(value="error",required= false)String fileUploadError,
			Model model){
		AppVersion appVersion = null;
		List<AppVersion> appVersionList = null;
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}
		try {
			appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(appVersion);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appversionmodify";
	}
	/**
	 * �޸İ汾������
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
	public String modifyAppVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
			@RequestParam(value="attach",required= false) MultipartFile attach){
		String downloadLink =  null;
		String apkLocPath = null;
		String apkFileName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(oldFileName);
			if(prefix.equalsIgnoreCase("apk")){
				 String apkName = null;
				 try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
				 } catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				 }
				 if(apkName == null || "".equals(apkName)){
					 return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
							 +"&aid="+appVersion.getAppId()
							 +"&error=error1";
				 }
				 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
				 File targetFile = new File(path,apkFileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
							 +"&aid="+appVersion.getAppId()
							 +"&error=error2";
				} 
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else{
				return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
						 +"&aid="+appVersion.getAppId()
						 +"&error=error3";
			}
		}
		appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if(appVersionService.modify(appVersion)){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appversionmodify";
	}
	/**
	 * ɾ���ļ�
	 * @param flag
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delfile",method=RequestMethod.GET)
	@ResponseBody
	public Object delFile(@RequestParam(value="flag",required=false) String flag,
						 @RequestParam(value="id",required=false) String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String fileLocPath = null;
		if(flag == null || flag.equals("") ||
			id == null || id.equals("")){
			resultMap.put("result", "failed");
		}else if(flag.equals("logo")){
			try {
				fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
				File file = new File(fileLocPath);
			    if(file.exists())
			     if(file.delete()){
						if(appInfoService.deleteAppLogo(Integer.parseInt(id))){//���±�
							resultMap.put("result", "success");
						 }
			    }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(flag.equals("apk")){
			try {
				fileLocPath = (appVersionService.getAppVersionById(Integer.parseInt(id))).getApkLocPath();
				File file = new File(fileLocPath);
			    if(file.exists())
			     if(file.delete()){
						if(appVersionService.deleteApkFile(Integer.parseInt(id))){//���±�
							resultMap.put("result", "success");
						 }
			    }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	/**
	 * ����idɾ��app
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delapp.json")
	@ResponseBody
	public Object delApp(@RequestParam String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("delResult", "notexist");
		}else{
			try {
				if(appInfoService.appsysdeleteAppById(Integer.parseInt(id)))
					resultMap.put("delResult", "true");
				else
					resultMap.put("delResult", "false");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
}
