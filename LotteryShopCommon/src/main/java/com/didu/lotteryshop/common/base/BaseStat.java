package com.didu.lotteryshop.common.base;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Component
public class BaseStat {
    @Autowired
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;
    private HttpSession session;
    @Autowired
    private WebApplicationContext wac;
    private SqlMapper sqlMapper;
    private LoginUser loginUser;

    public HttpServletResponse getResponse(){
        return response;
    }
    public SqlMapper getSqlMapper(){
        if(sqlMapper == null){
            sqlMapper = new SqlMapper(wac.getBean(SqlSession.class));
        }
        return  sqlMapper;
    }
    public LoginUser getLoginUser(){
        return  (LoginUser)request.getSession().getAttribute(Constants.LOGIN_USER);
    }
    public HttpSession  getSession(){
        return request.getSession();
    }
    public  HttpServletRequest getRequest(){
        request.getSession();
        return request;
    }

    /**
     * 判断选择语言
     * @return
     */
    public boolean isChineseLanguage(){
        boolean bool = false;
        Cookie[] cookies = this.getRequest().getCookies();
        for(Cookie c : cookies){
            if(c.getName() != null && "somoveLanguage".equals(c.getName())){
                if(c.getValue() != null && "zh".equals(c.getValue())){
                    bool = true ;
                    return bool;
                }
            }
        }
//        if(!bool){
//           String somoveLanguage =  this.getRequest().getHeader("somoveLanguage");
//           if(StringUtils.isNotBlank(somoveLanguage) &&  "zh".equals(somoveLanguage)){
//               bool = true ;
//           }
//        }
        return bool;
    }
}
