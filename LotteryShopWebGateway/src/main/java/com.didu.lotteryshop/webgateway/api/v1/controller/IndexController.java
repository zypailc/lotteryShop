package com.didu.lotteryshop.webgateway.api.v1.controller;

import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.webgateway.config.Constants;
import com.didu.lotteryshop.webgateway.controller.WebgatewayBaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Web页面Contorller
 * @date 2019-10-21
 * @author CHJ
 *
 */
@Controller
public class IndexController extends WebgatewayBaseController {
//    @Value("${security.oauth2.client.client-id}")
//    private String securityClientId;
//    @Value("${security.oauth2.client.client-secret}")
//    private String securityClientSecret;

    @RequestMapping("/")
    public String index1(Model model){
        model = getModel(model);
        return Constants.INDEX;
    }

    /**
     * 500错误页面
     * @param model
     * @return
     */
    @RequestMapping("/web/auth500")
    public String Error500(Model model){
        model = getModel(model);
        return "error/500";
    }
    /**
     * 404错误页面
     * @param model
     * @return
     */
    @RequestMapping("/web/auth404")
    public String Error404(Model model){
        model = getModel(model);
        return "error/404";
    }
    /**
     * 主页
     * @param model
     * @param generalizePersonId 推广人员的Id
     * @return
     */
    @RequestMapping("/web/"+Constants.INDEX_URL)
    public String index(Model model, HttpServletRequest request,String generalizePersonId){
        model = getModel(model);
        model.addAttribute("generalizePersonId",generalizePersonId);
        return Constants.INDEX;
    }

    /**
     *登录页面
     * @param model
     * @return
     */
    @RequestMapping("/web/authLogin")
    public String login(Model model,String rdirectUrl){
        if(StringUtils.isBlank(rdirectUrl)) {
            //默认登录跳转页面 如果不固定页面跳转地址，地址错误之后，会过滤器截器，跳转到404页面
            rdirectUrl = "/web/authIndex";
        }
        //}
        model = getModel(model);
        model.addAttribute("type","login");
//        model.addAttribute("client_id",securityClientId);
//        model.addAttribute("client_secret",securityClientSecret);
        model.addAttribute("rdirect_url",rdirectUrl);
        return "login";
    }



    /**
     * 注册页面
     * @param model
     * @return
     */
    @RequestMapping("/web/authRegister")
    public String register(Model model){
        model = getModel(model);
        model.addAttribute("type","register");
        return "register";
    }

    /**
     * 竞彩中心
     * @param model
     * @return
     */
    @RequestMapping("/web/"+ Constants.PLAYTHELOTTERY_URL)
    public String playTheLottery(Model model){
        model = getModel(model);
        return "playlottery/"+Constants.PLAYTHELOTTERY;
    }

    /**
     * 竞猜A详细页面
     * @param model
     * @return
     */
    @RequestMapping("/web/authLotteryServices")
    public String lotteryaServices(Model model,String playType){
        model = getModel(model);
        model.addAttribute("playType",playType);
        return "playlottery/lotterya_service";
    }

    /**
     * 竞猜B详细页面
     * @param model
     * @param playTypeId 玩法类型Id
     * @return
     */
    @RequestMapping("/web/authLotterybServices")
    public String lotterybService(Model model,String playTypeId,String playType){
        model = getModel(model);
        model.addAttribute("playTypeId",playTypeId);
        model.addAttribute("playType",playType);
        return "playlottery/lotteryb_service";
    }

    /**
     * 找回密码
     * @param model
     * @return
     */
    @RequestMapping("/web/authForgotPassword")
    public String forgotPassword(Model model){
        model = getModel(model);
        return  "forgot_password";
    }

    /**
     * 个人中心
     * @param model
     * @return
     */
    @RequestMapping("/web/personalCenter")
    public String personalCenter(Model model){
        model = getModel(model);
        return "personal/personal_center";
    }


