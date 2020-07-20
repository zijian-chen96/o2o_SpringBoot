/**
 * 
 */
$(function() {
	var loading = false;
	var maxItems = 20;
	var pageSize = 10;
	// get customer record URL
	var listUrl = '/o2o/frontend/listuserproductmapsbycustomer';
	var pageNum = 1;
	var productName = '';
	
	addItems(pageSize, pageNum);
	
	// get purchase history by input search condition, and set the HTML page
	function addItems(pageSize, pageIndex) {
		// generate new one to HTML
		var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize
				+ '&productName=' + productName;
		loading = true;
		
		$.getJSON(url, function(data) {
			if (data.success) {
				// total number
				maxItems = data.count;
				var html = '';
				data.userProductMapList.map(function(item, index) {
					html += '' + '<div class="card" data-product-id='
							+ item.productId + '>'
							+ '<div class="card-header">' + item.shop.shopName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">'
							+ item.product.productName + '</div>' + '</div>'
							+ '</li>' + '</ul>' + '</div>' + '</div>'
							+ '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.createTime).Format("yyyy-MM-dd")
							+ '</p>' + '<span>Points：' + item.point + '</span>'
							+ '</div>' + '</div>';
				});
				$('.list-div').append(html);
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// finished loading, remove loading action avoid overload
					$.detachInfiniteScroll($('.infinite-scroll'));
					// delete loading icon
					$('.infinite-scroll-preloader').remove();
					return;
				}
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}

	// infinite scroll and display by pages
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// binding search action, fuzzy search
	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	
	// binding side bar action
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	
	$.init();
});
