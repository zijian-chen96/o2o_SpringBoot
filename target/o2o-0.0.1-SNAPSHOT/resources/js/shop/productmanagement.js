/**
 * 
 */
$(function() {
	// get current shop all product list
	var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	
	// disable product URL
	var statusUrl ='/o2o/shopadmin/modifyproduct';
	
	getList();
	
	/**
	 * get product list under the shop
	 */
	function getList() {
		// get product list from back-end
		$.getJSON(listUrl, function(data) {
			if(data.success) {
				var productList = data.productList;
				var tempHtml = '';
				
				// for loop the product info, concat together, info include:
				// productName, priority, enable/disable(include productId), edit(include productId)
				// view(include productId)
				productList.map(function(item, index) {
					var textOp = "enable";
					var contraryStatus = 0;
					
					if(item.enableStatus == 0){
						// if enableStatus is 0 means the product is disable, 1 is enable
						textOp = 'disable';
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					
					// concat all product's info
					tempHtml += '<div class="row row-product">'
						+ '<div class="col-33">'
						+ item.productName
						+ '</div>'
						+ '<div class="col-20">'
						+ item.point
						+ '</div>'
						+ '<div class="col-40">'
						+ '<a href="#" class="edit" data-id="'
						+ item.productId
						+ '" data-status="'
						+ item.enableStatus
						+ '">Edit</a>'
						+ '<a href="#" class="status" data-id="'
						+ item.productId
						+ '" data-status="'
						+ contraryStatus
						+ '">'
						+ textOp
						+ '</a>'
						+ '<a href="#" class="preview" data-id="'
						+ item.productId
						+ '" data-status="'
						+ item.enableStatus
						+ '">Preview</a>'
						+ '</div>'
						+ '</div>'
				});
				// store the product info to html 
				$('.product-wrap').html(tempHtml);
			}
		});
	}
	
	// binding click actions for product-warp's a tap, and use the class elements
	$('.product-wrap').on('click', 'a', function(e) {
		var target = $(e.currentTarget);
		if(target.hasClass('edit')) {
			// if class edit on click and it will get into product info edit web-site, and it include productId
			window.location.href = '/o2o/shopadmin/productoperation?productId=' + e.currentTarget.dataset.id;
		} else if(target.hasClass('status')) {
			// if class status on click and it will use the back-end feature to enable/disable the product, and it include productId
			changeItemStatus(e.currentTarget.dataset.id, e.currentTarget.dataset.status);
		} else if(target.hasClass('preview')) {
			// if class preview on click and it will show the product preview on the front-end system
			window.location.href = '/o2o/frontend/productdetail?productId=' + e.currentTarget.dataset.id;
		}
	});
	
	function changeItemStatus(id, enableStatus) {
		// default product json object and add productId and status(enable/disable)
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		
		$.confirm('confirm?', function() {
			// enable/disable product
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if(data.success) {
						$.toast('action success!');
						getList();
					} else {
						$.toast('action failed!');
					}
				}				
			});
		});		
	}
	
});