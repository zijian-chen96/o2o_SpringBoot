package com.imooc.o2o.util;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * DES encryption
 * 
 * @author chen
 *
 */
public class DESUtil {
	
	private static Key key;
	
	// set the encrypt key
	private static String KEY_STR = "myKey";
	private static String CHARSETNAME = "UTF-8";
	private static String ALGORITHM = "DES";
	
	static {
		try {
			// create DES algorithm object
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			
			// use SHA1 security
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			
			// setup encrypt key seed
			secureRandom.setSeed(KEY_STR.getBytes());
			
			// initial the SHA1 algorithm object
			generator.init(secureRandom);
			
			// create encrypt key object
			key = generator.generateKey();
			generator = null;
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getEncryptString(String str) {
		// base on BASE64 code, accept byte[] then convert to String
		//BASE64Encoder base64encoder = new BASE64Encoder();
		
		try {
			// use utf8 code
			byte[] bytes = str.getBytes(CHARSETNAME);
			
			// get the encrypted object
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			
			// initial password information 
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			// encrypting
			byte[] doFinal = cipher.doFinal(bytes);
			
			// byte[] to encode String and return
			return Base64.getEncoder().encodeToString(doFinal);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * get decode data
	 * 
	 * @param str
	 * @return
	 */
	public static String getDecryptString(String str) {
		
		try {
			// decode the string to byte[]
			byte[] bytes = Base64.getDecoder().decode(str);
			
			//get decode object
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			
			// initial decrypt info
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			// decrypting
			byte[] doFinal = cipher.doFinal(bytes);
			
			// return the decoded data
			return new String(doFinal, CHARSETNAME);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getEncryptString("root"));
		System.out.println(getEncryptString("rootpassword"));
		System.out.println(getDecryptString("WnplV/ietfQ="));
		System.out.println(getDecryptString("Fwz3TuaSsrOolrOKY3mHZw=="));
	}
	

}