    /**
     * 手机模块显示页面
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/web/personalPhoneType")
    public String phoneType(Model model, @Param(value = "type") String type){
        model = getModel(model);
        return "personal/record/"+type+"_phone";
    }

    /**
     * 手机版钱包显示
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/web/personalWalletBase")
    public String walletBase(Model model,@Param(value = "type")String type){
        model = getModel(model);
        if(type.equals("wallet_eth")){
            model.addAttribute("title","ETH");
        }
        if(type.equals("wallet_flat")){
            if(super.isChineseLanguage()){
                model.addAttribute("title","FLAT");
            }else {
                model.addAttribute("title","金幣");
            }
        }
        if(type.equals("wallet_putMoney")){
            model.addAttribute("title","PUT MONEY");
            if(super.isChineseLanguage()){
                model.addAttribute("title","待領幣");
            }else {
                model.addAttribute("title","PUT MONEY");
            }
        }
        if(type.equals("wallet_gdEth")){
            if(super.isChineseLanguage()){
                model.addAttribute("title","分红");
            }else {
                model.addAttribute("title","DIVIDEND");
            }

        }
        model.addAttribute("type",type);
        //eth和平台币展示记录
        return "personal/wallet/phone/wallet_base";
    }

    /**
     * 钱包管理
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/web/personalWalletBind")
    public String walletWallet(Model model,@Param(value = "type")String type){
        model = getModel(model);
        model.addAttribute("type",type);
        return "personal/wallet/phone/wallet_bind";
    }

    /**
     * 钱包记录界面
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/web/personWalletRecord")
    public String personWalletRecord(Model model,@Param(value = "type") String type){
        model = getModel(model);
        model.addAttribute("type",type);
        model.addAttribute("title",walletType(type));
        return "personal/wallet/phone/wallet_base_record";
    }

    /**
     * 推广主頁
     * @param model
     * @return
     */
    @RequestMapping("/web/personalGeneralizePhone")
    public String generalizePhone(Model model){
        model = getModel(model);
        return "personal/generalize/generalize_phone";
    }

    /**
     * 推广记录
     * @param model
     * @return
     */
    @RequestMapping("/web/personalGeneralizeRecord")
    public String generalizeRecord(Model model,@Param(value = "type") String type){
        model = getModel(model);
        return "personal/generalize/generalize_record";
    }

    /**
     * 推广统计
     * @param model
     * @return
     */
    @RequestMapping("/web/generalizeStatistics")
    public String generalizeStatistics(Model model){
        model = getModel(model);
        return "personal/generalize/generalize_statistics";
    }

    @RequestMapping("/web/personalPasswordManagement")
    public String personalPasswordManagement(Model model){
        model = getModel(model);
        return  "personal/wallet/phone/password_management";
    }



    /**
     * 推广收益 暂时弃用
     * @param model
     * @return
     */
    @RequestMapping("/person/generalizeEarnings")
    public String generalizeEarnings(Model model){
        model = getModel(model);
        return "generalize/generalize_earnings";
    }

    /**
     * 判断钱包类型
     * @param type
     * @return
     */
    private String walletType(String type){
        if(type.equals("wallet_eth")){
            return "ETH";
        }
        if(type.equals("wallet_flat")){
            return "FLAT";
        }
        if(type.equals("wallet_putMoney")){
            return "PUT MONEY";
        }
        if(type.equals("wallet_gdEth")){
            return "DIVIDEND";
        }
        return "";
    }

    /**
     * 隐私政策
     * @param model
     * @return
     */
    @RequestMapping("/web/authPrivacyPolicy")
    public String privacyPolicy(Model model){
        model = getModel(model);
        return "file/privacyPolicy";
    }

    /**
     * 免责声明
     * @param model
     * @return
     */
    @RequestMapping("/web/authDisclaimer")
    public String disclaimer(Model model){
        model = getModel(model);
        return "file/disclaimer";
    }

    /**
     * cookie政策
     * @param model
     * @return
     */
    @RequestMapping("/web/authCookiePolicy")
    public String cookiePolicy(Model model){
        model = getModel(model);
        return "file/cookiePolicy";
    }
}
