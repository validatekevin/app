package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.appsys.entity.DevUser;
import cn.appsys.service.DevUserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/dev")
public class DevLoginController {
		@Resource
		private DevUserService devUserService;
		@RequestMapping("/login")
		public String login(){
			return "devlogin";
		}
		@RequestMapping(value="/dologin",method=RequestMethod.POST)
		public String doLogin(@RequestParam String devCode,@RequestParam String devPassword,HttpServletRequest request,HttpSession session){
			DevUser user = null;
			try {
				user = devUserService.login(devCode, devPassword);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=user){
				System.out.println("dddddddddddddd");
				session.setAttribute(Constants.DEV_USER_SESSION, user);
				System.out.println(session.getAttribute(Constants.DEV_USER_SESSION));
				return "redirect:/dev/flatform/main";
			}else{
				request.setAttribute("error", "用户名或密码不正确");
				return "devlogin";
			}			
		}
		@RequestMapping("/flatform/main")
		public String main(HttpSession session){
			if(session.getAttribute(Constants.DEV_USER_SESSION)==null){
				return "redirect:/dev/login";
			}			
			return "developer/main";
		}
		@RequestMapping("/logout")
		public String logout(HttpSession session){
			session.removeAttribute(Constants.DEV_USER_SESSION);
			return "devlogin";
		}
}
