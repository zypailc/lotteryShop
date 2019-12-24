package com.didu.lotteryshop.manage.controller;


import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.manage.service.MIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2019-11-21
 */
@Controller
@RequestMapping("/mIntro")
public class MIntroController {

    @Autowired
    private MIntroService mIntroService;

    @ResponseBody
    @RequestMapping("/findMIntro")
    public List<Map<String,Object>> findMIntro(String type){
        return mIntroService.findMIntro(type);
    }

    @ResponseBody
    @RequestMapping("/saveCharacteristicProject")
    public ResultUtil saveCharacteristicProject(HttpServletRequest request,String title_zh, String content_zh, String title_en, String content_en,Integer type){
        return  mIntroService.saveCharacteristicProject(request,title_zh,content_zh,title_en,content_en,type);
    }


    @ResponseBody
    @RequestMapping("/saveAllocationfunds")
    public ResultUtil saveAllocationfunds(String title_zh,String content_zh,String title_en,String content_en){
        //return  mIntroService.saveAllocationfunds(title_zh,content_zh,title_en,content_en);
        return null;
    }

    @ResponseBody
    @RequestMapping("/updateCharacteristicProject")
    public ResultUtil updateCharacteristicProject(HttpServletRequest request,String id,String title_zh,String content_zh,String title_en,String content_en,String sort){
        if(id == null || "".equals(id)){
            return ResultUtil.errorJson("error");
        }
        return mIntroService.updateCharacteristicProject(request,id,title_zh,content_zh,title_en,content_en,sort);
    }

    @ResponseBody
    @RequestMapping("/deleteMIntro")
    public ResultUtil deleteMIntro(String id){
        if(id == null || "".equals(id)){
            return ResultUtil.errorJson("error");
        }
        return mIntroService.deleteMIntro(id);
    }

    @ResponseBody
    @RequestMapping("/updateWhiteBoob")
    public ResultUtil updateWhiteBoob(String id,String title_zh,String content_zh,String title_en,String content_en,String sort){
        if(id == null || "".equals(id)){
            return ResultUtil.errorJson("error");
        }
        if(title_zh == null || "".equals(title_zh)){
            return ResultUtil.errorJson("error");
        }
        if(content_zh == null || "".equals(content_zh)){
            return ResultUtil.errorJson("error");
        }

        if(title_en == null || "".equals(title_en)){
            return ResultUtil.errorJson("error");
        }
        if(content_en == null || "".equals(content_en)){
            return ResultUtil.errorJson("error");
        }
        return mIntroService.updateWhiteBoob(id,title_zh,content_zh,title_en,content_en);
    }

}

