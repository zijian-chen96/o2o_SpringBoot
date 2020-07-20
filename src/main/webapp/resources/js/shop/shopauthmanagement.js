/**
 * 
 */
$(function() {
	// list all auth info under the shop URL
	var listUrl = '/o2o/shopadmin/listshopauthmapsbyshop?pageIndex=1&pageSize=9999';
	// edit auth info URL
	var modifyUrl = '/o2o/shopadmin/modifyshopauthmap';
	getList();

	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var shopauthList = data.shopAuthMapList;
				var tempHtml = '';
				shopauthList.map(function(item, index) {
					var textOp = "Renew";
					var contraryStatus = 0;
					
					if (item.enableStatus == 1) {
						// if status is 1, means auth enable, action change to remove(click button to remove auth)
						textOp = "Remove";
						contraryStatus = 0;
					} else {
						contraryStatus = 1;
					}
					
					tempHtml += '' + '<div class="row row-shopauth">'
							+ '<div class="col-40">' + item.employee.name
							+ '</div>';
					
					if (item.titleFlag != 0) {
						// if is not owner's auth info, we add edit action on
						tempHtml += '<div class="col-20">' + item.title
								+ '</div>' + '<div class="col-40">'
								+ '<a href="#" class="edit" data-employee-id="'
								+ item.employee.userId + '" data-auth-id="'
								+ item.shopAuthId + '">Edit</a>'
								+ '<a href="#" class="status" data-auth-id="'
								+ item.shopAuthId + '" data-status="'
								+ contraryStatus + '">' + textOp + '</a>'
								+ '</div>'
					} else {
						// if is owner, not allow have edit action
						tempHtml += '<div class="col-20">' + item.title
								+ '</div>' + '<div class="col-40">'
								+ '<span>No Action</span>' + '</div>'
					}
					tempHtml += '</div>';
				});
				$('.shopauth-wrap').html(tempHtml);
			}
		});
	}

	/**
	 * bind click action on a tag, click eidt button witn a tag it will goto edit page, 
	 * and click status with a tag will goto update auth info status
	 * 
	 */
	$('.shopauth-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						
						if (target.hasClass('edit')) {
							window.location.href = '/o2o/shopadmin/shopauthedit?shopAuthId='
									+ e.currentTarget.dataset.authId;
						} else if (target.hasClass('status')) {
							changeStatus(e.currentTarget.dataset.authId,
									e.currentTarget.dataset.status);
						}
					});
	
	function changeStatus(id, status) {
		var shopAuth = {};
		shopAuth.shopAuthId = id;
		shopAuth.enableStatus = status
		
		$.confirm('Confirm?', function() {
			$.ajax({
				url : modifyUrl,
				type : 'POST',
				data : {
					// convert json to string 
					shopAuthMapStr : JSON.stringify(shopAuth),
					statusChange : true,
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('Action Success！');
						getList();
					} else {
						$.toast('Action Success！');
					}
				}
			});
		});
	}
	
});