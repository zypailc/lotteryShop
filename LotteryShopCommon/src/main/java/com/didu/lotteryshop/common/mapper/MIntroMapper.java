package com.didu.lotteryshop.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.didu.lotteryshop.common.entity.MIntro;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2019-11-21
 */
public interface MIntroMapper extends BaseMapper<MIntro> {

    public List<Map<String,Object>> selectMIntro(@Param("type") String type);

}
