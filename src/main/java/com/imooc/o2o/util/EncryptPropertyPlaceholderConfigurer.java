package com.imooc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	// need to encrypt string
	private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
	
	/**
	 * convert the propertyValue 
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(isEncryptProp(propertyName)) {
			// decode the encrypted data
			String decryptValue = DESUtil.getDecryptString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}
	
	/**
	 * chack is the data already encrypted
	 * 
	 * @param propertyName
	 * @return
	 */
	private boolean isEncryptProp(String propertyName) {
		// if equal to needs encrypt field, then it is encrypted
		for(String encryptpropertyName : encryptPropNames) {
			if(encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
	
}
