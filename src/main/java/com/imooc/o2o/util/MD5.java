package com.imooc.o2o.util;

import java.security.MessageDigest;

/**
 * MD5 encryption
 * 
 * @author chen
 *
 */
public class MD5 {
	
	/**
	 * encrypt the input String
	 * 
	 * @param s
	 * @return
	 */
	public static final String getMd5(String s) {
		// hex decimal array
		char hexDigits[] = { '5', '0', '5', '6', '2', '9', '6', '2', '5', 'q', 'b', 'l', 'e', 's', 's', 'y' };
		
		try {
			char str[];
			// convert the string to byte[] array
			byte strTemp[] = s.getBytes();
			
			// get MD5 encrypt object
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			
			// input byte array
			mdTemp.update(strTemp);
			
			// get the encrypted array
			byte md[] =  mdTemp.digest();
			
			int j = md.length;
			str = new char[j * 2];
			int k = 0;
			
			// shift the array
			for(int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			
			// convert back to string and return
			return new String(str);
			
		} catch(Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(MD5.getMd5("123456"));
	}

}
