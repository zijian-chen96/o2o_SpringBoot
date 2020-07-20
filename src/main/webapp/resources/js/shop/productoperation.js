/**
 * 
 */
$(function() {
	// get productId from url
	var productId = getQueryString('productId');

	// use productId get product info
	var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;

	// get current shop's product category list
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';

	// update product info
	var productPostUrl = '/o2o/shopadmin/modifyproduct';

	// add product and modify product use same website
	// so, needs to determine the action of add/modify
	var isEdit = false;

	if (productId) {
		// if productId exist, then modify action
		getInfo(productId);
		isEdit = true;
	} else {
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct';
	}

	// to get edit product info from db, then insert into web-site
	function getInfo(id) {
		$.getJSON(infoUrl, function(data) {
			if(data.success) {
				// token the product info from return JSON data, then insert into web-site
				var product = data.product;
				$("#product-name").val(product.productName);
				$("#product-desc").val(product.productDesc);
				$("#priority").val(product.priority);
				$("#normal-price").val(product.normalPrice);
				$("#promotion-price").val(product.promotionPrice);
				
				// token original product category and current shop all category list
				var optionHtml = '';
				var optionArr = data.productCategoryList;
				var optionSelected = product.productCategory.productCategoryId;
				
				// generate front-end html product category list, 
				// and show the default product category before modify
				optionArr.map(function(item, index) {
					var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
					
					optionHtml += '<option data-value="'
						+ item.productCategoryId
						+ '"'
						+ isSelect
						+ '>'
						+ item.productCategoryName
						+'</option>';
				});
				$('#category').html(optionHtml);
			}
		});	
	}
	
	// get current shop all product category list, use for add new product action
	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if(data.success){
				var productCategoryList = data.data;
				var optionHtml = '';
				
				productCategoryList.map(function(item, index) {
					optionHtml += '<option data-value="'
						+ item.productCategoryId 
						+ '">'
						+ item.productCategoryName
						+ '</option>';
				});
				$('#category').html(optionHtml);
			}
		});	
	};
	
	// use for add product detail images, if we add new image, 
	// then it will create new upload box at the end
	// max allow upload 6 images
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});
	
	// sumbit botton action, needs determine the action is for add/modify product
	// then do correct response
	$('#submit').click(function() {
		// create product json object, then get the data from web-site
		var product = {};
		product.productName = $('#product-name').val();
		product.productDesc = $('#product-desc').val();
		product.priority = $('#priority').val();
		product.point = $('#point').val();
		product.normalPrice = $('#normal-price').val();
		product.promotionPrice = $('#promotion-price').val();
		// get the selected product category
		product.productCategory = {
				productCategoryId : $('#category').find('option').not(function() {
					return !this.selected;
				}).data('value')
		};
		product.productId = productId;
		
		// get view image file
		var thumbnail = $('#small-img')[0].files[0];
		// generate a Form object, use to accept data and send to back-end
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		
		// for loop the prodcut detail image, and get the image file
		$('.detail-img').map(function(index, item) {
			// check is it contain files
			if($('.detail-img')[index].files.length > 0){
				// store i-th files into dictionary formData with key productImg
				formData.append('productImg' + index, $('.detail-img')[index].files[0]);	
			}
		});
		
		// store the product json object into formData dictionary with key productStr
		formData.append('productStr', JSON.stringify(product));
		
		// get the verify code from the web-site
		var verifyCodeActual = $('#j_captcha').val();
		if(!verifyCodeActual) {
			$.toast('please enter verify code!');
			return;
		}
		
		formData.append("verifyCodeActual", verifyCodeActual);
		
		// send the data back to back-end
		
		$.ajax({
			url : productPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if(data.success){
					$.toast('sumbit success!');
					$('#captcha_img').click();
				} else{
					$.toast('sumbit failed!');
					$('#captcha_img').click();
				}
			}
		});
		
	});
	
	
	

});