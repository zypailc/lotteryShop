package com.didu.lotteryshop.common.filter.util;

import com.didu.lotteryshop.common.config.Constants;
import org.apache.commons.lang.ArrayUtils;

public class FilterUtil {

    public static String path(String path){
        int index = path.indexOf("/",2);
        String path_1 = path.substring(0,index != -1 ? (index+1):path.length());
        if(index < 0){
            return path_1 = path_1+"/";
        }
        return path_1;
    }

    public static String path_s(){
        return ArrayUtils.toString(Constants.STATIC_RESOURCE_FILENAME, ",");
    }

}
