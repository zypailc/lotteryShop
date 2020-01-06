package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.GdethService;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("v1/gdethWallet")
public class GdethController {

    @Autowired
    private GdethService gdethService;


    /**
     * 查询顶级推广商的利益分红
     * @return
     */
    @ResponseBody
    @RequestMapping("/findGdethWallet")
    public ResultUtil findGdethWallet(){
        return gdethService.findGdEthWallet();
    }

    /**
     * 查询顶级推广商的分红记录
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    @ResponseBody
    @RequestMapping("/findGdethRecord")
    public ResultUtil findGdethRecord(Integer currentPage,Integer pageSize,String startTime,String endTime){
        return gdethService.findGdEthRecord(currentPage,pageSize,startTime,endTime);
    }


}
