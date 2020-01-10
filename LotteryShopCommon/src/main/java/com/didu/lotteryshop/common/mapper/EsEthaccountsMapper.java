package com.didu.lotteryshop.common.mapper;

import com.didu.lotteryshop.common.entity.EsEthaccounts;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * eth账目流水记录 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
public interface EsEthaccountsMapper extends BaseMapper<EsEthaccounts> {

    public List<Map<String,Object>> findEthRecordPagination(Integer currentPage, Integer pageSize, String startTime, String endTime, String memberId, String status);
}