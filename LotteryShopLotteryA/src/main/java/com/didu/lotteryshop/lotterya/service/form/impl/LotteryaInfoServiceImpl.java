package com.didu.lotteryshop.lotterya.service.form.impl;

import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.mapper.LotteryaInfoMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
}
