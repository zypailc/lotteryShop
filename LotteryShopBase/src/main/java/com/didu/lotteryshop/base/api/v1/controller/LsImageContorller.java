package com.didu.lotteryshop.base.api.v1.controller;


import com.didu.lotteryshop.base.api.v1.service.form.MemberService;
import com.didu.lotteryshop.base.api.v1.service.form.imp.LsImageServiceImpl;
import com.didu.lotteryshop.base.api.v1.service.form.imp.MemberServiceImp;
import com.didu.lotteryshop.common.base.contorller.BaseContorller;
import com.didu.lotteryshop.common.entity.LoginUser;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 图片操作Contorller
 */
@Controller
@RequestMapping("/v1/lsimage")
public class LsImageContorller extends BaseContorller {

    @Autowired
    private LsImageServiceImpl imageService;
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private MemberServiceImp memberService;


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

}
