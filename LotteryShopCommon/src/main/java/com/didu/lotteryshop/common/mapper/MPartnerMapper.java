package com.didu.lotteryshop.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.didu.lotteryshop.common.entity.MPartner;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2019-11-19
 */
public interface MPartnerMapper extends BaseMapper<MPartner> {

    List<Map<String,Object>> findPartnerList(@Param("type") String type);

}
