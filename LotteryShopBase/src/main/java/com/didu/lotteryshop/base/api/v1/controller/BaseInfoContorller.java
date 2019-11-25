package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.BaseInfoService;
import com.didu.lotteryshop.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 获取页面的基本信息
 */
@Controller
@RequestMapping("/authorization/v1/baseInfo")
public class BaseInfoContorller extends BaseController {


    @Autowired
    public BaseInfoService baseInfoService;

    @ResponseBody
    @RequestMapping("/infoContent")
    public Map<String,Object> indexInfo(String languageType){
        return  baseInfoService.indexInfo(languageType);
    }


}
