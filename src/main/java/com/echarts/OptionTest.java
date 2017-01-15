package com.echarts;

import java.util.ArrayList;
import java.util.List;

/*
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.series.Map;
import com.github.abel533.echarts.json.GsonOption;
*/
import com.google.gson.Gson;
//mvn exec:java -Dexec.mainClass="com.echartsdemo.OptionTest"
public class OptionTest {
        public static String getJson1(){
            Gson gson = new Gson();
            java.util.Map<String,Object> result=new java.util.HashMap<String,Object>();
            List<AddressInfo> g_list=fillAddressInfo();
            result.put("g_list",g_list);
            result.put("p_list",fillPoint(g_list));
            String str = gson.toJson(result);
            return str;
        }
        public static List<AddressInfo> fillAddressInfo(){
            List<AddressInfo> g_list=new ArrayList<AddressInfo>();
            g_list.add(BaiduUtil.getAddressInfo("深圳市南山区荟芳园"));
            g_list.add(BaiduUtil.getAddressInfo("上海"));
            g_list.add(BaiduUtil.getAddressInfo("东莞"));
            return g_list;
        }
        public static List<MarkPointData> fillPoint(List<AddressInfo> g_list){
            List<MarkPointData> p_list=new ArrayList<MarkPointData>();
            for(AddressInfo a:g_list){
                MarkPointData point1=new MarkPointData();
                point1.name=a.getAddress();
                point1.value=90;
                p_list.add(point1);
            }
            return p_list;
        }
        /*
        public static String getJson(){
                // 地址：http://echarts.baidu.com/doc/example/map.html
                GsonOption option = new GsonOption();
                option.color("glod");
                option.title().text("数据迁移").subtext("数据纯属虚构").x(X.right);
                option.tooltip().trigger(Trigger.item).formatter("function (v) {return v[1].replace(':', ' > ');}");
                option.legend().orient(Orient.vertical).x(X.left).data("广东").selectedMode("single");
                option.toolbox().show(true).orient(Orient.vertical).x("right").y("center");
                option.dataRange().min(0).max(100).y("60%").calculable(false);
                Map map = new Map("深圳");
                map.type(SeriesType.map).mapType("none");
                map.geoCoord("荟芳园", "113.93477574793", "22.532997146963");
                map.markPoint().symbol("emptyCircle").effect().show(true).shadowBlur(0);
                map.markPoint().data(new Data("荟芳园", 100));
                option.series(map);
                return option.toString();
        }*/
}