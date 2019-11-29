package com.didu.lotteryshop.lotterya.service.form.impl;

import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.mapper.LotteryaInfoMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * A彩票基础信息 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@Service
public class LotteryaInfoServiceImpl extends ServiceImpl<LotteryaInfoMapper, LotteryaInfo> implements ILotteryaInfoService {
    @Autowired
    private LotteryaInfoMapper lotteryaInfoMapper;

    /**
     * 查询A彩票基本信息
     * @return
     */
    public LotteryaInfo findLotteryaInfo(){
        return lotteryaInfoMapper.selectById(1);
    }
    /**
     * 计算A彩票单笔奖金
     * @return 返回单注奖金
     */
    public BigDecimal calculateSingleBonus(){
        return this.calculateSingleBonus(this.findLotteryaInfo());
    }
    /**
     * 计算A彩票单笔奖金
     * @param lotteryaInfo A彩票基本信息
     * @return 返回单注奖金
     */
    public BigDecimal calculateSingleBonus(LotteryaInfo lotteryaInfo){
        BigDecimal pTotal = BigDecimal.ZERO;
        if(lotteryaInfo != null && lotteryaInfo.getPrice() != null){
            //单注奖金，公式：(price*1000)/100*50
            pTotal =  lotteryaInfo.getPrice().multiply(new BigDecimal("1000"))
                    .divide(new BigDecimal("100"))
                    .multiply(new BigDecimal("50"));
        }
        return pTotal;
    }
	
}
