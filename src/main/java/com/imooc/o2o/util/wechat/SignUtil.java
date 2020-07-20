package com.imooc.o2o.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Wechat request verify Util toll class
 * 
 * @author chen
 *
 */
public class SignUtil {
	// same as the token on Wechat api setting
	private static String token = "myo2o";
	
	/**
	 * use for verify signature
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] {token, timestamp, nonce};
		
		// sort token, timestamp, and nonce, in dictionary(alphabet) order
		Arrays.sort(arr);
		
		StringBuilder content = new StringBuilder();
		for(int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		
		MessageDigest md = null;
		String tmpStr = null;
		
		try {
			md = MessageDigest.getInstance("SHA-1");
			
			// concat three elements together to a string, then encrypt by use sha1
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
			
		content = null;
		// compare sha1 encode and signature, determine is the request from Wechat
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
	
	/**
	 * convert the byteString array to hex-decimal string
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		
		for(int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		
		return strDigest;
	}

	/**
	 * convert the byte to HexString
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0x0F];
		
		String s = new String(tempArr);
		return s;
	}

}
