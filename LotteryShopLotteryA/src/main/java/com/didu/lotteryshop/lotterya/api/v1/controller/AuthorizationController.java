package com.didu.lotteryshop.lotterya.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotterya.api.v1.service.LotteryAService;
import com.didu.lotteryshop.lotterya.controller.LotteryABaseController;
import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 不过安全验证Controller
 * @author CHJ
 * @date 2019-12-10
 */
@Controller
@RequestMapping("/authorization/v1")
public class AuthorizationController extends LotteryABaseController {
    @Autowired
    private LotteryAService lotteryAService;

    /**
     * 获取彩票基本信息
     * @return
     */
    @RequestMapping("/getLotteryInfo")
    @ResponseBody
    public ResultUtil getLotteryInfo(){
        return lotteryAService.getLotteryInfo();
    }

    /**
     * 查詢彩票期數
     * @param currentPage 当前页
     * @param pageSize 分页总条数
     * @return
     */
    @RequestMapping("/getLotteryIssue")
    @ResponseBody
    public ResultUtil getLotteryIssue(Integer currentPage,Integer pageSize){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return lotteryAService.getLotteryIssue(currentPage,pageSize);
    }

    /**
     * 查询购买数据
     * @param currentPage
     * @param pageSize
     * @param isOneself 是否只查询自己 0：否 ；1：是
     * @param mTransferStatus 状态格式 :1,2
     * @param lotteryaBuy
     * @return
     */
    @RequestMapping("/getLotteryBuy")
    @ResponseBody
    public ResultUtil getLotteryBuy(Integer currentPage, Integer pageSize,Integer isOneself,String mTransferStatus, LotteryaBuy lotteryaBuy){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return lotteryAService.getLotteryBuy(currentPage,pageSize,isOneself,mTransferStatus,lotteryaBuy);
    }

}
