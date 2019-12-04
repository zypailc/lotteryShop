package com.didu.lotteryshop.common.service.form;

import com.baomidou.mybatisplus.service.IService;
import com.didu.lotteryshop.common.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2019-10-23
 */
public interface MemberService extends IService<Member>,UserDetailsService  {


}
