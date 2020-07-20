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

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testAInsertProduct() throws Exception {
		Shop shop1 = new Shop();
		shop1.setShopId(1L);
		ProductCategory pc1 = new ProductCategory();
		pc1.setProductCategoryId(1L);
		
		// initial three products and insert into shopId 1
		// product categoryId 1
		Product product1 = new Product();
		product1.setProductName("test 1");
		product1.setProductDesc("test Desc 1");
		product1.setImgAddr("test 1");
		product1.setPriority(1);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setShop(shop1);
		product1.setProductCategory(pc1);
		
		Product product2 = new Product();
		product2.setProductName("test 2");
		product2.setProductDesc("test Desc 2");
		product2.setImgAddr("test 2");
		product2.setPriority(2);
		product2.setEnableStatus(0);
		product2.setCreateTime(new Date());
		product2.setLastEditTime(new Date());
		product2.setShop(shop1);
		product2.setProductCategory(pc1);
		
		Product product3 = new Product();
		product3.setProductName("test 3");
		product3.setProductDesc("test Desc 3");
		product3.setImgAddr("test 3");
		product3.setPriority(3);
		product3.setEnableStatus(1);
		product3.setCreateTime(new Date());
		product3.setLastEditTime(new Date());
		product3.setShop(shop1);
		product3.setProductCategory(pc1);
		
		int effectedNum = productDao.insertProduct(product1);
		assertEquals(1, effectedNum);
		effectedNum = productDao.insertProduct(product2);
		assertEquals(1, effectedNum);
		effectedNum = productDao.insertProduct(product3);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testBQueryProductList() throws Exception {
		Product productCondition = new Product();
		
		// split by page, in this case will return 3 data
		List<Product> productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(3, productList.size());
		
		// search named test total number of products
		int count = productDao.queryProductCount(productCondition);
		assertEquals(7, count);
		
		// use product name to search ,assume will return 2 result
		//productCondition.setProductName("test");
		Shop shop = new Shop();
		shop.setShopId(1L);
		productCondition.setShop(shop);
		productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(3, productList.size());
		count = productDao.queryProductCount(productCondition);
		assertEquals(7, count);
	}
	
	@Test
	@Ignore
	public void testCQueryProductByProductId() throws Exception {
		long productId = 1;
		// initial three products and insert into shopId 1
		// product categoryId 1
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("image 1");
		productImg1.setImgDesc("test image 1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("image 2");
		productImg2.setImgDesc("test image 2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
		
		// check the productId 1 is it return list size is 2
		Product product = productDao.queryProductById(productId);
		assertEquals(2, product.getProductImgList().size());
		
		// delete this two product images from db
		effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
	
	@Test
	@Ignore
	public void testDUpdateProduct() throws Exception {
		Product product = new Product();
		ProductCategory pc = new ProductCategory();
		Shop shop = new Shop();
		
		shop.setShopId(1L);
		pc.setProductCategoryId(2L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductName("first product");
		product.setProductCategory(pc);
		
		// modify productName of productId 1 
		int effectedNum = productDao.updateProduct(product);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testEUpdateProductCategoryToNull() {
		// set productCategoryId 2's productCategoryId to null
		int effectedNum = productDao.updateProductCategoryToNull(2L);
		assertEquals(1,effectedNum);
	}
}
