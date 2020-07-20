package com.imooc.o2o.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "local", method = { RequestMethod.GET, RequestMethod.POST })
public class LocalAuthController {

	@Autowired
	private LocalAuthService localAuthService;

	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// check verify code
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code");
			return modelMap;
		}

		// get the input username
		String userName = HttpServletRequestUtil.getString(request, "userName");

		// get the input password
		String password = HttpServletRequestUtil.getString(request, "password");

		// from the session get current PersonInfo(if the user use Wechat login, then it
		// will be get it)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

		// null checker, username, password, and personinfo session cannot be empty
		if (userName != null && password != null && user != null && user.getUserId() != null) {
			// create LocalAuth object and set value
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(userName);
			localAuth.setPassword(password);
			localAuth.setPersonInfo(user);

			// binding the account
			LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);

			if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", le.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "userName or password is empty");
		}

		return modelMap;
	}

	/**
	 * modify password
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// check verify code
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code");
			return modelMap;
		}

		// get userName
		String userName = HttpServletRequestUtil.getString(request, "userName");

		// get old password
		String password = HttpServletRequestUtil.getString(request, "password");

		// get new password
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");

		// from the session get current PersonInfo(if the user use Wechat login, then it
		// will be get it)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

		// null checker, old/new password and user session cannot be empty,
		// also old and new password cannot be same
		if (userName != null && password != null && newPassword != null && user != null && user.getUserId() != null
				&& !password.equals(newPassword)) {
			try {
				// check the original account is same to the input account or not,
				// is not the same its a illegal action
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());

				if (localAuth == null || !localAuth.getUsername().equals(userName)) {
					// if not the same then return and exit
					modelMap.put("success", false);
					modelMap.put("errMsg", "username and password not match");
					return modelMap;
				}

				// modify localAuth user password
				LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password, newPassword);

				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter password");
		}

		return modelMap;
	}

	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logincheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// get the verify code flag, to check is it need to verify code or not
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code");
			return modelMap;
		}

		// get userName
		String userName = HttpServletRequestUtil.getString(request, "userName");

		// get old password
		String password = HttpServletRequestUtil.getString(request, "password");
		
		// null checker
		if(userName != null && password != null) {
			// input userName and password to get localAuth account info
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			
			if(localAuth != null) {
				// if success to get LocalAuth account info and it means login success
				modelMap.put("success", true);
				
				// set the user to session
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "wrong userName or password");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "userName or password is empty");
		}
		
		return modelMap;
	}
	
	/**
	 * when user click logout needs to set user session to null
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// set user session to null
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}

}
