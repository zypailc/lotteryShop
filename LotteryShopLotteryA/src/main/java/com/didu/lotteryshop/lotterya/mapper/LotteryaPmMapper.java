package com.didu.lotteryshop.lotterya.mapper;

import com.didu.lotteryshop.lotterya.entity.LotteryaPm;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * A彩票提成 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
public interface LotteryaPmMapper extends BaseMapper<LotteryaPm> {

    /**
     *根据用户查询未领取待领币统计
     * @return
     */
    public Map<String,Object> findLotteryAIssueReceiveStatistics(String memberId,Integer status);

    /**
     * 根据用户查询每期可领取的待领币的列表
     * @param memberId
     * @return
     */
    public List<Map<String,Object>> findLotteryAIssueReceive(String memberId,Integer currentPage, Integer pageSize,String status);

}