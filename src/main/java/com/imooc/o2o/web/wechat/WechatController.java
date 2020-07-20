package com.imooc.o2o.web.wechat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.o2o.util.wechat.SignUtil;

@Controller
// set this routing in URL setting
@RequestMapping("wechat")
public class WechatController {
	
	private static Logger log = LoggerFactory.getLogger(WechatController.class);
	
	@RequestMapping(method = {RequestMethod.GET})
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat get...");
		
		// wechat encrypt signature, signature include developer's token info, and request timestamp, and nonce.
		String signature = request.getParameter("signature");
		
		// tiemstamp
		String timestamp = request.getParameter("timestamp");
		
		// random number
		String nonce = request.getParameter("nonce");
		
		// random String
		String echostr = request.getParameter("echostr");
		
		// use to signature to do verify, if success will return echostr and it means success and accept, otherwise failed
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			
			if(SignUtil.checkSignature(signature, timestamp, nonce)) {
				log.debug("wechat get success...");
				out.print(echostr);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null)
				out.close();
		}
		
	}
	
}
