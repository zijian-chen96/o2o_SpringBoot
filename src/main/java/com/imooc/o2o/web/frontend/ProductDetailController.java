package com.imooc.o2o.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
	@Autowired
	private ProductService productService;

	/**
	 * get product detail by use productId
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// get productId from front-end
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;

		// null checker
		if (productId != -1) {
			// get productInfo and product detail image by use productId
			product = productService.getProductById(productId);

			// 2.0 version new add
			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
			if (user == null) {
				modelMap.put("needQRCode", false);
			} else {
				modelMap.put("needQRCode", true);
			}

			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}

		return modelMap;
	}

	// get Wechat userInfo api prefix
	private static String urlPrefix;

	// get Wechat userInfo api middle
	private static String urlMiddle;

	// get Wechat userInfo api suffix
	private static String urlSuffix;

	// response from Wechat to add customer award info url
	private static String productmapUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		ProductDetailController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		ProductDetailController.urlMiddle = urlMiddle;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		ProductDetailController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.productmap.url}")
	public void setProductmapUrl(String productmapUrl) {
		ProductDetailController.productmapUrl = productmapUrl;
	}

	/**
	 * generate award redeem QRCode for clerk to scan and receive the award,
	 * scan the QRCode will goto the URL
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
		// get productId from front-end
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		// get user info from session
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
	
		// null checker
		if (productId != -1 && user != null && user.getUserId() != null) {
			// get the current timeStamp, to make sure the QRCode works in the times, unit is ms
			long timpStamp = System.currentTimeMillis();
			
			// set the productId, userId, timeStamp to content, and set it to state,
			// if Wecat got the data and it will response the data info, and add aaa will replace after it
			String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:" + user.getUserId()
					+ ",aaacreateTimeaaa:" + timpStamp + "}";
			try {
				// encode the content data by use base64 for avoid special character effect, and concat URL
				String longUrl = urlPrefix + productmapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8")
						+ urlSuffix;

				// convert the URL to short URL
				String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
				// use the QRcode generator tool to create the QRCode
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				// send the QRCode image stream to front-end
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
