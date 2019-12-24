package com.didu.lotteryshop.base.api.v1.service;

import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.common.entity.MIntro;
import com.didu.lotteryshop.common.entity.MPartner;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.mapper.MIntroMapper;
import com.didu.lotteryshop.common.service.form.impl.MIntroServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.SysConfigServiceImpl;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseInfoService extends BaseBaseService {

    @Autowired
    private MIntroMapper mIntroMapper;

    @Autowired
    private SysConfigServiceImpl sysConfigService;


    /**
     * 查询项目白皮书
     * @param languageType
     * @return
     */
    public List<Map<String,Object>> infoContentWhiteBook(String languageType){
        return  findMintro(languageType, MIntroServiceImpl.TYPE_WHITE_BOOK,null);
    }

    /**
     * 查询项目特点
     * @param languageType 语言类型
     * @return
     */
    public List<Map<String,Object>> infoContentCharacteristic(String languageType){
        return  findMintro(languageType,MIntroServiceImpl.TYPE_CHARACTERISTIC_PROJECT,null);
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
     * 查询玩法规则
     * @param playType
     * @param languageType
     * @return
     */
    public List<Map<String,Object>> findPlayTypeRule(String playType,String languageType){
        return  findMintro(languageType,MIntroServiceImpl.TYPE_ALLOCATION_FUNDS,Integer.parseInt(playType));
    }

    /**
     *
     * @param languageType
     * @return
     */
    public List<Map<String,Object>> findNotice(String languageType){
        return  findMintro(languageType,MIntroServiceImpl.TYPE_NOTICE,null);
    }

    /**
     * 獲取最高燃氣費
     * @return
     */
    public BigDecimal findPlayConfig(){
        SysConfig sysConfig = sysConfigService.getSysConfig();
        // 最高燃氣費
        BigDecimal gas = Web3jUtils.gasToEtherByBigDecimal(sysConfig.getGasPrice(),sysConfig.getGasLimit());
        return  gas;
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
    private List<Map<String,Object>> findMintro(String languageType,Integer type,Integer playType){
        String sql = "select li_.id as imgId, i_"+languageType+".title as title,i_"+languageType+".content as content " +
                " from m_intro mi_ " +
                " left join ls_image li_ on (mi_.ls_image_id = li_.id)"+
                " left join intro_"+languageType + " i_"+languageType + " on (mi_.language_id = i_"+languageType+".id) where 1=1 " ;
        if(type != null){
            sql +=" and mi_.type = " + type;
        }
        if(playType != null){
            sql +=" and mi_.play_type = " + playType;
        }
        sql += " order by mi_.sort,mi_.create_time ";
        return getSqlMapper().selectList(sql);
    }



}
