package com.didu.lotteryshop.manage.controller;


import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.manage.service.MPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2019-11-19
 */
@Controller
@RequestMapping("/mPartner")
public class MPartnerController {

    @Autowired
    private MPartnerService mPartnerService;

    @ResponseBody
    @RequestMapping("/getPartnerList")
    public List<Map<String,Object>> getPartnerList(){
        return  mPartnerService.getPartnerList();
    }

    @ResponseBody
    @RequestMapping("/getExternalList")
    public List<Map<String,Object>> getExternalList(){
        return mPartnerService.getExternalList();
    }

    @ResponseBody
    @RequestMapping("/savePartner")
    public ResultUtil savePartner(String linkAddress, HttpServletRequest request){
        return mPartnerService.savePartner(linkAddress,request);
    }

    @ResponseBody
    @RequestMapping("/saveExternal")
    public ResultUtil saveExternal(String linkAddress, HttpServletRequest request){
        return mPartnerService.saveExternal(linkAddress,request);
    }

    @ResponseBody
    @RequestMapping("/updateSort")
    public ResultUtil updateSort(String id,String sort){
        return mPartnerService.updateSort(id,sort);
    }

    @ResponseBody
    @RequestMapping("/deleteMPartner")
    public ResultUtil deleteMPartner(String id){
        return mPartnerService.deleteMPartner(id);
    }

}

