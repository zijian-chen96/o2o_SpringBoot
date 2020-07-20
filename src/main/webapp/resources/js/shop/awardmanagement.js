/**
 * 
 */
$(function() {
	// get awards under the shop URL
	var listUrl = '/o2o/shopadmin/listawardbyshop?pageIndex=1&pageSize=9999';
	// set awards enableStatus URL
	var changeUrl = '/o2o/shopadmin/modifyaward';
	getList();
	
	function getList() {
		// requst back-end, get award list
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var awardList = data.awardList;
				var tempHtml = '';
				// concat the awards info, info include：
				// awardName，priority，enable\disable(include awardId)，edit(include awardId)
				// view(include awardId)
				awardList.map(function(item, index) {
					var textOp = "Disable";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// if status is 0，means disable award，action change to enable(click will enable the awards)
						textOp = "Enable";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// concat all awards info
					tempHtml += '' + '<div class="row row-award">'
							+ '<div class="col-33">'
							+ item.awardName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.point
							+ '</div>'
							+ '<div class="col-40">'
							+ '<a href="#" class="edit" data-id="'
							+ item.awardId
							+ '" data-status="'
							+ item.enableStatus
							+ '">Edit</a>'
							+ '<a href="#" class="delete" data-id="'
							+ item.awardId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.awardId
							+ '" data-status="'
							+ item.enableStatus
							+ '">View</a>'
							+ '</div>'
							+ '</div>';
				});
				// set the info to HTML
				$('.award-wrap').html(tempHtml);
			}
		});
	}
	
	// binding click action to award-wrap class
	$('.award-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							// if click edit will go to award edit page, and include awardId
							window.location.href = '/o2o/shopadmin/awardoperation?awardId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('delete')) {
							// if click delete will use the enable/disable function, and include productId
							changeItem(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							// if click preview will go to front-end display system show the detail of awards
							window.location.href = '/o2o/frontend/awarddetail?awardId='
									+ e.currentTarget.dataset.id;
						}
					});

	// binding add new action for add button
	$('#new').click(function() {
		window.location.href = '/o2o/shopadmin/awardoperation';
	});
	
	function changeItem(awardId, enableStatus) {
		// set award add json object to awardId and status(enable/disable)
		var award = {};
		award.awardId = awardId;
		award.enableStatus = enableStatus;
		$.confirm('confirm?', function() {
			// enable/disable awards
			$.ajax({
				url : changeUrl,
				type : 'POST',
				data : {
					awardStr : JSON.stringify(award),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('action success！');
						getList();
					} else {
						$.toast('action failed！');
					}
				}
			});
		});
	}
});