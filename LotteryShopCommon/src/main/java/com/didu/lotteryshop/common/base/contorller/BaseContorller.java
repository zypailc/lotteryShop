package com.didu.lotteryshop.common.base.contorller;

import com.didu.lotteryshop.common.entity.UserDetil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

public class BaseContorller {

    @Autowired


    //获取用户信息
    public UserDetil getModel(Principal user){
        UserDetil userDetil = new UserDetil();
        return userDetil;
    }
}
