package com.didu.lotteryshop.common.base.contorller;

import com.didu.lotteryshop.common.base.BaseStat;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.UserDetil;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class BaseContorller extends BaseStat{

    public Model getModel(Model model){
        UserDetil userDetil= getLoginUser();
        model.addAttribute("UserDetil",userDetil);
        if(userDetil == null){
            model.addAttribute("whetherLogin",false);
        }else{
            model.addAttribute("whetherLogin",true);
        }
        return model;
    }
}
