package com.sfaw.springsecurityknife.controller;

import com.sfaw.springsecurityknife.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * LoginController
 * @author wy
 * @version 1.0
 * @date 2023/5/16 10:28
 **/
@Controller
public class LoginController {

	@Autowired
	private JwtService jwtService;

	//	@RequestMapping("/loginpage")
	//	public String login() {
	//		return "loginpage.html";
	//	}

	/**
	 * 这个接口不会用到
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("/tologin")
	public String login(HttpServletRequest request,
			@RequestParam("username") String username,
			@RequestParam("password") String password
	) {
		System.out.println(username + ": " + password);

		// todo 这里可以生成token 返回给前端，然后接口都带上token
		String token = jwtService.generateToken(username);
		// 后端也可以放到session里，然后重定向后请求也带上session 即可
		request.getSession().setAttribute("token", token);
		return "redirect:userinfo.html";
		//return "redirect:doc.html";
	}
}
