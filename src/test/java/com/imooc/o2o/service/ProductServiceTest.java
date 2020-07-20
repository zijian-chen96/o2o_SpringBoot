package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
	
	@Autowired
	private ProductService productService;
	
	@Test
	@Ignore
	public void testAddProduct() throws ShopOperationException, FileNotFoundException {
		// create shopId 1 and productCategoryId 1
		Shop shop = new Shop();
		shop.setShopId(1L);
		
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		
		Product product = new Product();
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("test product 1");
		product.setProductDesc("test product 1");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		
		// create view images
		File thumbnailFile = new File("/Users/chen/projectimage/pinkpig.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		
		// create two product detail image, and add into db
		File productImg1 = new File("/Users/chen/projectimage/pinkpig.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("/Users/chen/projectimage/murshroom.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
				
		// add product
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());	
	}
	
	@Test
	public void testModifyProduct() throws ShopOperationException, FileNotFoundException {
		// create shopId 1 and productCategoryId 1
		Product product = new Product();
		Shop shop = new Shop();
		ProductCategory pc = new ProductCategory();
		
		shop.setShopId(1L);
		pc.setProductCategoryId(1L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("good product");
		product.setProductDesc("good product");
		
		// create view image file
		File thumbnailFile = new File("/Users/chen/projectimage/murshroom.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		
		// create two detail image file
		File productImg1 = new File("/Users/chen/projectimage/pinkpig.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("/Users/chen/projectimage/murshroom.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		
		// modify the product and test
		ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
	
}
