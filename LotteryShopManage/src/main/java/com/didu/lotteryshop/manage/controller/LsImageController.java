package com.didu.lotteryshop.manage.controller;


import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.manage.service.LsImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2019-11-18
 */
@Controller
@RequestMapping("/lsImage")
public class LsImageController {

    @Autowired
    private LsImageService lsImageService;
    @Autowired
    private LsImageMapper lsImageMapper;

    @ResponseBody
    @RequestMapping("/getHeadImgList")
    public List<LsImage> getHeadImgList(String type){
        return lsImageService.getHeadImgList(type);
    }

    @ResponseBody
    @RequestMapping("/headImg")
    public ResultUtil headImg(HttpServletRequest request){
        return lsImageService.saveFileImg(request);
    }

    @ResponseBody
    @RequestMapping("/deleteHeadImg")
    public ResultUtil deleteHeadImg(String id){
        if(id != null && !"".equals(id)){
            return  lsImageService.deleteByid(id);
        }
        return ResultUtil.errorJson("error");
    }

    @ResponseBody
    @RequestMapping("/qRBackground")
    public ResultUtil qRBackground(HttpServletRequest request,String id){
        if(id == null || "".equals(id)){
            return ResultUtil.errorJson("error");
        }
        return lsImageService.saveFileQRBackground(request,id);
    }

    @ResponseBody
    @RequestMapping("/getQRBackgroundImg")
    public void getQRBackgroundImg(String id, HttpServletResponse response){
        LsImage lsImage = lsImageMapper.selectById(id);
        FileUtil.outImg(response,lsImage.getByteData());
    }

    @ResponseBody
    @RequestMapping("/getImg")
    public void getImg(String id,HttpServletResponse response){
        if(id != null && !"".equals(id)){
            LsImage lsImage = new LsImage();
            lsImage = lsImageMapper.selectById(Integer.parseInt(id));
            FileUtil.outImg(response,lsImage.getByteData());
        }
    }
}

