/**
 * 
 */
$(function() {
	// change password controller url
	var url = '/o2o/local/changelocalpwd';
	
	// get usertype from url address
	// usertype = 1 is customer, other is shopowner
	var usertype = getQueryString('uesrtype');
	
	$('#submit').click(function() {
		// get input username
		var userName = $('#userName').val();
		
		// get input password
		var password = $('#password').val();
		
		// get input new password
		var newPassword = $('#newPassword').val();
		var confirmPassword = $('#confirmPassword').val();
		
		if(newPassword != confirmPassword) {
			$.toast('the second times new password is not match!');
			return;
		}
		
		// add data form
		var formData = new FormData();
		formData.append('userName', userName);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		
		// get verify code
		var verifyCodeActual = $('#j_captcha').val();
		if(!verifyCodeActual) {
			$.toast('please enter verify code!');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		
		// post the value to back-end for change the password
		$.ajax({
			url : url,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if(data.success) {
					$.toast('submit success!');
					if(usertype == 1) {
						// if user on the front-end system, then auto goto front-end display system
						window.location.href = '/o2o/frontend/index';
					} else {
						// if user on shop management system, then goto shoplist
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('submit failed! ' + data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
	
	$('#back').click(function() {
		window.location.href = '/o2o/shopadmin/shoplist';
	});
	
});