package com.didu.lotteryshop.common.mapper;

import com.didu.lotteryshop.common.entity.EsLsbaccounts;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * lsb平台账目流水记录 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
public interface EsLsbaccountsMapper extends BaseMapper<EsLsbaccounts> {

    public List<Map<String,Object>> findLsbRecordPagination(Integer currentPage, Integer pageSize, String memberId, String startTime, String endTime, String status);
}