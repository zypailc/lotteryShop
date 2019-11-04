package com.didu.lotteryshop.auth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.didu.lotteryshop.common.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2019-10-23
 */
public interface MemberMapper extends BaseMapper<Member> {

    Member findByMemberName(@Param("email") String email);
}
