package com.didu.lotteryshop.common.utils;

import org.apache.commons.lang.ArrayUtils;

public class ConfigurationUtil {

    //秘钥1
    public static final  String KEY_TOW = "b13828b542d244bf9b08e5c2caf95df8";

    //秘钥2
    public static final  String KEY_THREE = "9d949958cccd454a";

    //静态资源的文件名
    public static final  String []STATIC_RESOURCE_FILENAME = {"/plugins/","/js/","/lang/","/images/","/static/","/styles/"};

    public static void main(String [] args){
        String path = "/plugins";
        int index = path.indexOf("/",2);
        String path_1 = path.substring(0,index != -1 ? (index+1):path.length());
        if(index < 0){
            path_1 = path_1+"/";
        }
        System.out.println(ArrayUtils.toString(STATIC_RESOURCE_FILENAME, ",").indexOf(path_1));
    }

}
