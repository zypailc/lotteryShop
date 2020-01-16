package com.didu.lotteryshop.lotterya.mapper;

import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * A彩票购买记录 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
public interface LotteryaBuyMapper extends BaseMapper<LotteryaBuy> {

    /**
     * 查询每期中奖的记录
     * @param currentPage
     * @param pageSize
     * @param mTransferStatus
     * @param lotteryaBuy
     * @return
     */
    public List<Map<String,Object>> getPageLotteryBuyAll(Integer currentPage, Integer pageSize,String mTransferStatus,LotteryaBuy lotteryaBuy);


}