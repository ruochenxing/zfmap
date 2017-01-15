var myChart;
var domGraphic = document.getElementById('graphic');
var domMain = document.getElementById('main');
var needRefresh = false;
var BMapExtension;
var startPoint = {
    x: 113.93477574793,
    y: 22.532997146963
};
var echarts;
var isExampleLaunched;
require.config({
    paths: {
        echarts: './js'
    },
    packages: [
        {
            name: 'BMap',
            location: './js',
            main: 'main'
        }
    ]
});
function requireCallback (ec, defaultTheme) {
    refresh();
}
function focusGraphic() {
    domGraphic.className = 'col-md-8 ani';
    if (needRefresh) {
        myChart.showLoading();
        setTimeout(refresh, 1000);
    }
}
function getJson(type){
    $.ajax({  
        type : "get",  
        url :"servlet/getJson.do",  
        data : {'type':type},  
        dataType : "text",
        success : function(result) {
            result=jQuery.parseJSON(result);
            var g_length=result.g_list.length;
            var str="";
            if(g_length>0){
                str+="{";
                for(var i=0;i<g_length;i++){
                    if(i!=g_length-1){
                        str+='\"'+result.g_list[i].name+'\" : '+'['+result.g_list[i].lng+','+result.g_list[i].lat+'],';
                    }
                    else{
                        str+='\"'+result.g_list[i].name+'\" : '+'['+result.g_list[i].lng+','+result.g_list[i].lat+']';
                    }
                }
                str+="}"
            }
            var geoCoord=jQuery.parseJSON(str);
            showMap(geoCoord,result.p_list);
        }
    });     
}
function refresh(isBtnRefresh){
    require(['BMap'], function(mapEx){
        BMapExtension = mapEx;
        if (isBtnRefresh) {
            needRefresh = true;
            focusGraphic();
            return;
        }
        needRefresh = false;
        getJson("job");
    });
}
function needMap() {
    var href = location.href;
    return href.indexOf('Map') != -1
           || href.indexOf('mix3') != -1
           || href.indexOf('mix5') != -1
           || href.indexOf('dataRange') != -1;

}
function launchExample() {
    if (isExampleLaunched) {
        return;
    }
    // 按需加载
    isExampleLaunched = 1;
    require(
        [
            'echarts',
            'http://echarts.baidu.com/doc/example/theme/macarons',
            'echarts/chart/map'
        ],
        requireCallback
    );
}
function showMap(geoCoord,data){
    var BMapExt = new BMapExtension(domMain, BMap, require('echarts'), require('zrender'));
    var map = BMapExt.getMap();
    var container = BMapExt.getEchartsContainer();
    var point = new BMap.Point(startPoint.x, startPoint.y);
    map.centerAndZoom(point, 14);//设置中心点
    map.enableScrollWheelZoom(true);
    option = {
        color: ['gold','aqua','lime'],
        title : {//标题
            text: '深圳市',
            subtext:'数据纯属虚构',
            x:'right'
        },
        tooltip : {//提示框，鼠标悬浮交互时的信息提示
            trigger: 'item',//触发方式
            formatter: function (v) {
            	return "["+v[5].date+"]"+v[5].company+":"+v[5].title+"<br/>地址："+v[5].address+"<br/>薪酬："+v[5].salary+"<br/>";
            }
        },
        legend: {//图例，每个图表最多仅有一个图例
            orient: 'vertical',
            x:'left',
            data:['深圳'],
            selectedMode: 'single',
            selected:{
            }
        },
        toolbox: {//工具箱
            show : false,//是否显示
            orient : 'vertical',//horizontal
            x: 'left',
            y: 100,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        dataRange: {//值域选择，每个图表最多仅有一个值域控件
        	min : 1,
            max : 50,
            x:1,
            y:50,
            calculable : true,
            color: ['#ff3333', 'orange', 'yellow','lime','aqua']
        },
//        dataRange: {//值域选择，每个图表最多仅有一个值域控件
//            x:0,
//            y:50,
//            splitList: [
//                {start: 30,label:"30k以上"},
//                {start: 20, end: 30,label:"20k-30k"},
//                {start: 15, end: 20,label:"15k-20k"},
//                {start: 10, end: 15,label:"10k-15k"},
//                {start: 3, end: 10,label:"3k-10k"},
//                {end: 3,label:"3k以下"}
//            ],
//            color: ['#ff3333', 'orange', 'yellow','lime','aqua']
//        },
        series : [
            {
                name:'深圳市',
                type:'map',
                mapType: 'none',
                data:[],
                geoCoord:geoCoord,
                markPoint : {
                    symbol:'emptyCircle',
                    symbolSize : function (v){
                        return 10 + v/10
                    },
                    effect : {
                        show: true,
                        shadowBlur : 0
                    },
                    itemStyle:{
                        normal:{
                            label:{show:false}
                        }
                    },
                    data : data
                }
            }
        ]
    };
    if (myChart && myChart.dispose) {
        myChart.dispose();
    }
    myChart = BMapExt.initECharts(container);
    window.onresize = myChart.resize;
    // 添加点击事件
    var ecConfig = require('echarts/config');  
    myChart.on(ecConfig.EVENT.CLICK, eConsole); 
    
    BMapExt.setOption(option, true);
}
function eConsole(param) {
	if (typeof param.seriesIndex == 'undefined') {
		return;
	}
	if (param.type == 'click') {
		window.open(param.data.url);
	}
}
launchExample();