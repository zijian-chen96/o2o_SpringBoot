/**
 * 
 */
$(function() {
	var loading = false;
	var maxItems = 999;
	var pageSize = 10;
	// get award list URL
	var listUrl = '/o2o/frontend/listawardsbyshop';
	// redeem award URL
	var exchangeUrl = '/o2o/frontend/adduserawardmap';
	var pageNum = 1;
	// get shopId from URL address
	var shopId = getQueryString('shopId');
	var awardName = '';
	var canProceed = false;
	var totalPoint = 0;

	// pre-load 20 awards
	addItems(pageSize, pageNum);

	// search the award list, and set it to HTML page
	function addItems(pageSize, pageIndex) {
		// create new one HTML
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&shopId=' + shopId + '&awardName=' + awardName;
		loading = true;
		
		$.getJSON(url, function(data) {
			if (data.success) {
				// get the total number
				maxItems = data.count;
				var html = '';
				data.awardList.map(function(item, index) {
					html += '' + '<div class="card" data-award-id="'
							+ item.awardId + '" data-point="' + item.point
							+ '">' + '<div class="card-header">'
							+ item.awardName + '<span class="pull-right">Need Points: '
							+ item.point + '</span></div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ getContextPath() + item.awardImg
							+ '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.awardDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ 'Update</p>';
					
					if (data.totalPoint != undefined) {
						// if the user have points, show the redeem button
						html += '<span>Redeem</span></div></div>'
					} else {
						html += '</div></div>'
					}
				});
				$('.list-div').append(html);
				
				if (data.totalPoint != undefined) {
					// if the user own points and will show
					canProceed = true;
					$("#title").text('Current Points ' + data.totalPoint);
					totalPoint = data.totalPoint;
				}
				
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// finish loading, no need loading, avoid overload
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

	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		// infinite scroll
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	$('.award-list').on('click', '.card', function(e) {
		// if the user own points higher than the award cost point
		if (canProceed && (totalPoint >= e.currentTarget.dataset.point)) {
			// and will pop up the confirmation 
			$.confirm('cost ' + e.currentTarget.dataset.point + ' points, confirm?', function() {
				// request back-end, to get awards
				$.ajax({
					url : exchangeUrl,
					type : 'POST',
					data : {
						awardId : e.currentTarget.dataset.awardId
					},
					dataType : 'json',
					success : function(data) {
						if (data.success) {
							$.toast('action success！');
							totalPoint = totalPoint - e.currentTarget.dataset.point;
							$("#title").text('Current Points ' + totalPoint);
						} else {
							$.toast('action failed！');
						}
					}
				});
			});
		} else {
			$.toast('no enough points or illegal action!');
		}
	});

	// search condition, fuzzy award name search
	$('#search').on('change', function(e) {
		awardName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// side bar button action
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});

	$.init();
});
