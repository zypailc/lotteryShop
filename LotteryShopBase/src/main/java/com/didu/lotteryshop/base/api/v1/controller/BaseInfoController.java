package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.BaseInfoService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取页面的基本信息
 */
@Controller
@RequestMapping("/authorization/v1/baseInfo")
public class BaseInfoController extends BaseBaseController {


    @Autowired
    public BaseInfoService baseInfoService;

    /**
     * 查询项目白皮书
     * @param languageType
     * @return
     */
    @ResponseBody
    @RequestMapping("/infoContentWhiteBook")
    public List<Map<String,Object>> infoContentWhiteBook(String languageType){
        if(languageType == null || "".equals(languageType)){
            return null;
        }
        return baseInfoService.infoContentWhiteBook(languageType);
    }

    /**
     * 查询项目特点
     * @param languageType
     * @return
     */
    @ResponseBody
    @RequestMapping("/infoContentCharacteristic")
    public List<Map<String,Object>> infoContentCharacteristic(String languageType){
        if(languageType == null || "".equals(languageType)){
            return null;
        }
        return  baseInfoService.infoContentCharacteristic(languageType);
    }

    /**
     * 查询合作伙伴
     * @return
     */
    @ResponseBody
    @RequestMapping("/infoContentPartners")
    public List<Map<String,Object>> infoContentPartners(){
        return  baseInfoService.infoContentPartners();
    }

    /**
     * 查询充值途径
     * @return
     */
    @ResponseBody
    @RequestMapping("/infoContentExternal")
    public List<Map<String,Object>> infoContentExternal(){
        return  baseInfoService.infoContentExternal();
    }

    /**
     * 查詢玩法規則
     * @param playType
     * @param languageType
     * @return
     */
    @ResponseBody
    @RequestMapping("/findPlayTypeRule")
    public List<Map<String,Object>> findPlayTypeRule(String playType,String languageType){
        if(playType == null || "".equals(playType)){
            return null;
        }
        if(languageType == null || "".equals(languageType)){
            return null;
        }
        return  baseInfoService.findPlayTypeRule(playType,languageType);
    }

    /**
     * 獲取配置數據
     * @return
     */
    @ResponseBody
    @RequestMapping("/findPlayConfig")
    public Map<String,Object> findPlayConfig(){
        Map<String,Object> map = new HashMap<>();
        BigDecimal gas = baseInfoService.findPlayConfig();
        map.put("gas",gas);
        return map;
    }

}
