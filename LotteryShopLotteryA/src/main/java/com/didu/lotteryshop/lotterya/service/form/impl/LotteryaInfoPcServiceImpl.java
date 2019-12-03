package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfoPc;
import com.didu.lotteryshop.lotterya.mapper.LotteryaInfoPcMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaInfoPcService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * A彩票提成配置 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@Service
public class LotteryaInfoPcServiceImpl extends ServiceImpl<LotteryaInfoPcMapper, LotteryaInfoPc> implements ILotteryaInfoPcService {
    /**
     * 查询A彩票中奖提成配置
     * @param lotteryAInfoId
     * @return
     */
    public List<LotteryaInfoPc> findDraw(Integer lotteryAInfoId){
        return this.find(lotteryAInfoId,2);
    }

    /**
     * 查询A彩票购买提成配置
     * @param lotteryAInfoId
     * @return
     */
    public List<LotteryaInfoPc> findBuy(Integer lotteryAInfoId){
        return this.find(lotteryAInfoId,1);
    }

    /**
     * 查询A彩票提成配置
     * @param lotteryAInfoId
     * @param type
     * @return
     */
	public List<LotteryaInfoPc> find(Integer lotteryAInfoId,Integer type){
	    Wrapper<LotteryaInfoPc> wrapper = new EntityWrapper<>();
        wrapper.eq("lotterya_info_id",lotteryAInfoId)
                .and().eq("type",type)
        .orderBy("level",true);
        return super.selectList(wrapper);
    }
}
