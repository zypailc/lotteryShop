package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.MIntro;
import com.didu.lotteryshop.common.entity.MPartner;
import com.didu.lotteryshop.common.mapper.MIntroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseInfoService extends BaseBaseService {

    @Autowired
    private MIntroMapper mIntroMapper;


    /**
     * 查询项目白皮书
     * @param languageType
     * @return
     */
    public List<Map<String,Object>> infoContentWhiteBook(String languageType){
        return  findMintro(languageType,MIntro.TYPE_WHITE_BOOK);
    }

    /**
     * 查询项目特点
     * @param languageType 语言类型
     * @return
     */
    public List<Map<String,Object>> infoContentCharacteristic(String languageType){
        return  findMintro(languageType,MIntro.TYPE_CHARACTERISTIC_PROJECT);
    }

    /**
     * 查询或作伙伴
     * @return
     */
    public List<Map<String,Object>> infoContentPartners(){
        return  findContent(MPartner.TYPE_PARTNER);
    }

    /**
     * 查询充值途径
     * @return
     */
    public List<Map<String,Object>> infoContentExternal(){
        return  findContent(MPartner.TYPE_EXTERNAL);
    }

    /**
     * 查询或作伙伴或者充值途径
     * @param type
     * @return
     */
    private List<Map<String,Object>> findContent(String type){
        String sql = "select mp_.link_address as linkAddress,ls_.id as imgId from m_partner mp_ left join ls_image ls_ on (mp_.ls_image_id = ls_.id) where mp_.type = " + type + " order by mp_.sort";
        return getSqlMapper().selectList(sql);
    }

    /**
     *
     * @param languageType
     * @param type
     * @return
     */
    private List<Map<String,Object>> findMintro(String languageType,Integer type){
        String sql = "select li_.id as imgId, i_"+languageType+".title as title,i_"+languageType+".content as content " +
                " from m_intro mi_ " +
                " left join ls_image li_ on (mi_.ls_image_id = li_.id)"+
                " left join intro_"+languageType + " i_"+languageType + " on (mi_.language_id = i_"+languageType+".id) where mi_.type = ";
        return getSqlMapper().selectList(sql + type);
    }

}
