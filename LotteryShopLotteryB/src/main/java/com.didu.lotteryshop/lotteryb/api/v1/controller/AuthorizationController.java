package com.didu.lotteryshop.lotteryb.api.v1.controller;

import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 不过验证查询B类彩票的信息
 */
@Controller
@RequestMapping("/authorization/v1")
public class AuthorizationController {

    @Autowired
    private LotterybInfoServiceImpl lotterybInfoService;

    /**
     * 查询B类彩票的所有玩法
     * @return
     */
    public List<LotterybInfo> findLotterybAllInfo(){
        return lotterybInfoService.findLotterybAllInfo();
    }

}
