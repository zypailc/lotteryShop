package com.didu.lotteryshop.common.filter;

import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.EsMemberProperties;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.service.form.impl.EsMemberPropertiesServiceImpl;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginSessionFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String path = path(httpServletRequest.getRequestURI());
        //如果是前台请求的资源文件 不需要过滤请求
        if(path_s().indexOf(path) < 0) {
//            String requestURI = httpServletRequest.getRequestURI();
//            boolean bool = true;
//            if (requestURI.indexOf("/web/") >= 0) {
//                if (requestURI.indexOf("/web/auth") == -1) {
//                    String token = (String) httpServletRequest.getSession().getAttribute("session_login_token");
//                    if(StringUtils.isBlank(token)){
//                        httpServletResponse.sendRedirect("/web/authLogin?rdirectUrl=" + requestURI);
//                        bool = false;
//                    }
//                }
//            }

//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            //最新登陆的用户名
//            String memberName = null;
//            try {
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//                memberName = userDetails.getUsername();
//            } catch (Exception e) {
//                memberName = authentication.getPrincipal().toString();
//            }
         //   if (bool){
                String memberName = httpServletRequest.getHeader("LoginUserName");
                if (StringUtils.isBlank(memberName)) {
                    memberName = (String) httpServletRequest.getSession().getAttribute("LoginUserName");
                }

                //后台管理员用户不用记录用户信息
                if (!"didu123456didu".equals(memberName)) {
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
                        String sql_date = "select date_format(update_time,'%Y-%m-%d %H:%i:%s') as updateTime from es_member where email = '" + memberName + "'";
                        List<Map<String, Object>> list_1 = sqlMapper.selectList(sql_date);
                        String update_date = "";
                        String update_date_old = (String) httpServletRequest.getSession().getAttribute(Constants.LOGIN_SESSION_UPDATE_KEY);
                        if(list_1 != null && list_1.size() > 0){
                            if(list_1.get(0) != null && list_1.get(0).isEmpty()) {
                                update_date = list_1.get(0).get("updateTime").toString();
                            }
                        }
                        if(update_date_old == null || ( update_date_old != null  && !update_date_old.equals(update_date))){
                            //step 3 查询数据库，存入用户
                            String sql = "select id,member_name as memberName,email,head_portrait_url as headPortraitUrl,p_address as pAddress,b_address as bAddress,wallet_name as walletName " +
                                    " ,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime ,date_format(update_time,'%Y-%m-%d %H:%i:%s') as updateTime ,payment_code as paymentCode" +
                                    " , payment_code_wallet as paymentCodeWallet , password as password ,generalize_type as generalizeType " +
                                    " from es_member where email = '" + memberName + "'";
                            //存入用户修改时间
                            List<Map<String, Object>> list = sqlMapper.selectList(sql);
                            if (list != null && list.size() > 0) {
                                map = list.get(0);
                                String WalletName = "";
                                String paymentCode = "";
                                try {
                                    WalletName = AesEncryptUtil.decrypt(map.get("walletName") != null ? map.get("walletName").toString() : "", Constants.KEY_THREE);
                                    paymentCode = map.get("paymentCode") != null ? map.get("paymentCode").toString() : "";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                httpServletRequest.getSession().setAttribute(Constants.LOGIN_SESSION_UPDATE_KEY, map.get("updateTime") != null ? map.get("updateTime").toString() : "");
                                loginUser = new LoginUser();
                                loginUser.setId(map.get("id") != null ? map.get("id").toString() : "");
                                loginUser.setEmail(map.get("email") != null ? map.get("email").toString() : "");
                                loginUser.setMemberName(map.get("memberName") != null ? map.get("memberName").toString() : "");
                                loginUser.setHeadPortraitUrl((map.get("headPortraitUrl") != null && !"".equals(map.get("headPortraitUrl"))) ? map.get("headPortraitUrl").toString() : Constants.HEAD_PORTRAIT_URL);
                                loginUser.setPAddress(map.get("pAddress") != null ? map.get("pAddress").toString() : "");
                                loginUser.setBAddress(map.get("bAddress") != null ? map.get("bAddress").toString() : "");
                                loginUser.setPaymentCodeWallet(map.get("paymentCodeWallet") != null ? map.get("paymentCodeWallet").toString() : "");
                                loginUser.setPassword(map.get("password") != null ? map.get("password").toString() : "");
                                loginUser.setGeneralizeType(map.get("generalizeType") != null ? map.get("generalizeType").toString() : "");
                                loginUser.setPaymentCode(paymentCode);
                                loginUser.setWalletName(WalletName);
                                //用户添加配置
                                if(map.get("id") != null && !"".equals(map.get("id"))){
                                    EsMemberPropertiesServiceImpl memberPropertiesService = wac.getBean(EsMemberPropertiesServiceImpl.class);
                                    //金额是否显示
                                    EsMemberProperties memberProperties = findEsMemberProperties(map.get("id").toString(),EsMemberPropertiesServiceImpl.TYPE_MONEY);
                                    if(memberProperties == null){
                                        memberProperties = new EsMemberProperties();
                                        memberProperties.setMemberId(map.get("id").toString());
                                        memberProperties.setIsView(0);
                                        memberProperties.setType(EsMemberPropertiesServiceImpl.TYPE_MONEY);
                                        memberPropertiesService.insert(memberProperties);
                                        loginUser.setMoneyView("0");//是否显示金额明文
                                    }else {
                                        loginUser.setMoneyView(memberProperties.getIsView() == null ? "0":memberProperties.getIsView().toString());//金额是否显示
                                    }
                                    //公告是否已读
                                    EsMemberProperties memberProperties_1 = findEsMemberProperties(map.get("id").toString(),EsMemberPropertiesServiceImpl.TYPE_NOTICE);
                                    if(memberProperties_1 == null){
                                        memberProperties_1 = new EsMemberProperties();
                                        memberProperties_1.setMemberId(map.get("id").toString());
                                        memberProperties_1.setIsView(0);
                                        memberProperties_1.setType(EsMemberPropertiesServiceImpl.TYPE_NOTICE);
                                        memberPropertiesService.insert(memberProperties_1);
                                        loginUser.setNoticeView("0");//公告是否已读
                                    }else {
                                        loginUser.setNoticeView(memberProperties_1.getIsView() == null ? "0":memberProperties_1.getIsView().toString());
                                    }

                                }
                            }
                        }
                        if (loginUser != null) {
                            httpServletRequest.getSession().setAttribute(Constants.LOGIN_USER, loginUser);
                        }
                    }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 查询个人配置
     * @param memberId
     * @param type
     * @return
     */
    private EsMemberProperties findEsMemberProperties(String memberId,Integer type){
        EsMemberPropertiesServiceImpl memberPropertiesService = wac.getBean(EsMemberPropertiesServiceImpl.class);
        Map<String,Object> mapMemberProperties = new HashMap<>();
        mapMemberProperties.put("member_id",memberId);
        mapMemberProperties.put("type",type);
        List<EsMemberProperties> memberProperties = memberPropertiesService.selectByMap(mapMemberProperties);
        if(memberProperties != null && memberProperties.size() > 0){
            return memberProperties.get(0);
        }
        return null;
    }

    private static String path(String path){
        int index = path.indexOf("/",2);
        String path_1 = path.substring(0,index != -1 ? (index+1):path.length());
        if(index < 0){
            return path_1 = path_1+"/";
        }
        return path_1;
    }

    public static String path_s(){
        return ArrayUtils.toString(Constants.STATIC_RESOURCE_FILENAME, ",");
    }

}
