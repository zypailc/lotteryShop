package com.didu.lotteryshop.auth.service.impl;

import com.didu.lotteryshop.auth.mapper.MemberMapper;
import com.didu.lotteryshop.auth.service.MemberService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.entity.Permission;
import com.didu.lotteryshop.common.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2019-10-23
 */
@Service("userDetailService")
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {

        Member member = memberMapper.findByMemberName(memberName);

        if (member == null) {
            throw new UsernameNotFoundException(memberName);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // 可用性 :true:可用 false:不可用
        boolean enabled = true;
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;
        for (Role role : member.getRoles()) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            for (Permission permission : role.getPermissions()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getUri());
                grantedAuthorities.add(authority);
            }
        }
        User user = new User(member.getEmail(), member.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return user;
    }

}
