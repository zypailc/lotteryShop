package com.didu.lotteryshop.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.didu.lotteryshop.common.entity.LsImage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2019-11-18
 */
public interface LsImageMapper extends BaseMapper<LsImage> {

    @Select(value = "select * from ls_image where type = #{type}")
    public List<Map<String,Object>> findTest(@Param("type") String type);

}
