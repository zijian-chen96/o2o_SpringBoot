/**
 * 
 */
$(function() {
	// login verify controller url
	var loginUrl = '/o2o/local/logincheck';
	
	// get usertype from url address
	// usertype = 1 is customer, other is shopowner
	var usertype = getQueryString('usertype');
	
	// login count, if login failed 3 times, then needs verify code to login
	var loginCount = 0;
	
	$('#submit').click(function() {
		// get input username
		var userName = $('#username').val();
		
		// get input password
		var password = $('#psw').val();
		
		// get verify code info
		var verifyCodeActual = $('#j_captcha').val();
		
		// check is it need verify or not, default false means not need verify
		var needVerify = false;
		
		// if login failed 3 time, then need verify
		if(loginCount >= 3){
			if(!verifyCodeActual) {
				$.toast('please enter verify code!');
				return;
			} else {
				needVerify = true;
			}	
		}
		
		// request back-end for login verify
		$.ajax({
			url : loginUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual,
				// need for verify code
				needVerify : needVerify
			},
			success : function(data) {
				if(data.success) {
					$.toast('login success!');
					if(usertype == 1) {
						// if user on the front-end system, then auto goto front-end display system
						window.location.href = '/o2o/frontend/index';
					} else {
						// if user on shop management system, then goto shoplist
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('login failed! ' + data.errMsg);
					loginCount++;
					if(loginCount >= 3) {
						// login failed 3 more times, then need to verify code
						$('#verifyPart').show();
					}
				}
			}
		});
			
	});
	
});