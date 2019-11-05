package com.didu.lotteryshop.common.filter;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.UserDetil;
import com.github.abel533.sql.SqlMapper;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginSessionFilter extends OncePerRequestFilter {

    /*@Autowired
    private SqlSession sqlSession;*/
    @Autowired
    private WebApplicationContext wac;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        //最新登陆的用户名
        String memberName = authentication.getPrincipal().toString();
        //如果 memberName == ‘anonymousUser’ 没有登陆用户
        //登陆后记录的用户名
        String user_name =  (String)httpServletRequest.getSession().getAttribute(Constants.LOGIN_SESSION_KEY);
        httpServletRequest.getSession().setAttribute(Constants.LOGIN_SESSION_KEY,memberName);
        Map<String,Object> map = null;
        UserDetil userDetil = null;
        //判断用户 如果没有存在或者最新登陆的用户和登陆后记录的用户不匹配 重新查询用户的信息 存入session
        if((memberName != null && !"anonymousUser".equals(memberName))||
                (memberName != null && !"anonymousUser".equals(memberName) && user_name != null && !"".equals(user_name) && !user_name.equals(memberName))){
            SqlSession sqlSession = wac.getBean(SqlSession.class);
            SqlMapper sqlMapper = new SqlMapper(sqlSession);
            //step 3 查询数据库，存入用户
            String sql ="select member_name as memberName,email from es_member where email = '" + memberName +"'";
            List<Map<String,Object>> list =  sqlMapper.selectList(sql);
            if(list != null && list.size() > 0){
                map = list.get(0);
                userDetil = new UserDetil();
                userDetil.setEmail(map.get("email") != null ? map.get("email").toString():"");
                userDetil.setMemberName(map.get("memberName") != null ? map.get("memberName").toString():"");
            }
        }
        if(userDetil != null) {
            httpServletRequest.getSession().setAttribute(Constants.LOGIN_USER, userDetil);
        }
        // Principal principal = (Principal)authentication.getPrincipal();
        //System.out.println(principal.getName());
        //System.out.println(authentication.getPrincipal());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
