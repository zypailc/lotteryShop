package com.didu.lotteryshop.common.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
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

    private static String getFileName(String header) {//这个方法通过header获取文件的名字
        String fileName = null;
        fileName = header.substring(header.indexOf("filename") + 2 + "filename".length(), header.length() - 1);
	    return fileName;
    }

    /**
     * 根据请求头，获取文件的二进制
     * @param request
     * @return
     */
    public static byte[] getFileByte(HttpServletRequest request){
        InputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            Collection<Part> parts = request.getParts();
            if (parts.size() > 0) {
                for (Part part : parts) {
                    byte[] buffer = part(part);
                    return buffer;
                }
            }
        } catch (ServletException e) {
            e.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
         return null;
    }

    private static byte[] part(Part part){
        InputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            byte[] buffer = null;
            //获取文件流
            in = part.getInputStream();
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null){
                    in.close();
                }
                if(bos != null){
                    bos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<byte[]> getFileByteList(HttpServletRequest request){
        List<byte[]> list = new ArrayList<>();
        InputStream in = null;
        ByteArrayOutputStream bos = null;
        String fileName = "";
        try {
            if(request.getParts() != null) {
                Collection<Part> parts = request.getParts();
                if (parts.size() > 0) {
                    for (Part part : parts) {
                        String header = part.getHeader("content-disposition");
                        if (header.indexOf("filename") != -1) {
                            fileName = getFileName(header);
                            if (fileName != null && !"".equals(fileName)) {
                                byte[] buffer = part(part);
                                list.add(buffer);
                            }
                        }
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

    /**
     * 二进制转流
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 向前台输出图片流
     * @param response
     * @param img
     */
    public static void outImg(HttpServletResponse response,byte [] img){
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(response.getOutputStream());
            out.write(img);
            out.flush();
        }catch (IOException io){
            io.printStackTrace();
        }finally {
            try {
                out.close();
            }catch (IOException io){
                io.printStackTrace();
            }
        }
    }

    /**
     * 根据地址删除文件
     * @param fileUrl
     */
    public static void deleteFile(String fileUrl){
        File file = new File(fileUrl);
        if (file.isFile()) {
            file.delete();
        }
    }

    public static void main(String [] args){
        deleteFile("D:\\aq.png");
    }

}
