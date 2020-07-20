/**
 * 
 */
$(function() {
	var loading = false;

	// the page allow return max number,
	// if over max number will not allow request the back-end
	var maxItems = 20;

	// one page allow return max number
	var pageSize = 10;

	// get shopList URL
	var listUrl = '/o2o/frontend/listproductsbyshop';

	// page number
	var pageNum = 1;

	// get parent shopId from address URL
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';

	// get current shopInfo and productCategory URL
	var searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId;

	// apply the shopCategory and areaList for user search
	getSearchDivData();

	// pre-load 10 shop Info
	addItems(pageSize, pageNum);

	// set redeem gift a tap URL (version 2.0)
	 $('#exchangelist').attr('href', '/o2o/frontend/awardlist?shopId=' + shopId);

	/**
	 * get shopCategory and areaList info
	 * 
	 * @return
	 */
	function getSearchDivData() {
		// if parentId exist, then get all level 2 category under the level 1
		var url = searchDivUrl;

		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								// get shopCategoryList from back-end
								var shop = data.shop;

								$('#shop-cover-pic').attr('src', getContextPath() + shop.shopImg);
								$('#shop-update-time').html(
										new Date(shop.lastEditTime)
												.Format("yyyy-MM-dd"));
								$('#shop-name').html(shop.shopName);
								$('#shop-desc').html(shop.shopDesc);
								$('#shop-addr').html(shop.shopAddr);
								$('#shop-phone').html(shop.phone);

								// get current shop productList
								var productCategoryList = data.productCategoryList;
								var html = '';

								// for loop productCategoryList, concat all a
								// tag
								productCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-product-search-id='
													+ item.productCategoryId
													+ '>'
													+ item.productCategoryName
													+ '</a>';
										});
								// set the productCategory a tag to HTML
								$('#shopdetail-button-div').html(html);
							}
						});

	}

	function addItems(pageSize, pageIndex) {
		// concat the searching URL, null will take out this condition limit,
		// not null will use this condition to do the search
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&productCategoryId=' + productCategoryId
				+ '&productName=' + productName + '&shopId=' + shopId;

		// set loading flag, if back-end still loading data,
		// then it will not allow keep request back-end
		loading = true;
		
		// get the productList by use the searching role
		$.getJSON(url, function(data) {
			if (data.success) {
				// get current search condition all shops number
				maxItems = data.count;
				var html = '';

				// for loop the shopList, concat the cards
				data.productList.map(function(item, index) {
					html += '<div class="card" data-product-id="' 
						+ item.productId
						+ '">' 
						+ '<div class="card-header">'
						+ item.productName 
						+ '</div>'
						+ '<div class="card-content">'
						+ '<div class="list-block media-list">' 
						+ '<ul>'
						+ '<li class="item-content">'
						+ '<div class="item-media">' 
						+ '<img src="'
						+ getContextPath()
						+ item.imgAddr 
						+ '" width="44">' 
						+ '</div>'
						+ '<div class="item-inner">'
						+ '<div class="item-subtitle">' 
						+ item.productDesc
						+ '</div>' 
						+ '</div>' 
						+ '</li>' 
						+ '</ul>'
						+ '</div>' 
						+ '</div>' 
						+ '<div class="card-footer">'
						+ '<p class="color-gray">'
						+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
						+ 'Updated</p>' 
						+ '<span>View</span>' 
						+ '</div>'
						+ '</div>';
				});
				// set the cards to front-end HTML
				$('.list-div').append(html);

				// get current presented cards number, include pre-load cards
				var total = $('.list-div .card').length

				// if total equals to search condition find out total number,
				// then stop back-end loading more
				if(total >= maxItems) {		
					// hide loading icon
					$('.infinite-scroll-preloader').hide();
				} else {
					$('.infinite-scroll-preloader').show();
				}

				// otherwise pageNum increase , keep loading new shops
				pageNum += 1;

				// finish loading, able loading again
				loading = false;

				// refresh page, to display new loaded shops
				$.refreshScroller();
			}
		});

	}

	// scroll-down will auto searching by pageIndex/pageSize
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// after choose new productCategory, needs clear the productList 
	// and reset the page, and use the new category condition to do search
	$('#shopdetail-button-div').on(
			'click',
			'.button',
			function(e) {
				// get productCategoryId 
				productCategoryId = e.target.dataset.productSearchId;

				if (productCategoryId) {
					// if already selected other category,
					// then needs remove the selected condition,
					// then change to the new condition
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						productCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}

					// searching condition changed, so needs clear shopList,
					// then to do the search
					$('.list-div').empty();

					// reset the page
					pageNum = 1;
					addItems(pageSize, pageNum);
				}

			});
	
	// click shop card enter the shop detail page
	$('.list-div').on('click', '.card', function(e) {
		var productId = e.currentTarget.dataset.productId;
		window.location.href = '/o2o/frontend/productdetail?productId=' + productId;
	});

	// if the input shopName has changed, then needs reset page,
	// clear original shopList, and use the new shopName to do the search
	
	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// click me then open the right side bar
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});

	// initial the page
	$.init();

});