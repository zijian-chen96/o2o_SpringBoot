/**
 * 
 */
$(function() {
	var loading = false;
	
	// the page allow return max number, 
	// if over max number will not allow request the back-end
	var maxItems = 999;
	
	// one page allow return max number
	var pageSize = 10;
	
	// get shopList URL
	var listUrl = '/o2o/frontend/listshops';
	
	// get shopCategoryList and areaList URL
	var searchDivUrl = '/o2o/frontend/listshopspageinfo';
	
	// page number
	var pageNum = 1;
	
	// get parent shop category id from address URL
	var parentId = getQueryString('parentId');
	var areaId = '';
	var shopCategoryId = '';
	var shopName = '';
	
	// apply the shopCategory and areaList for user search
	getSearchDivData();
	
	// pre-load 10 shop Info
	addItems(pageSize, pageNum);
	
	/**
	 * get shopCategory and areaList info
	 * 
	 * @return
	 */
	function getSearchDivData() {
		// if parentId exist, then get all level 2 category under the level 1
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		
		$.getJSON(url, function(data) {
			if(data.success) {
				// get shopCategoryList from back-end
				var shopCategoryList = data.shopCategoryList;
				var html = '';
				
				html += '<a href="#" class="button" data-category-id="">All Category</a>';
				
				// for loop shopCategoryList, concat all a tag
				shopCategoryList.map(function(item, index) {
					html += '<a href="#" class="button" data-category-id='
						+ item.shopCategoryId
						+ '>'
						+ item.shopCategoryName
						+ '</a>';
				});
				
				// set the category tag to front-end HTML
				$('#shoplist-search-div').html(html);
				
				var selectOptions = '<option value="">All Area</option>';
				
				// get areaList info from back-end
				var areaList = data.areaList;
				
				// for loop the areaList info, concat the option tag
				areaList.map(function(item, index) {
					selectOptions += '<option value="'
						+ item.areaId 
						+ '">'
						+ item.areaName
						+ '</option>';
				});
				// set the area tag to front-end HTML
				$('#area-search').html(selectOptions);
			}
		});

	}
	
	function addItems(pageSize, pageIndex) {
		// concat the searching URL, null will take out this condition limit, 
		// not null will use this condition to do the search
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
			+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
			+ '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
		
		// set loading flag, if back-end still loading data, 
		// then it will not allow keep request back-end
		loading = true;
		
		$.getJSON(url, function(data) {
			if(data.success) {
				// get current search condition all shops number
				maxItems = data.count;
				var html = '';
				
				// for loop the shopList, concat the cards
				data.shopList.map(function(item, index) {
					html += '<div class="card" data-shop-id="'
						+ item.shopId
						+ '">'
						+ '<div class="card-header">'
						+ item.shopName
						+ '</div>'
						+ '<div class="card-content">'
						+ '<div class="list-block media-list">'
						+ '<ul>'
						+ '<li class="item-content">'
						+ '<div class="item-media">'
						+ '<img src="'
						+ getContextPath()
						+ item.shopImg
						+ '" width="44">'
						+ '</div>'
						+ '<div class="item-inner">'
						+ '<div class="item-subtitle">'
						+ item.shopDesc
						+ '</div>'
						+ '</div>'
						+ '</li>'
						+ '</ul>'
						+ '</div>'
						+ '</div>'
						+ '<div class="card-footer">'
						+ '<p class="color-gray">'
						+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
						+ ' Updated</p>'
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
		if(loading)
			return;
		addItems(pageSize, pageNum);
	});
	
	// click shop card enter the shop detail page
	$('.shop-list').on('click', '.card', function(e) {
		var shopId = e.currentTarget.dataset.shopId;
		window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
	});
	
	// after choose new shopCategory, needs clear the shopList and reset the page, 
	// and use the new category condition to do search
	$('#shoplist-search-div').on('click', '.button', function(e) {
		// if it is a childCategory under a parentCategory
		if(parentId){
			shopCategoryId = e.target.dataset.categoryId;
			
			// if already selected other category, 
			// then needs remove the selected condition, 
			// then change to the new condition
			if($(e.target).hasClass('button-fill')) {
				$(e.target).removeClass('button-fill');
				shopCategoryId = '';
			} else {
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			
			// searching condition changed, so needs clear shopList,
			// then to do the search
			$('.list-div').empty();
			
			// reset the page
			pageNum = 1;
			addItems(pageSize, pageNum);
			
		// if parentCategory is null, then search by the parentCategory
		} else {
			parentId = e.target.dataset.categoryId;
			
			if($(e.target).hasClass('button-fill')){
				$(e.target).removeClass('button-fill');
				parentId = '';
			} else {
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			
			// searching condition changed, so needs clear shopList,
			// then to do the search
			$('.list-div').empty();
			
			// reset the page
			pageNum = 1;
			addItems(pageSize, pageNum);
			parentId = '';
		}
		
	});
	
	// if the input shopName has changed, then needs reset page,
	// clear original shopList, and use the new shopName to do the search
	$('#search').on('change', function(e) {
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	
	// if area info has changed, then needs reset the page,
	// clear the original shopList, and use new area to do search
	$('#area-search').on('change', function() {
		areaId = $('#area-search').val();
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