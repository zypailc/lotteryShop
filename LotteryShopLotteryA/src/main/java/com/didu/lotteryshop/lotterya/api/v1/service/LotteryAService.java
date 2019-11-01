package com.didu.lotteryshop.lotterya.api.v1.service;

import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.stereotype.Service;

/**
 **
  * A彩票 Service
  * @author CHJ
  * @date 2019-11-01 14:09
  */
@Service
public class LotteryAService {

    /**
     *
     * @param payPasswod 支付密码
     * @return
     */
    public ResultUtil ethBuyLottery(String payPasswod){
        //step 1 获取当前用户
        //step 2 判断金额是否充足
        //step 3 购买记录，进行转账
        //step 4 修改金额，冻结金额
        //step 5 返回成功
        return null;
    }
}
