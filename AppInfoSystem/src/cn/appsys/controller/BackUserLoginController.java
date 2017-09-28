package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.entity.BackendUser;
import cn.appsys.service.BackEndUserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/manager")
public class BackUserLoginController {
		@Resource
		private BackEndUserService backendUserService;

		@RequestMapping(value="/login")
		public String login(){			
			return "backendlogin";
		}		
		@RequestMapping(value="/dologin",method=RequestMethod.POST)
		public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,HttpServletRequest request,HttpSession session){			
			
			BackendUser user = null;
			try {
				user = backendUserService.login(userCode,userPassword);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != user){//��¼�ɹ�
				session.setAttribute(Constants.USER_SESSION, user);
				//ҳ����ת��main.jsp��
				return "redirect:/manager/backend/main";
			}else{
				//ҳ����ת��login.jsp��������ʾ��Ϣ--ת��
				request.setAttribute("error", "�û��������벻��ȷ");
				return "backendlogin";
			}
		}
		
		@RequestMapping(value="/backend/main")
		public String main(HttpSession session){
			if(session.getAttribute(Constants.USER_SESSION) == null){
				return "redirect:/manager/login";
			}
			return "backend/main";
		}
		
		@RequestMapping(value="/logout")
		public String logout(HttpSession session){
			session.removeAttribute(Constants.USER_SESSION);
			return "backendlogin";
		}
}
