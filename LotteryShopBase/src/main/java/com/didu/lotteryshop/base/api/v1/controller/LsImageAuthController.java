package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/authorization/v1/lsimage")
public class LsImageAuthController {

    @Autowired
    private LsImageMapper lsImageMapper;

    @ResponseBody
    @RequestMapping("/getImg")
    public void getImg(String id, HttpServletResponse response){
        if(id != null && !"".equals(id)){
            LsImage lsImage = new LsImage();
            lsImage = lsImageMapper.selectById(Integer.parseInt(id));
            FileUtil.outImg(response,lsImage.getByteData());
        }
    }

}
