package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.MIntro;
import com.didu.lotteryshop.common.mapper.MIntroMapper;
import com.didu.lotteryshop.common.service.form.impl.MIntroServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseInfoService extends BaseService {

    @Autowired
    private MIntroMapper mIntroMapper;

    public Map<String,Object> indexInfo(String languageType){
        Map<String,Object> map = new HashMap<>();
        //充值途径
        String sql = "select mp_.link_address as linkAddress,ls_.url as viewUrl from m_partner mp_ left join ls_image ls_ on (mp_.ls_image_id = ls_.id) where mp_.type = 1";
        List<Map<String,Object>> partners = getSqlMapper().selectList(sql);
        //合作伙伴
        sql = "select mp_.link_address as linkAddress,ls_.url as viewUrl from m_partner mp_ left join ls_image ls_ on (mp_.ls_image_id = ls_.id) where mp_.type = 2";
        List<Map<String,Object>> external = getSqlMapper().selectList(sql);

        sql = "select li_.url as url , i_"+languageType+".title as title,i_"+languageType+".content as content " +
                " from m_intro mi_ " +
                " left join ls_image li_ on (mi_.ls_image_id = li_.id)"+
                " left join intro_"+languageType + " i_"+languageType + " on (mi_.language_id = i_"+languageType+".id) where mi_.type = ";
        //项目特点
        List<Map<String,Object>> characteristicProject = getSqlMapper().selectList(sql + MIntro.TYPE_CHARACTERISTIC_PROJECT);
        //分配资金
        List<Map<String,Object>> allocationFunds = getSqlMapper().selectList(sql + MIntro.TYPE_ALLOCATION_FUNDS);
        //项目白皮书
        List<Map<String,Object>> whiteBook = getSqlMapper().selectList(sql + MIntro.TYPE_WHITE_BOOK);

        map.put("partners",partners);
        map.put("external",external);
        map.put("whiteBook",whiteBook);
        map.put("characteristicProject",characteristicProject);
        map.put("allocationFunds",allocationFunds);
        return map;
    }

}
