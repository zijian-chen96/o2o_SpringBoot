package com.imooc.o2o.dto;
/**
 * store JSON object, return all result.
 * 
 * @author chen
 *
 */
public class Result<T> {
	
	// determine true or false
	private boolean success; 
	
	// success is true will return data
	private T data;
	
	// error messages
	private String errorMsg;
	
	private int errorCode;
	
	public Result() {
		
	}
	
	// success is true  
	public Result(boolean success, T data) {
		this.success = success;
		this.data = data;
	}
	
	// success is false
	public Result(boolean success, int errorCode, String errorMsg) {
		this.success = success;
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
