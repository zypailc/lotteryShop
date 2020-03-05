package com.didu.lotteryshop.lotteryb.api.v1.controller;

import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.lotteryb.api.v1.service.LotteryBService;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybConfigServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
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
    public ResultUtil findLotterybInfo(Integer lotterybInfoId){
        return lotteryBService.getLotteryBInfo(lotterybInfoId);
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

}
