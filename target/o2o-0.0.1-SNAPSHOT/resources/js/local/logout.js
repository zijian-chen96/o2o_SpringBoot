/**
 * 
 */
$(function() {
	$('#log-out').click(function() {
		// clear session
		$.ajax({
			url : "/o2o/local/logout",
			type : "post",
			async : false,
			cache : false,
			dataType : 'json',
			success : function(data) {
				if(data.success) {
					var usertype = $('#log-out').attr('usertype');
					// success clear will return to login page
					window.location.href = '/o2o/local/login?usertype=' + usertype;
					return false;
				}
			},
			error : function(data, error) {
				alert(error);
			}
		});
		
	});
	
});