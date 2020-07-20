package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {
	
	/**
	 * bind account routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/accountbind", method = RequestMethod.GET)
	private String accountbind() {
		return "local/accountbind";
	}
	
	/**
	 * change password routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/changepsw", method = RequestMethod.GET)
	private String changepsw() {
		return "local/changepsw";
	}
	
	/**
	 * login page routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		return "local/login";
	}
	
}
