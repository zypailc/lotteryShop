package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.*;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.ObjectToMapUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GdethService extends BaseBaseService {

    @Autowired
    private EsGdethwalletServiceImpl esGdethwalletService;
    @Autowired
    private EsGdethaccountsServiceImpl esGdethaccountsService;
    @Autowired
    private EsGdethconfigWithdrawServiceImpl esGdethconfigWithdrawService;
    @Autowired
    private EsGdethconfigServiceImpl esGdethconfigService;


    /**
     * 查询顶级推广商利益分红
     * @return
     */
    public ResultUtil findGdEthWallet(){
        LoginUser loginUser = getLoginUser();
        EsGdethwallet esGdethwallet = esGdethwalletService.findByMemberId(loginUser.getId());
        if(esGdethwallet != null){
            esGdethwallet.setTotal(BigDecimalUtil.bigDecimalToPrecision(esGdethwallet.getTotal()));
            esGdethwallet.setBalance(BigDecimalUtil.bigDecimalToPrecision(esGdethwallet.getBalance()));
            esGdethwallet.setFreeze(BigDecimalUtil.bigDecimalToPrecision(esGdethwallet.getFreeze()));
        }else {
            esGdethwallet = new EsGdethwallet();
            esGdethwallet.setTotal(new BigDecimal("0"));
            esGdethwallet.setBalance(new BigDecimal("0"));
            esGdethwallet.setFreeze(new BigDecimal("0"));
        }
        Map<String,Object> map = new HashMap<>();
        try {
            map = ObjectToMapUtil.objectToMap(esGdethwallet);
        }catch (Exception e){
            e.printStackTrace();
        }
        EsGdethconfig esGdethconfig = esGdethconfigService.findEsGdethconfig();
        Wrapper<EsGdethconfigWithdraw> wrapper = new EntityWrapper<>();
        wrapper.orderBy("active_num",false);
        List<EsGdethconfigWithdraw> list = esGdethconfigWithdrawService.selectList(wrapper);
        map.put("level",esGdethconfig.getcLevel());
        map.put("eth",esGdethconfig.getConsumeTotal());
        map.put("day1",esGdethconfig.getCycleDay());
        String listStr = "";
        for (int i = 0; i < list.size(); i++){
            if(list.size() == (i+1)){
                listStr += ">"+list.get(i).getActiveNum()+"/"+list.get(i).getActiveNum()+"。";
            }else if(i == 0){
                listStr += "0 ~ "+list.get(i).getActiveNum()+"/"+list.get(i).getActiveNum()+"、";
            }else {
                listStr += (list.get(i).getActiveNum()-1)+ " ~ "+list.get(i).getActiveNum()+"/"+list.get(i).getActiveNum()+"、";
            }
        }
        map.put("list",listStr);
        return ResultUtil.successJson(map);
    }

    /**
     * 查询顶级推广商的推广分红记录
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public ResultUtil findGdEthRecord(Integer currentPage,Integer pageSize,String startTime,String endTime){
        LoginUser loginUser = getLoginUser();
        return ResultUtil.successJson(esGdethaccountsService.findGdethRecordPagination(loginUser.getId(),currentPage,pageSize,startTime,endTime));
    }

}
