package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.mapper.LotterybInfoMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-20
 */
@Service
public class LotterybInfoServiceImpl extends ServiceImpl<LotterybInfoMapper, LotterybInfo> implements ILotterybInfoService {

    /**
     * 玩法 竞猜-B1
     */
    public static final String TYPE_ID_1 = "1";
    /**
     * 玩法 竞猜-B3
     */
    public static final String TYPE_ID_3 = "2";
    /**
     * 玩法 竞猜-B5
     */
    public static final String TYPE_ID_5 = "3";


}
