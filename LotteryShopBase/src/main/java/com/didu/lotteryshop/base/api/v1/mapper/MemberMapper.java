package com.didu.lotteryshop.base.api.v1.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.didu.lotteryshop.common.entity.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2019-10-23
 */
public interface MemberMapper extends BaseMapper<Member> {

    Member findByMemberName(String memberName);

}
