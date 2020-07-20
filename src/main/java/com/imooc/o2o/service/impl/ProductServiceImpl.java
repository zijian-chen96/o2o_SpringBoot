package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductImgDao productImgDao;

	// 1. process the image, get images and store the addr to product
	// 2. store the data into tb_product and get productId back
	// 3. use productId to process multiple product images
	// 4. store the images into tb_product_img
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		
		// null check
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// set the default value  for product
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			
			// default the product is allow to show
			product.setEnableStatus(1);
			
			// if product view image is not null then add image
			if(thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			
			try {
				// create product info
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum <= 0) {
					throw new ProductOperationException("failed to create product!");
				}
			} catch(Exception e) {
				throw new ProductOperationException("failed to create product:" + e.toString());
			}
			
			// if detail image is not null then add image
			if(productImgHolderList != null && productImgHolderList.size() > 0) {
				addProductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
			
		} else {
			// return error messages
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * add view images
	 * 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	
	
	/**
	 * add multiple images
	 * 
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// get images dest, and store into correct shop folder
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		
		// for each loop to process all image, and insert into productImg
		for(ImageHolder productImgHolder : productImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		
		// if the list is not null, and it will to process add multiple images into db
		if(productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if(effectedNum <= 0) {
					throw new ProductOperationException("failed to create product images");
				}
			} catch(Exception e) {
				throw new ProductOperationException("failed to create product images:" + e.toString());
			}
		}
		
		
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}
	
	// 1.if there is view image, then process it first,
	// if there is old view image, delete it first then use the new image
	// 2.if there is detail images, delete first then store the new images
	// 3.delete all tb_product_img original images
	// 4.update tb_produt and tb_product_img info
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		// null checker
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// set the default for product
			product.setLastEditTime(new Date());
			
			// if product view image is not null and original view image is not null
			// then delete the old image then add new images
			if(thumbnail != null) {
				// to get original data from db, token out the path of original images
				Product tempProduct = productDao.queryProductById(product.getProductId());
				
				if(tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			
			// if there is new product detail images, delete old images first then add new images
			if(productImgHolderList != null && productImgHolderList.size() > 0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			
			try {
				// update product info
				int effectedNum = productDao.updateProduct(product);
				
				if(effectedNum <= 0) {
					throw new ProductOperationException("failed update product info");
				}
				
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch(Exception e) {
				throw new ProductOperationException("failed update product info:" + e.toString());
			}
			
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * delete all detail images under the product
	 * 
	 * @param productId
	 */
	private void deleteProductImgList(long productId) {
		// use productId to get original images
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		
		// delete all original images
		for(ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		
		// delete all image data from db
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// page convert to db's index, and use dao to return the current page product list
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		
		// base on the same search return the total number of products
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}
	
	

}
