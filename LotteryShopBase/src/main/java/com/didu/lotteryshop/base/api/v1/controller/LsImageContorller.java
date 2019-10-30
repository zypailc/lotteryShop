package com.didu.lotteryshop.base.api.v1.controller;


import com.didu.lotteryshop.base.api.v1.service.imp.LsImageServiceImpl;
import com.didu.lotteryshop.common.entity.LsImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 图片操作Contorller
 */
@Controller
@RequestMapping("/v1/authorization/lsimage")
public class LsImageContorller {

    @Autowired
    private LsImageServiceImpl imageService;

    /**
     * 根据图片类型获取图片
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping("/imageType")
    public List<LsImage> findImageType(Integer type){
        return imageService.findImageType(type);
    }

}
