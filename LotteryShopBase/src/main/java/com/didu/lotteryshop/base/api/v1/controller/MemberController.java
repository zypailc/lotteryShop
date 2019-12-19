package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.api.v1.service.MemberService;
import com.didu.lotteryshop.base.config.BaseConfig;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.service.form.impl.LsImageServiceImpl;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.QRCodeUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.VerifyETHAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/member")
public class MemberController extends BaseBaseController {


    @Autowired
    private MemberService memberService;
    @Autowired
    private LsImageServiceImpl lsImageService;
    @Autowired
    private BaseConfig baseConfig;
    /**
     * 绑定用户外部钱包
     * @param paymentCode
     * @param bAddress
     * @return
     */
    @ResponseBody
    @RequestMapping("/bindWallet")
    public ResultUtil bindWallet(String paymentCode,String bAddress){
        if(paymentCode == null || "".equals(paymentCode)){
            return ResultUtil.errorJson("Please enter your password!");
        }
        if(bAddress == null || "".equals(bAddress)){
            return ResultUtil.errorJson("Please enter your wallet address!");
        }
        if(!VerifyETHAddressUtil.isETHValidAddress(bAddress)){
            return ResultUtil.errorJson("Please fill in your wallet address correctly!");
        }
        return memberService.bindWallet(paymentCode,bAddress);
    }

    /**
     * 修改用户的外部钱包
     * @param bAddress
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateBindWallet")
    public ResultUtil updateBindWallet(String bAddress){
        if(bAddress == null || "".equals(bAddress)){
            return ResultUtil.errorJson("Please enter your wallet address !");
        }
        if(!VerifyETHAddressUtil.isETHValidAddress(bAddress)){
            return ResultUtil.errorJson("Please fill in your wallet address correctly !");
        }

        return memberService.modifyBAddress(bAddress);
    }

    /**
     * 推广页面数据初始化
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/generalizeInit")
    public ResultUtil generalizeInit(String userId, HttpServletResponse response){
        LoginUser loginUser = getLoginUser();
        String QR = baseConfig.getUrl() +  "?generalizePersonId="+loginUser.getId();
        String QRUrl = "";
        FileInputStream inputStream = null;
        try {
            BufferedImage image = QRCodeUtil.createImage_1(QR, "", true);
            LsImage lsImage = lsImageService.selectById(baseConfig.getQRBackgroundId());
            QRUrl = baseConfig.getImgTemporaryUrl() + System.currentTimeMillis()+".png";
            QRCodeUtil.mergeImage_p(ImageIO.read(new ByteArrayInputStream(lsImage.getByteData())),image,QRUrl);
            File file = new File(QRUrl);
            inputStream = new FileInputStream(new File(QRUrl));
            FileUtil.outImg(response,FileUtil.input2byte(inputStream));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                    FileUtil.deleteFile(QRUrl);
                }catch (IOException io){
                    io.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 查询推广用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/findGeneralizeMemberList")
    public List<Map<String,Object>> findGeneralizeMemberList(){
        return memberService.findGeneralizeMemberList();
    }

    /**
     * 查询钱包总账
     * @return
     */
    @ResponseBody
    @RequestMapping("/findWalletTotal")
    public ResultUtil findWalletTotal(String exchangeRate){
        return memberService.findWalletTotal(exchangeRate);
    }

    /**
     * 查询购买记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/findLotterPurchaseResord")
    public ResultUtil findLotterPurchaseResord(Integer currentPage,Integer pageSize,String startTime,String endTime,String type){
        currentPage =  currentPage == null ? 1:currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        return memberService.findLotterPurchaseResord(currentPage,pageSize,startTime,endTime,type);
    }

}
