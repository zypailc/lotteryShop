package com.didu.lotteryshop.common.mapper;

import com.didu.lotteryshop.common.entity.EsGdethaccounts;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 推广分成eth账目流水记录 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2019-12-17
 */
public interface EsGdethaccountsMapper extends BaseMapper<EsGdethaccounts> {

    public List<Map<String, Object>> findGdethRecordPagination(Integer currentPage, Integer pageSize, String memberId, String startTime, String endTime);
}