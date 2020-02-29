package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfoPc;
import com.didu.lotteryshop.lotteryb.mapper.LotterybInfoPcMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybInfoPcService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-29
 */
@Service
public class LotterybInfoPcServiceImpl extends ServiceImpl<LotterybInfoPcMapper, LotterybInfoPc> implements ILotterybInfoPcService {


    /**
     * 购买提成
     */
    public static  final Integer TYPE_BUY = 1;

    /**
     * 中奖提成
     */
    public static  final Integer TYPE_WINNING = 2;

    /**
     * 查询购买提成比例
     * @param lotterybInfoId
     * @return
     */
    public List<LotterybInfoPc> findBuy(Integer lotterybInfoId){
        return this.find(lotterybInfoId,LotterybInfoPcServiceImpl.TYPE_BUY);
    }

    /**
     * 查询中奖提成比例
     * @param lotterybInfoId
     * @return
     */
    public List<LotterybInfoPc> findWinning(Integer lotterybInfoId){
        return this.find(lotterybInfoId,LotterybInfoPcServiceImpl.TYPE_WINNING);
    }

    /**
     * 根据竞猜玩法和类型查询提成比例
     * @param lotterybInfoId
     * @param type
     * @return
     */
    private List<LotterybInfoPc> find(Integer lotterybInfoId, Integer type){
        Wrapper<LotterybInfoPc> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId).eq("type",type);
        wrapper.orderBy("level",true);
        return super.selectList(wrapper);
    }

}
