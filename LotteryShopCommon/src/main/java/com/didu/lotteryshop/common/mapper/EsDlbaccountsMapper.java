package com.didu.lotteryshop.common.mapper;

import com.didu.lotteryshop.common.entity.EsDlbaccounts;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 待领币平台账目流水记录 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
public interface EsDlbaccountsMapper extends BaseMapper<EsDlbaccounts> {

    public List<Map<String,Object>> findDlbRecordPagination(Integer currentPage, Integer pageSize, String memberId, String startTime, String endTime, String statuss);
}