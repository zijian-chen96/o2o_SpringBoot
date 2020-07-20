/**
 * 
 */
$(function() {
	var loading = false;
	var maxItems = 20;
	var pageSize = 10;
	// get user award redeem record list URL
	var listUrl = '/o2o/frontend/listuserawardmapsbycustomer';

	var pageNum = 1;
	var awardName = '';
	addItems(pageSize, pageNum);
	
	// search awards by input condition, and set to HTML
	function addItems(pageSize, pageIndex) {
		// create new one HTML
		var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize
				+ '&awardName=' + awardName;
		loading = true;
		
		$.getJSON(url, function(data) {
			if (data.success) {
				// get total number
				maxItems = data.count;
				var html = '';
				data.userAwardMapList.map(function(item, index) {
					var status = '';
					// check usedStatus and show the award is redeem or not
					if (item.usedStatus == 0) {
						status = "Redeem";
					} else if (item.usedStatus == 1) {
						status = "Redeemed";
					}
					html += '' + '<div class="card" data-user-award-id='
							+ item.userAwardId + '>'
							+ '<div class="card-header">' + item.shop.shopName
							+ '<span class="pull-right">' + status
							+ '</sapn></div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">'
							+ item.award.awardName + '</div>' + '</div>'
							+ '</li>' + '</ul>' + '</div>' + '</div>'
							+ '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.createTime).Format("yyyy-MM-dd")
							+ '</p>' + '<span>Cost Points: ' + item.point + '</span>'
							+ '</div>' + '</div>';
				});
				$('.list-div').append(html);
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// finished loading, cancel loading action avoid overload
					$.detachInfiniteScroll($('.infinite-scroll'));
					// delete loading flag
					$('.infinite-scroll-preloader').remove();
					return;
				}
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}
	
	// binding click action, click will go to award redeem detail page
	// customer will use the QR code to the shop scan and receive the awards
	$('.list-div')
			.on(
					'click',
					'.card',
					function(e) {
						var userAwardId = e.currentTarget.dataset.userAwardId;
						window.location.href = '/o2o/frontend/myawarddetail?userAwardId='
								+ userAwardId;
					});
	
	// infinite scroll
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// binding search action, use for award fuzzy name search
	$('#search').on('change', function(e) {
		awardName = e.target.value;
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
