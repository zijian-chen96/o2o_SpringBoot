/**
 * 
 */
$(function() {
	var shopAuthId = getQueryString('shopAuthId');
	// input shopAuthId to get auth info URL
	var infoUrl = '/o2o/shopadmin/getshopauthmapbyid?shopAuthId=' + shopAuthId;
	// modify auth info URL
	var shopAuthPostUrl = '/o2o/shopadmin/modifyshopauthmap';

	if (shopAuthId) {
		getInfo(shopAuthId);
	} else {
		$.toast('User does not exist！');
		window.location.href = '/o2o/shopadmin/shopmanage';
	}

	function getInfo(id) {
		$.getJSON(infoUrl, function(data) {
			if (data.success) {
				var shopAuthMap = data.shopAuthMap;
				// set HTML value
				$('#shopauth-name').val(shopAuthMap.employee.name);
				$('#title').val(shopAuthMap.title);
			}
		});
	}

	$('#submit').click(function() {
		var shopAuth = {};
		var employee = {};
		// get the modify info and send to back-end
		shopAuth.employee = employee;
		shopAuth.employee.name = $('#shopauth-name').val();
		shopAuth.title = $('#title').val();
		shopAuth.shopAuthId = shopAuthId;
		var verifyCodeActual = $('#j_captcha').val();
		
		if (!verifyCodeActual) {
			$.toast('please enter verify code！');
			return;
		}
		
		$.ajax({
			url : shopAuthPostUrl,
			type : 'POST',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			data : {
				// convert json to string
				shopAuthMapStr : JSON.stringify(shopAuth),
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('sumbit success！');
					$('#captcha_img').click();
				} else {
					$.toast('sumbit failed！');
					$('#captcha_img').click();
				}
			}
		});
	});

});