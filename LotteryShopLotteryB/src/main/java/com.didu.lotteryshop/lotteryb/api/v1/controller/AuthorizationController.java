package com.didu.lotteryshop.lotteryb.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.api.v1.service.LotteryBService;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.service.LotterybIssueService;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybConfigServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 不过验证查询B类彩票的信息
 */
@Controller
@RequestMapping("/authorization/v1")
public class AuthorizationController {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;
    @Autowired
    private LotteryBService lotteryBService;
    @Autowired
    private LotterybConfigServiceImpl lotterybConfigService;
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueServiceIml;
    @Autowired
    private LotterybIssueService lotterybIssueService;

    /**
     * 查询B类彩票的所有玩法
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAll")
    public List<LotterybInfo> findLotterybAllInfo(){
        return lotterybInfoService.findLotterybAllInfo();
    }


    /**
     * 查询玩法类型
     * @param lotterybInfoId
     * @return
     */
    @ResponseBody
    @RequestMapping("/findLotterybInfo")
    public ResultUtil findLotterybIssueUp(Integer lotterybInfoId){
        return lotteryBService.getLotteryBInfo(lotterybInfoId);
    }

    /**
     * 查询上期的开奖信息
     * @param lotterybInfoId
     * @return
     */
    @ResponseBody
    @RequestMapping("/findLotterybIssueUp")
    public ResultUtil findLotterybInfoUp(Integer lotterybInfoId){
        return ResultUtil.successJson(lotterybIssueServiceIml.findUpLotteryaIssue(lotterybInfoId));
    }



    /**
     * 获取玩法配置信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/findLotterybConfig")
    public ResultUtil findLotterybConfig(Integer type){
        return ResultUtil.successJson(lotterybConfigService.getConfig(type));
    }

    /**
     * 根据玩法Id 查询期数表信息
     * @param currentPage
     * @param pageSize
     * @param lotterybInfoId 玩法Id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getLotteryIssue")
    public ResultUtil getLotteryIssue(Integer currentPage,Integer pageSize,Integer lotterybInfoId){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return  lotterybIssueService.getLotteryIssue(currentPage,pageSize,lotterybInfoId);
    }

    /**
     * 查询购买数据
     * @param currentPage
     * @param pageSize
     * @param isOneself 是否只查询自己 0：否 ；1：是
     * @param mTransferStatus 状态格式 :1,2
     * @param lotterybBuy
     * @param type
     * @return
     */
    @RequestMapping("/getLotteryBuy")
    @ResponseBody
    public ResultUtil getLotteryBuy(Integer currentPage, Integer pageSize, Integer isOneself, String mTransferStatus, LotterybBuy lotterybBuy, Integer type){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        type = type == null ? 1:type;
        return lotteryBService.getLotteryBuy(currentPage,pageSize,isOneself,mTransferStatus,lotterybBuy,type);
    }

}
