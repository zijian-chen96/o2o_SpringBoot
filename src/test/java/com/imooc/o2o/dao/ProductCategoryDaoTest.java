package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.ProductCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testCDeleteProductCategory() throws Exception {
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategory(shopId);
		for(ProductCategory pc : productCategoryList) {
			if("product test 5".equals(pc.getProductCategoryName()) || "product test 6".equals(pc.getProductCategoryName())) {
				int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1, effectedNum);
			}
		}
	}
	
	@Test
	@Ignore
	public void testABatchInsertProductCategory() {
		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("product test 5");
		productCategory1.setPriority(1);
		productCategory1.setCreateTime(new Date());
		productCategory1.setShopId(1L);
		
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("product test 6");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory1);
		productCategoryList.add(productCategory2);
		int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectedNum);
	}
	
	@Test
	@Ignore
	public void testBQueryProductCategory() {	
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategory(1L);
		assertEquals(3, productCategoryList.size());
		System.out.println(productCategoryList.get(0).getProductCategoryName());
		
	}
}
