/**
 * 
 */

$(function() {
	// get headLineList and root categoryList URL
	var url = '/o2o/frontend/listmainpageinfo';
	
	// send request to back-end, get the data(headLine and root category)
	$.getJSON(url, function(data) {
		if(data.success) {
			// get the headList from back-end
			var headLineList = data.headLineList;
			var swiperHtml = '';
			
			// for loop the headLineList, and concat rolling images together
			headLineList.map(function(item, index) {
				swiperHtml += '<div class="swiper-slide img-wrap">'
					+ '<a href="'
					+ item.lineLink
					+ '"external><img class="banner-img" src="'
					+ getContextPath()
					+ item.lineImg
					+ '" alt="'
					+ item.lineName
					+ '"></a>'
					+ '</div>';
			});
			
			// set the rolling images to HTML
			$('.swiper-wrapper').html(swiperHtml);
			
			// set the time on rolling image to 3 seconds per image
			$('.swiper-container').swiper( {
				autoplay : 3000,
				// if the use doing action on the image and it will auto stop/not stop the autoplay
				autoplayDisableOnInteraction : false
			});
			
			// get root category from back-end
			var shopCategoryList = data.shopCategoryList;
			var categoryHtml = '';
			
			// for loop the categoryList, and concat category together(col-50, one row 2 category)
			shopCategoryList.map(function(item, index) {
				categoryHtml += '<div class="col-50 shop-classify" data-category='
					+ item.shopCategoryId
					+ '>'
					+ '<div class="word">'
					+ '<p class="shop-title">'
					+ item.shopCategoryName
					+ '</p>'
					+ '<p class="shop-desc">'
					+ item.shopCategoryDesc
					+ '</p>'
					+ '</div>'
					+ '<div class="shop-classify-img-warp">'
					+ '<img class="shop-img" src="'
					+ getContextPath()
					+ item.shopCategoryImg
					+ '">'
					+ '</div>'
					+ '</div>';
			});
			// set the category to HTML
			$('.row').html(categoryHtml);
		}		
	});
	
	// on click "me", will show the side bar
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	
	$('.row').on('click', '.shop-classify', function(e) {
		var shopCategoryId = e.currentTarget.dataset.category;
		var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
		window.location.href = newUrl;
	});
	
	
});