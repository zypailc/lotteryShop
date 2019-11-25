package com.didu.lotteryshop.common.filter;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.filter.util.FilterUtil;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ConfigurationUtil;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class LoginSessionFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String path = FilterUtil.path(httpServletRequest.getRequestURI());
        //如果是前台请求的资源文件 不需要过滤请求
        if(FilterUtil.path_s().indexOf(path) < 0){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //最新登陆的用户名
            String memberName = null;
            try {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                memberName = userDetails.getUsername();
            } catch (Exception e) {
                memberName = authentication.getPrincipal().toString();
            }
            //如果 memberName == ‘anonymousUser’ 没有登陆用户
            //登陆后记录的用户名
            String user_name = (String) httpServletRequest.getSession().getAttribute(Constants.LOGIN_SESSION_KEY);
            Map<String, Object> map = null;
            LoginUser loginUser = null;
            //判断用户 如果没有存在或者最新登陆的用户和登陆后记录的用户不匹配 重新查询用户的信息 存入session
            if (/*(memberName != null && !"anonymousUser".equals(memberName)) ||
                    (memberName != null && !"anonymousUser".equals(memberName) && user_name != null && !"".equals(user_name) && !user_name.equals(memberName))||
                    ("anonymousUser".equals(memberName) && !memberName.equals(user_name))*/
                    (memberName!= null && !"".equals(memberName)) && (
                            ((!"browser".equals(memberName) || !"anonymousUser".equals(memberName))) ||
                                    ("anonymousUser".equals(memberName) && user_name != null)
                            )
            ) {
                SqlSession sqlSession = wac.getBean(SqlSession.class);
                SqlMapper sqlMapper = new SqlMapper(sqlSession);
                //必须过验证之后才存储用户名
                if(!memberName.equals("anonymousUser")) {
                    httpServletRequest.getSession().setAttribute(Constants.LOGIN_SESSION_KEY, memberName);
                }else {
                    memberName = user_name;//如果请求的是静态资源 用户名取的是session里面的用户
                }
                String sql_date = "select date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime from es_member where email = '" + memberName + "'";
                List<Map<String, Object>> list_1 = sqlMapper.selectList(sql_date);
                String update_date = "";
                String update_date_old = (String) httpServletRequest.getSession().getAttribute(Constants.LOGIN_SESSION_UPDATE_KEY);
                if(list_1 != null && list_1.size() > 0){
                    update_date = list_1.get(0).get("createTime").toString();
                }
                if(update_date_old == null || ( update_date_old != null  && !update_date_old.equals(update_date))){
                    //step 3 查询数据库，存入用户
                    String sql = "select id,member_name as memberName,email,head_portrait_url as headPortraitUrl,p_address as pAddress,b_address as bAddress,wallet_name as walletName " +
                            " ,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime ,payment_code as paymentCode" +
                            " from es_member where email = '" + memberName + "'";
                    //存入用户修改时间
                    List<Map<String, Object>> list = sqlMapper.selectList(sql);
                    if (list != null && list.size() > 0) {
                        map = list.get(0);
                        httpServletRequest.getSession().setAttribute(Constants.LOGIN_SESSION_UPDATE_KEY, map.get("createTime") != null ? map.get("createTime").toString() : "");
                        loginUser = new LoginUser();
                        loginUser.setId(map.get("id") != null ? map.get("id").toString() : "");
                        loginUser.setEmail(map.get("email") != null ? map.get("email").toString() : "");
                        loginUser.setMemberName(map.get("memberName") != null ? map.get("memberName").toString() : "");
                        loginUser.setHeadPortraitUrl((map.get("headPortraitUrl") != null && !"".equals(map.get("headPortraitUrl"))) ? map.get("headPortraitUrl").toString() : Constants.HEAD_PORTRAIT_URL);
                        loginUser.setPAddress(map.get("pAddress") != null ? map.get("pAddress").toString() : "");
                        loginUser.setBAddress(map.get("bAddress") != null ? map.get("bAddress").toString() : "");
                        loginUser.setPaymentCode(map.get("paymentCode") != null ? map.get("paymentCode").toString() : "");
                        String walletName = "";
                        try {
                            walletName = map.get("walletName") != null && !"".equals(map.get("walletName")) ? AesEncryptUtil.decrypt(map.get("walletName").toString(), Constants.KEY_THREE) : "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loginUser.setWalletName(walletName);
                    }
                }
                if (loginUser != null) {
                    httpServletRequest.getSession().setAttribute(Constants.LOGIN_USER, loginUser);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
