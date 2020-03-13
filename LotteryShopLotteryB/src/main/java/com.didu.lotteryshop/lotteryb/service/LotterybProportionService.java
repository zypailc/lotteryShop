package com.didu.lotteryshop.lotteryb.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybProportion;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybProportionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotterybProportionService extends LotteryBBaseService {

    @Autowired
    private LotterybProportionServiceImpl lotterybProportionService;

    /**
     * 根据上次返奖生成本次返奖信息
     * @param lotterybProportionId
     * @return
     */
    public LotterybProportion getProportionNext(Integer lotterybProportionId){
        LotterybProportion lotterybProportion = lotterybProportionService.selectById(lotterybProportionId);
        if(lotterybProportion != null){
            Integer rule = lotterybProportion.getRule() + 1;
            Wrapper<LotterybProportion> wrapper = new EntityWrapper<>();
            wrapper.and().eq("rule",rule);
            List<LotterybProportion> list = lotterybProportionService.selectList(wrapper);
            if(!list.isEmpty() && list.size() > 0){
                return  list.get(0);
            }else {
                return lotterybProportionService.selectById(1);
            }

        }else {
            return lotterybProportionService.selectById(1);
        }
    }

}
