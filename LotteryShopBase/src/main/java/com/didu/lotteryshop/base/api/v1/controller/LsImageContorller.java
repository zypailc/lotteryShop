package com.didu.lotteryshop.base.api.v1.controller;


import com.didu.lotteryshop.base.api.v1.service.MemberService;
import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.service.form.impl.LsImageServiceImpl;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 图片操作Contorller
 */
@Controller
@RequestMapping("/v1/lsimage")
public class LsImageContorller extends BaseBaseController {

    @Autowired
    private LsImageServiceImpl imageService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private LsImageMapper lsImageMapper;


    /**
     * 根据图片类型获取图片
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping("/imageType")
    public List<LsImage> findImageType(Integer type){
        return imageService.findImageType(type);
    }

    /**
     * 修改头像
     * @param member
     * @return
     */
    @ResponseBody
    @RequestMapping("/headPortrait")
    public ResultUtil headPortrait(Member member){
        LoginUser userDetil= getLoginUser();
        member.setId(userDetil.getId());
        return memberService.headPortrait(member);
    }

    @ResponseBody
    @RequestMapping("/getImg")
    public void getImg(String id, HttpServletResponse response){
        if(id != null && !"".equals(id)){
            LsImage lsImage = new LsImage();
            lsImage = lsImageMapper.selectById(Integer.parseInt(id));
            FileUtil.outImg(response,lsImage.getByteData());
        }
    }
}
