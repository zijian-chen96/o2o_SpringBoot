/**
 * 
 */
$(function() {
	// get awardId from URL address
	var awardId = getQueryString('awardId');
	// get award info by awardId URL
	var infoUrl = '/o2o/shopadmin/getawardbyid?awardId=' + awardId;
	// modify award URL
	var awardPostUrl = '/o2o/shopadmin/modifyaward';
	// award add/modify use the same page,
	// use flag to determine this action is add or modify
	var isEdit = false;
	
	if (awardId) {
		// if there is awardId is and it is modify action
		getInfo(awardId);
		isEdit = true;
	} else {
		awardPostUrl = '/o2o/shopadmin/addaward';
	}
	
	// get the award info that needs modify, and set it to the form
	function getInfo(id) {
		$.getJSON(infoUrl, function(data) {
			if (data.success) {
				// get award object from JSON, and set it to form
				var award = data.award;
				$('#award-name').val(award.awardName);
				$('#priority').val(award.priority);
				$('#award-desc').val(award.awardDesc);
				$('#point').val(award.point);
			}
		});
	}
	
	// binding action to submit, work for add and modify different action
	$('#submit').click(function() {
		// create award json object, and set the value
		var award = {};
		award.awardName = $('#award-name').val();
		award.priority = $('#priority').val();
		award.awardDesc = $('#award-desc').val();
		award.point = $('#point').val();
		award.awardId = awardId ? awardId : '';
		
		// get viewImage stream
		var thumbnail = $('#small-img')[0].files[0];
		
		// create form object use for hold the data and send to back-end
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		
		// convert award to json string and set the key to awardStr
		formData.append('awardStr', JSON.stringify(award));
		
		// get verify code
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('please enter verify code！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
	
		// send the data to back-end and process
		$.ajax({
			url : awardPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('action success！');
					$('#captcha_img').click();
				} else {
					$.toast('action failed！');
					$('#captcha_img').click();
				}
			}
		});
	});

});