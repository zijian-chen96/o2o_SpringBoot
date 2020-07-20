package com.imooc.o2o.service;

import java.io.IOException;
import java.util.List;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineService {
	
	public static final String HLLISTKEY = "headlinelist";

	/**
	 * it will return the headLine by input info
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	List<HeadLine> getheadLineList(HeadLine headLineCondition) throws IOException;
}
