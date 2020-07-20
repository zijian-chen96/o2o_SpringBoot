/**
 * 
 */
$(function() {
	var userName = '';
	getList();
	function getList() {
		// get shop points URL
		var listUrl = '/o2o/shopadmin/listusershopmapbyshop?pageIndex=1&pageSize=9999'
				+ '&userName=' + userName;
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var userShopMapList = data.userShopMapList;
				var tempHtml = '';
				// concat the users and points data
				userShopMapList.map(function(item, index) {
					tempHtml += '' + '<div class="row row-usershopcheck">'
							+ '<div class="col-50">' + item.user.name
							+ '</div>' + '<div class="col-50">' + item.point
							+ '</div>' + '</div>';
				});
				$('.usershopcheck-wrap').html(tempHtml);
			}
		});
	}

	// binding search, fuzzy search by userName
	$('#search').on('change', function(e) {
		userName = e.target.value;
		$('.usershopcheck-wrap').empty();
		getList();
	});
});