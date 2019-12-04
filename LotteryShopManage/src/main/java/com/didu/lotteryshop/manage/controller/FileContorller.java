package com.didu.lotteryshop.manage.controller;

import com.didu.lotteryshop.common.utils.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/file")
public class FileContorller {

    @ResponseBody
    @RequestMapping("/fileImg")
    public void fileImg(String fileName, String type, HttpServletResponse response){
        // 这个是我的本地文件所在的路径。 修改成自己的路径
        String pathname = "";
        if(type != null && "head".equals(type)){
            pathname = FileUtil.FILE_PATH + "\\" + fileName;
        }else if(type != null && "partner".equals(type)){
            pathname = FileUtil.FILE_PATH_PARTNER + "\\" + fileName;
        }
        if(fileName == null || "".equals(fileName)){
            return;
        }
        File file = new File(pathname);
        try {
            ServletOutputStream out = response.getOutputStream();
            InputStream in = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len=in.read(buffer))>0){
                out.write(buffer,0,len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
