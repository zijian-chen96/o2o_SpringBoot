/**
 * 
 */
$(function() {
	// get productId from address URL
	var productId = getQueryString('productId');
	
	// get productInfo URL
	var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;
	
	// get productInfo from back-end and send to front-end
	$.getJSON(productUrl, function(data) {
		if(data.success){
			// get productInfo
			var product = data.product;
			
			// set the productInfo to HTML
			// product view image
			$('#product-img').attr('src', getContextPath() + product.imgAddr);
			
			// product update time
			$('#product-time').text(new Date(product.lastEditTime).Format("yyyy-MM-dd"));
			
			if(product.point != undefined) {
				$('#product-point').text('purchase can get ' + product.point + ' points');
			}
			
			// product name
			$('#product-name').text(product.productName);
			
			// product description
			$('#product-desc').text(product.productDesc);
			
			// product price display logic, check the original price is null or not,
			// all null will not display price
			if(product.normalPrice != undefined && product.promotionPrice != undefined){
				// if current price and normal price is not null, 
				// then needs add '\' on the normal price
				$('#price').show();
				$('#normalPrice').html('<del>' + '$' + product.normalPrice + '</del>');
				$('#promotionPrice').text('$' + product.promotionPrice);
			} else if(product.normalPrice != undefined && product.promotionPrice == undefined) {
				// if normalPrice is null promotionPrice is not, then show the promotionPrice
				$('#price').show();
				$('#promotionPrice').text('$' + product.normalPrice);
			} else if(product.normalPrice == undefined && product.promotionPrice != null) {
				// if normallPrice not null promotionPrice is null, then show the normalPrice
				$('#promotionPrice').text('$' + product.promotionPrice);
			}
			
			var imgListHtml = '';
			
			// for loop the productDetaiImageList, generate multiple img tag
			product.productImgList.map(function(item, index) {
				imgListHtml += '<div><img src="'
					+ getContextPath()
					+ item.imgAddr
					+ '" width="100%"/></div>';
			});
			
			// 2.0 version new add
			if(data.needQRCode) {
				// generate out buy product RQ Code for scan
				imgListHtml += '<div><img src="/o2o/frontend/generateqrcode4product?productId='
					+ product.productId
					+ '" width="100%"/></div>';
			}
			
			$('#imgList').html(imgListHtml);	
		}
	});
	
	// click me then open the right side bar
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});

	// initial the page
	$.init();
	
	
});