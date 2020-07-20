/**
 * 
 */
$(function() {
	var productName = '';
	getProductSellDailyList();
	getList();
	
	function getList() {
		// get user purchase info URL
		var listUrl = '/o2o/shopadmin/listuserproductmapbyshop?pageIndex=1&pageSize=9999&productName=' + productName;
		
		// request back-end, get purchase info list
		$.getJSON(listUrl, function(data) {
			if(data.success) {
				var userProductMapList = data.userProductMapList;
				var tempHtml = '';
				
				// for loop the purchase info list, concat the list info
				userProductMapList.map(function(item, index) {
					tempHtml += '<div class="row row-productbuycheck">'
						+ '<div class="col-10">' 
						+ item.product.productName
						+ '</div>'
						+ '<div class="col-40 productbuycheck-time">'
						+ new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss")
						+ '</div>' 
						+ '<div class="col-20">'
						+ item.user.name 
						+ '</div>'
						+ '<div class="col-10">' 
						+ item.point 
						+ '</div>'
						+ '<div class="col-20">' 
						+ item.operator.name
						+ '</div>' 
						+ '</div>';
				});
				$('.productbuycheck-wrap').html(tempHtml);
			}	
		});
	}
	
	$('#search').on('change', function(e) {
		// when input keyword in search bar
		// it will use the keyword to do fuzzy search
		productName = e.target.value;
		$('.productbuycheck-wrap').empty();
		// reload
		getList();
	});
	
	/**
	 * get a weeks sells
	 */
	function getProductSellDailyList() {
		// get the shop products sells in a week URL
		var listProductSellDailyUrl = '/o2o/shopadmin/listproductselldailyinfobyshop';
		// request back-end, products sells
		$.getJSON(listProductSellDailyUrl, function(data) {
			if(data.success) {
				var myChart = echarts.init(document.getElementById('chart'));
				// generate static Echart data 
				var option = generateStaticEchartPart();
				option.legend.data = data.legendData;
				option.xAxis = data.xAxis;
				option.series = data.series;
				myChart.setOption(option);
			}
		});
	}
	
	/**
	 * generate static Echart data
	 */
	function generateStaticEchartPart() {
		/** echarts logic part **/
		var option= {
				// pop up, tip the info when touch and interacting
				tooltip : {
					trigger : 'axis',
					axisPointer :{ // axis pointer
						type : 'shadow' // shadow when interacting
					}
				},
				// chart, every charter at most have one char image
				legend: {},
				// grid image
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				// x-axis label
				xAxis : [{}],
				//yAxis label
				yAxis : [{
					type : 'value'
				}],
				// image generate data every series and the data
				series : [{}]
			};
			return option;
	}
	
	
});