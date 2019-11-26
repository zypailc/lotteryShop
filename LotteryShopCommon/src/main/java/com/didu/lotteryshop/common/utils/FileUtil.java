package com.didu.lotteryshop.common.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileUtil {

    public static final  String FILE_PATH = "E:\\LotteryShop2-1\\LotteryShopWebGateway\\src\\main\\resources\\static\\images\\head";

    public static final  String FILE_PATH_PARTNER = "E:\\LotteryShop2-1\\LotteryShopWebGateway\\src\\main\\resources\\static\\images\\partner";

    public static List<Map<String, Object>> fileUpload(HttpServletRequest request, String fileUrl) {
        List<Map<String,Object>> list = new ArrayList<>();
        //文件上传路径
        String savePath = fileUrl;
        File file = new File(savePath);
        if (!file.exists() && !file.isDirectory()) {
		    file.mkdir();
        }
        String fileName = null;
        try {
		    Collection<Part> parts = request.getParts();
		    if (parts.size() > 0) {
                for (Part part : parts) {
                    Map<String,Object> map = new HashMap<>();
                    String header = part.getHeader("content-disposition");
                    if(header.indexOf("filename") != -1) {
                        //util.IPTimeStamp newName = new util.IPTimeStamp(header);
                        //fileName =newName.getIPTimeRand() + getFileName(header);
                        fileName = System.currentTimeMillis() + "_" + getFileName(header) ;
                        map.put("fileName",fileName);
                        map.put("localhost",savePath + File.separator + fileName);
                        list.add(map);
                        part.write(savePath + File.separator + fileName);
                    }
                }
		    }
        } catch (IOException e) {
		    e.printStackTrace();
        } catch (ServletException e) {
		    e.printStackTrace();
        }
	    return list;
    }

    public static String getFileName(String header) {//这个方法通过header获取文件的名字
        String fileName = null;
        fileName = header.substring(header.indexOf("filename") + 2 + "filename".length(), header.length() - 1);
	    return fileName;
    }
}
