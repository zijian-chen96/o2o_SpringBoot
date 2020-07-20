package com.imooc.o2o.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class CodeUtil {
	
	/**
	 * check the verify code correct or not
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkVerifyCode(HttpServletRequest request) {
		String verifyCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
	
	/**
	 * generate QR code stream
	 * 
	 * @param content
	 * @param response
	 * @return
	 */
	public static BitMatrix generateQRCodeStream(String content, HttpServletResponse response) {
		// add header info for response, to tell browser the return data is image stream
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		
		// set image text code and border
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix bitMatrix;
		
		try {
			// variables: codeContent, char-set, image-width, image-height, settings
			bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);			
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
		return bitMatrix;
	}
	
}
