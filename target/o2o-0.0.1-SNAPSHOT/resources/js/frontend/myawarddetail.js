/**
 * 
 */
$(function() {
	// get userAwardId from URL address
	var userAwardId = getQueryString('userAwardId');

	// get userAward info by userAwardId
	var awardUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId='
			+ userAwardId;

	$.getJSON(awardUrl, function(data) {
		if (data.success) {
			// get award info and shows
			var award = data.award;
			
			$('#award-img').attr('src',
					getContextPath() + award.awardImg);
			$('#create-time').text(
					new Date(data.userAwardMap.createTime)
					.Format("yyyy-MM-dd"));
			$('#award-name').text(award.awardName);
			$('#award-desc').text(award.awardDesc);
			
			var imgListHtml = '';
			// if not got to exchange the right award, then generate the redeem QRCode for shop scan
			if (data.usedStatus == 0) {
				imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4award?userAwardId='
					+ userAwardId
					+ '" width="100%"/></div>';
				$('#imgList').html(imgListHtml);
			}
		}
	});
	
	// binding side bar action
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	
	$.init();
});
