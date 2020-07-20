/**
 * 
 */
$(function() {
	// bind account controller url
	var bindUrl = '/o2o/local/bindlocalauth';
	
	// get userType ffrom url address
	// userType = 1 is front-end displey system, other is shop management system
	var usertype = getQueryString('usertype');
	
	$('#submit').click(function() {
		// get input username
		var userName = $('#username').val();
		
		// get input password
		var password = $('#psw').val();
		
		// get input verify code
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		
		if(!verifyCodeActual) {
			$.toast('please enter verify code!');
			return;
		}
		
		// request back-end, and bind account
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if(data.success) {
					$.toast('Link Success!');
					if(usertype == 1) {
						// if user on the front-end system, then auto goto front-end display system
						window.location.href = '/o2o/frontend/index';
					} else {
						// if user on shop management system, then goto shoplist
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('failed to sumbit! ' + data.errMsg);
					$('#captcha_img').click();
				}
			}
			
		});

	});	
	
});