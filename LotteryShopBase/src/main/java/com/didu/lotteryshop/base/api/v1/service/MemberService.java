package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.*;
import com.didu.lotteryshop.common.service.MailService;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService extends BaseBaseService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private MemberServiceImpl memberServiceImp;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private MailService mailService;
    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsDlbwalletServiceImpl esDlbwalletService;
    @Autowired
    private SysTaskServiceImpl sysTaskService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private EsMemberPropertiesServiceImpl memberPropertiesService;
    @Autowired
    private EsGdethwalletServiceImpl esGdethwalletService;

    /**
     * 绑定钱包
     * @param paymentCode
     * @param bAddress
     * @return
     */
    public ResultUtil bindWallet(String paymentCode, String bAddress){
        LoginUser loginUser = super.getLoginUser();
        String paymentCodeWallet = CodeUtil.getCode(32);//钱包文件生成需要密文和支付密码冲突
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",loginUser.getId());
        map.put("paymentCode",paymentCodeWallet);
        map.put("bAddress",bAddress);
        String reStr = "";
        ResultUtil result = null;
        Map<String, Object> resultMap = null;
        String WallName = "";
        boolean b = false;
        try {
            reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/generate", super.getEncryptRequestHttpEntity(map), String.class);
            if (reStr == null || "".equals(reStr)) {
                String msg = "Wallet creation failed !";
                if(super.isChineseLanguage()){
                    msg = "錢包創建失敗!";
                }
                return ResultUtil.errorJson(msg);
            }
            result = super.getDecryptResponseToResultUtil(reStr); //解密
            //判斷是否成功
            if (result != null && result.getCode() != ResultUtil.SUCCESS_CODE) {
                String msg = "Wallet creation failed !";
                if(super.isChineseLanguage()){
                    msg = "錢包創建失敗!";
                }
                return ResultUtil.errorJson(msg);
            }
            resultMap = (Map<String, Object>) result.getExtend().get(ResultUtil.DATA_KEY);
            if (resultMap != null) {
                WallName = AesEncryptUtil.encrypt(resultMap.get("fileName") == null ? "":resultMap.get("fileName").toString(), Constants.KEY_THREE);//钱包文件加密
                paymentCode = AesEncryptUtil.encrypt_code(paymentCode,Constants.KEY_TOW);//加密支付密码 // 不可逆加密
                //paymentCode = AesEncryptUtil.encrypt(paymentCode, Constants.KEY_TOW);//加密支付密码 // 可逆加密
                Member member = new Member();
                member.setId(loginUser.getId());
                member.setPAddress(resultMap.get("address").toString());
                member.setBAddress(bAddress);
                member.setWalletName(WallName);
                member.setPaymentCode(paymentCode);
                member.setPaymentCodeWallet(paymentCodeWallet);
                b = memberServiceImp.updateMember(member);
                if(!b){
                    String msg = "Failed to initialize wallet!";
                    if(super.isChineseLanguage()){
                        msg = "錢包初始化失敗！";
                    }
                    return ResultUtil.errorJson(msg);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //初始化钱包
        EsEthwallet esEthwallet = esEthwalletService.initInsert(loginUser.getId());
        if (esEthwallet == null) {
            String msg = "Failed to initialize wallet!";
            if(super.isChineseLanguage()){
                msg = "錢包初始化失敗！";
            }
            return ResultUtil.errorJson(msg);
        }
        String msg = "Binding success !";
        if(super.isChineseLanguage()){
            msg = "綁定成功！";
        }
        return ResultUtil.successJson(msg);
    }

    /**
     * 修改用戶信息
     * @param bAddress
     * @return
     */
    public ResultUtil modifyBAddress(String bAddress){
        LoginUser loginUser = getLoginUser();
        Member member = new Member();
        member.setId(loginUser.getId());
        member.setBAddress(bAddress);
        boolean b = memberServiceImp.updateMember(member);
        if(b){
            String msg = "modify successfully !";
            if(super.isChineseLanguage()){
                msg = "修改成功！";
            }
            return ResultUtil.successJson(msg);
        }
        String msg = "fail to modify !";
        if(super.isChineseLanguage()){
            msg = "修改失敗！";
        }
        return ResultUtil.successJson(msg);
    }

    /**
     * 查找推广用户
     * @return
     */
    public List<Map<String,Object>> findGeneralizeMemberList(){
        LoginUser loginUser = getLoginUser();
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("generalize_member_id",loginUser.getId());
        List<Map<String,Object>> list = memberServiceImp.selectMaps(wrapper);
        return  list;
    }

    /**
     * 注冊用戶
     * @param member
     * @return
     */
    public ResultUtil register(Member member) {
        //判断邮箱是否被注册
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("email",member.getEmail());
        Member m = memberServiceImp.selectOne(wrapper);
        if(m != null && m.getEmail() != null){
            String msg = "The email address has been registered !";
            if(super.isChineseLanguage()){
                msg = "該郵箱已註冊，請使用其他郵箱";
            }
            return ResultUtil.errorJson(msg);
        }
        //随机生成一个秘钥jsonObjectjsonObject
        String secretKey = Constants.KEY_TOW;
        //随机生成随机密码
        String password = "1";//CodeUtil.getCode(18);
        try {
            //秘钥和密码加密生成暗文
            String ciphertext = AesEncryptUtil.encrypt_code(password,secretKey);
            member.setPassword(ciphertext);
        }catch (Exception e){
            e.printStackTrace();
        }
        member.setId(CodeUtil.getUuid());
        member.setSecretKey(secretKey);
        member.setCreateTime(new Date());
        member.setUpdateTime(new Date());
        member.setGeneralizeType(0);
        //判斷是否有上級推薦
        if(member.getGeneralizeMemberId() != null && !"".equals(member.getGeneralizeMemberId())){
            //查詢上級基本用戶信息
            Member memberUp = new Member();
            memberUp = memberServiceImp.selectById(member.getGeneralizeMemberId());
            if(memberUp != null){
                member.setGeneralizeMemberId(memberUp.getId());
                member.setGeneralizeMemberIds(memberUp.getGeneralizeMemberIds() == null || "".equals(memberUp.getGeneralizeMemberIds()) ? memberUp.getId():memberUp.getGeneralizeMemberIds() + "," + memberUp.getId());
                member.setGeneralizeMemberLevel(memberUp.getGeneralizeMemberLevel() == null ? 0:(memberUp.getGeneralizeMemberLevel()+1));
            }
        }
        String subject = "ColourBall register password";
        String emailStr = "Your account number is : " + member.getEmail() + "<br/>　Your password is : "+ password;
        if(super.isChineseLanguage()){
            subject = "ColourBall 註冊密碼";
            emailStr = "您的賬號是 : " + member.getEmail() + "<br/>　您的密码是 : "+ password;
        }
        mailService.sendSimpleMailMember(member,subject,emailStr);
        //保存用户信息
        boolean b = memberServiceImp.insert(member);
        if(b){
            //生成平台币钱包
            b = esLsbwalletService.initInsert(member.getId()) != null;
            //生成待领币钱包
            if(b)
                b = esDlbwalletService.initInsert(member.getId()) != null;
            //注册送代领币
            if(b)
               b = sysTaskService.TaskRegister(member.getId());
            if(b) {
                String msg = "Registered successfully , please log in !";
                if(super.isChineseLanguage()){
                    msg = "註冊成功，請登錄!";
                }
                return ResultUtil.successJson(msg);
            }
        }
        String msg = "Registration failed. Please try again !";
        if(super.isChineseLanguage()){
            msg = "註冊失敗，請再試壹次！";
        }
        return ResultUtil.errorJson(msg);
    }

    /**
     * 重置密码
     * @param email
     * @return
     */
    public ResultUtil forgotPassword(String email){
        Member member = new Member();
        member.setEmail(email);
        member.setUpdateTime(new Date());
        //随机生成随机密码
        String password = CodeUtil.getCode(18);
        try {
            //秘钥和密码加密生成暗文
            String ciphertext = AesEncryptUtil.encrypt_code(password,Constants.KEY_TOW);
            member.setPassword(ciphertext);
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        Wrapper wrapper = new EntityWrapper<Member>();
        wrapper.allEq(map);
        boolean b = memberServiceImp.update(member,wrapper);
        if(b) {
            String subject = "ColourBall rnew password";
            String emailStr = "Your account number is : " + member.getEmail() + "<br/>　Your password is : "+ password;
            String msg = "Your new password has been sent to your email !";
            if(super.isChineseLanguage()){
                subject = "ColourBall 新密碼";
                emailStr = "您的賬號是 : " + member.getEmail() + "<br/>　您的密码是 : "+ password;
                msg = "您的新密碼已發送到您的電子郵件！";
            }
            mailService.sendSimpleMailMember(member, subject, emailStr);
            return ResultUtil.successJson(msg);
        }
        String msg = "error , please operate again !";
        if(super.isChineseLanguage()){
            msg = "錯誤，請重新操作!";
        }
        return  ResultUtil.errorJson(msg);
    }

    /**
     * 修改头像
     * @param member
     * @return
     */
    public ResultUtil headPortrait(Member member){
        boolean flag = memberServiceImp.updateById(member);
        if(flag) {
            String msg = "modify successfully !";
            if(super.isChineseLanguage()){
                msg = "修改成功！";
            }
            return ResultUtil.successJson(msg);
        }else {
            String msg = "fail to modify !";
            if(super.isChineseLanguage()){
                msg = "修改失敗！";
            }
            return ResultUtil.errorJson(msg);
        }
    }

    /**
     * 查询用户总资产
     * @param exchangeRate
     * @return
     */
    public ResultUtil findWalletTotal(String exchangeRate){
        if(exchangeRate != null && "".equals(exchangeRate)){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        LoginUser loginUser = getLoginUser();
        EsEthwallet ethwallet = esEthwalletService.findByMemberId(loginUser.getId());// eth
        EsDlbwallet dlbwallet = esDlbwalletService.findByMemberId(loginUser.getId());// dlb
        EsLsbwallet lsbwallet = esLsbwalletService.findByMemberId(loginUser.getId());// lsb
        EsGdethwallet gdethwallet = esGdethwalletService.findByMemberId(loginUser.getId());
        SysConfig sysConfig = sysConfigService.getSysConfig();
        BigDecimal exchangeRateBig = new BigDecimal(exchangeRate).stripTrailingZeros();

        map.put("eth", BigDecimalUtil.bigDecimalToPrecision(ethwallet == null ? new BigDecimal("0") : ethwallet.getTotal()));
        map.put("ethToUsd",BigDecimalUtil.bigDecimalToPrecision(exchangeRateBig.multiply(ethwallet == null ? new BigDecimal("0") : ethwallet.getTotal())));

        BigDecimal dlbToEtb = dlbwallet == null ? new BigDecimal("0") : dlbwallet.getTotal().divide(sysConfig.getLsbToEth(),BigDecimalUtil.BIGDECIMAL_PRECISION,BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
        map.put("dlb",dlbwallet == null ? 0 : dlbwallet.getTotal().stripTrailingZeros());//代领币
        map.put("dlbToEtb",dlbwallet == null ? 0 : dlbToEtb);
        map.put("dlbToUsd",dlbwallet == null ? 0 : BigDecimalUtil.bigDecimalToPrecision(dlbToEtb.multiply(exchangeRateBig)));

        BigDecimal lsbToEth = lsbwallet == null ? new BigDecimal("0") : lsbwallet.getTotal().divide(sysConfig.getLsbToEth(),BigDecimalUtil.BIGDECIMAL_PRECISION,BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
        map.put("lsb",lsbwallet == null ? 0 : lsbwallet.getTotal().stripTrailingZeros());//平台币
        map.put("lsbToEth",lsbwallet == null ? 0 : lsbToEth.stripTrailingZeros());
        map.put("lsbToUsd",lsbwallet == null ? 0 : BigDecimalUtil.bigDecimalToPrecision(lsbToEth.multiply(exchangeRateBig)));
        BigDecimal ethTotal = (ethwallet == null ? new BigDecimal("0") : ethwallet.getTotal()).add(dlbToEtb).add(lsbToEth);
        map.put("ethTotal",BigDecimalUtil.bigDecimalToPrecision(ethTotal));
        map.put("ethTotalToUsd",BigDecimalUtil.bigDecimalToPrecision(ethTotal.multiply(exchangeRateBig)));
        //分红币

        map.put("gdEth",BigDecimalUtil.bigDecimalToPrecision(gdethwallet == null ? new BigDecimal("0"):gdethwallet.getTotal()).stripTrailingZeros());
        map.put("gdEthToUsd",BigDecimalUtil.bigDecimalToPrecision(exchangeRateBig.multiply(gdethwallet == null ? new BigDecimal("0"):gdethwallet.getTotal()).stripTrailingZeros()));
        return ResultUtil.successJson(map);
    }

    /**
     * 查询购买记录
     * @param startTime
     * @param endTime
     * @param type ; type = 1 查询A玩法 type = -1 查询所有
     * @return
     */
    public ResultUtil findLotterPurchaseResord(Integer currentPage,Integer pageSize,String startTime,String endTime,String type,String winning){
        LoginUser loginUser = getLoginUser();
        //可能会有多种玩法 把所有玩法的购买记录拼接在一起 （字段类型：彩票类型(loteryType)，期数(issueNum)，时间(startTime,endTime)，开奖号(luckNum)，自选号(selfLuckNum),中奖金额(luckNum),是否中奖(isLuck)）
        String sql = " select 1 as lotteryType , li_.issue_num as issueNum, DATE_FORMAT(li_.start_time,'%Y-%m-%d %H:%i:%s') as startTime, DATE_FORMAT(li_.end_time,'%Y-%m-%d %H:%i:%s') as endTime,"+
                " li_.luck_num as luckNum,lb_.luck_num as selfLuckNum ,lb_.luck_total as luckTotal,lb_.is_luck as isLuck,lb_.create_time as createTime "+
                " from lotterya_buy lb_ "+
                " left join lotterya_issue li_ on (lb_.lotterya_issue_id = li_.id) "; // A 玩法
        //这里可使用拼接查询其他彩票购买记录
        sql += " where lb_.member_id = '"+loginUser.getId()+"' ";
        if (startTime != null && !"".equals(startTime)) {
            sql += " and DATE_FORMAT(lb_.create_time,'%Y-%m-%d') >= '"+startTime+"'";
        }
        if (endTime != null && !"".equals(endTime)) {
            sql += " and DATE_FORMAT(lb_.create_time,'%Y-%m-%d') <= '"+endTime+"'";
        }
        if(winning != null && !"".equals(winning) && !"-1".equals(winning)){
            sql += " and lb_.is_luck = '" + winning + "'";
        }
        sql += " ORDER BY createTime DESC ";
        sql += " limit " + ((currentPage - 1) * pageSize)+ "," +pageSize;
        List<Map<String,Object>> list = getSqlMapper().selectList(sql);
        return ResultUtil.successJson(list);
    }

    /**
     * 修改用户密码
     * @param oldPassword
     * @param newPassword
     * @param type
     * @return
     */
    public ResultUtil updatePassword(String oldPassword,String newPassword,String type){
        Member member = new Member();
        oldPassword = AesEncryptUtil.encrypt_code(oldPassword,Constants.KEY_TOW);
        newPassword = AesEncryptUtil.encrypt_code(newPassword,Constants.KEY_TOW);
        LoginUser loginUser = getLoginUser();
        if("1".equals(type)){
            //login
            if(!loginUser.getPassword().equals(oldPassword)){
                String msg = "Original password error. Fail to modify !";
                if(super.isChineseLanguage()){
                    msg = "原始密碼錯誤,修改失敗！";
                }
                return ResultUtil.errorJson(msg);
            }
            member.setPassword(newPassword);
        }

        if("2".equals(type)){
            //play code
            if(!loginUser.getPaymentCode().equals(oldPassword)){
                String msg = "Original password error. Fail to modify !";
                if(super.isChineseLanguage()){
                    msg = "原始密碼錯誤,修改失敗！";
                }
                return ResultUtil.errorJson(msg);
            }
            member.setPaymentCode(newPassword);
        }
        member.setId(loginUser.getId());
        boolean b = memberServiceImp.updateById(member);
        if(!b){
            String msg = "System error , please try again !";
            if(super.isChineseLanguage()){
                msg = "系統錯誤，請重試！";
            }
            return ResultUtil.errorJson(msg);
        }
        String msg = "modify successfully !";
        if(super.isChineseLanguage()){
            msg = "修改成功！";
        }
        return ResultUtil.successJson(msg);
    }

    /**
     * 修改金额是否显示
     * @param isView
     * @return
     */
    public ResultUtil updateMoneyIsView(String isView){
        LoginUser loginUser = getLoginUser();
        return memberPropertiesService.updateMoneyIsView(loginUser.getId(),isView);
    }

    /**
     * 修改公告已被查看
     * @return
     */
    public ResultUtil updateNoticeIsRead(String noticeId){
        LoginUser loginUser = getLoginUser();
        if(loginUser == null && loginUser.getId() == null ){
            return null;
        }
        if(noticeId != null && !"".equals(noticeId)){

        }
        return memberPropertiesService.updateNoticeIsView(loginUser.getId(),noticeId);
    }

    /**
     * 查询推广的购买分红和中奖分红
     * @param startTime
     * @param endTime
     * @return
     */
    public ResultUtil findGeneralizeStatistics (String startTime,String endTime){
        LoginUser loginUser = getLoginUser();
        if(loginUser == null){
            return null;
        }
        String sql = " select " +
                " SUM(case when ed_.dic_type = 1 then lpd_.total else 0 end) gTotal," +
                " SUM(case when ed_.dic_type = 2 then lpd_.total else 0 end) zTotal, " +
                " lpd_.level,ed_.dic_type " +
                " from es_dlbaccounts ed_ " +
                " left join lotterya_pm lp_ on (ed_.oper_id = lp_.id) " +
                " left join lotterya_pm_detail lpd_ on (lp_.id = lpd_.lotterya_pm_id) " +
                " where ed_.member_id = '"+loginUser.getId()+"'  and ed_.type = '1'  and ed_.status = '1' and ed_.oper_id <> '-1' and ed_.dic_type in (1,2) " ;
        if(startTime != null && !"".equals(startTime)){
            sql += " and DATE_FORMAT(ed_.create_time,'%Y-%m-%d %T') >= '"+startTime+"' ";
        }
        if(endTime != null && !"".equals(endTime)){
            sql += " and DATE_FORMAT(ed_.create_time,'%Y-%m-%d %T') <= '"+endTime+"' ";
        }
        sql +=" GROUP BY ed_.dic_type,lpd_.level " +
        " order by lpd_.level";
        List<Map<String,Object>> list = getSqlMapper().selectList(sql);

        sql = " select sum(1) as personNum,em_.generalize_member_level " +
                " from es_member em_ " +
                " left join es_member em1_ on (em1_.id = '"+loginUser.getId()+"') " +
                " where em_.generalize_member_ids like '%"+loginUser.getId()+"%' and em_.generalize_member_level > em1_.generalize_member_level  " +
                " and em_.generalize_member_level <= (em1_.generalize_member_level + 6)" +
                " GROUP BY em_.generalize_member_level " +
                " ORDER BY em_.generalize_member_level ";
        List<Map<String,Object>> list1 = getSqlMapper().selectList(sql);
        Map<String,Object> map = new HashMap<>();
        map.put("generalize",list);
        map.put("generalizePerson",list1);
        return ResultUtil.successJson(map);
    }

}
