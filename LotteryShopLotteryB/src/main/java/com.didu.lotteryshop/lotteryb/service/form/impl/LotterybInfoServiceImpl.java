package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybInfo;
import com.didu.lotteryshop.lotteryb.mapper.LotterybInfoMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public static final Integer TYPE_ID_1 = 1;
    /**
     * 玩法 竞猜-B3
     */
    public static final Integer TYPE_ID_3 = 2;
    /**
     * 玩法 竞猜-B5
     */
    public static final Integer TYPE_ID_5 = 3;

    /**
     * 根据玩法id查询
     * @param lotterybInfoId
     * @return
     */
    public LotterybInfo find(Integer lotterybInfoId){
        return super.selectById(lotterybInfoId);
    }

    /**
     * 查询所有玩法
     * @return
     */
    public List<LotterybInfo> findLotterybAllInfo() {
        Wrapper<LotterybInfo> wrapper = new EntityWrapper<>();
        wrapper.orderBy("id",true);
        return super.selectList(wrapper);
    }
}
