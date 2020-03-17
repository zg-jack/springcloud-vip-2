package com.xiangxue.jack.dynamicDataSource;

import java.util.ArrayList;
import java.util.List;

/** 
 *  往期视频加瑶瑶老师QQ：2483034688
 *  Jack老师QQ： 2943489129
 *  时间   ：     2018年6月14日 下午9:13:45 
 *  作者   ：   烛光学院【Jack老师】
 *  
 *  建立映射关系，建立当前用户操作线程和数据源标识的映射关系
 */
public class DynamicDataSourceContextHolder {
    
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static ThreadLocal<String> getContextHolder() {
        return contextHolder;
    }

    public static List<String> dataSourceIds = new ArrayList<String>();
    
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }
    
    public static String getDataSourceType() {
        return contextHolder.get();
    }
    
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
