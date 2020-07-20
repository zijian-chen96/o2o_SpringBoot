package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.ProductCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceTest {
	@Autowired
	private ProductCategoryService productCategoryService; 
	
	@Test
	public void testProductCategory() {
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList = productCategoryService.getProductCategoryList(1L);
		assertEquals(3, productCategoryList.size());
		System.out.println(productCategoryList.get(0).getProductCategoryName());
	}
	
	
}
