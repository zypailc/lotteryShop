package com.didu.lotteryshop.common.filter;

import com.didu.lotteryshop.common.config.Constants;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Component
public class LoginSessionFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("进来了");
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String memberName = authentication.getPrincipal().toString();
        String user_name =  (String)httpServletRequest.getSession().getAttribute(Constants.LOGIN_SESSION_KEY);
        if(StringUtils.isBlank(user_name)){
            httpServletRequest.getSession().setAttribute(Constants.LOGIN_SESSION_KEY,memberName);
        }
       // Principal principal = (Principal)authentication.getPrincipal();
      //  System.out.println(principal.getName());
        System.out.println(authentication.getPrincipal());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
