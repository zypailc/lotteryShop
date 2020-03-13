package com.didu.lotteryshop.lotteryb.mapper;

import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-02-18
 */
public interface LotterybBuyMapper extends BaseMapper<LotterybBuy> {

    public List<Map<String,Object>> getPageLotteryBuyAll(Integer currentPage, Integer pageSize, String mTransferStatus, LotterybBuy lotterybBuy);
}