package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsEthaccounts;
import com.didu.lotteryshop.common.mapper.EsEthaccountsMapper;
import com.didu.lotteryshop.common.service.form.IEsEthaccountsService;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * eth账目流水记录 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
@Service
public class EsEthaccountsServiceImpl extends ServiceImpl<EsEthaccountsMapper, EsEthaccounts> implements IEsEthaccountsService {
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;

    /** dic_type 在sys_dic字段值*/
    public static String DIC_TYPE = "ethaccounts_dictype";
    /** 充值 */
    public static String DIC_TYPE_IN = "1";
    /** 提现 */
    public static String DIC_TYPE_DRAW = "2";
    /** 购买A彩票 */
    public static String DIC_TYPE_BUYLOTTERYA = "3";
    /** 购买B彩票 */
    public static String DIC_TYPE_BUYLOTTERYB = "4";
    /** 赢得A彩票*/
    public static String DIC_TYPE_WINLOTTERYA = "5";
    /** 赢得B彩票*/
    public static String DIC_TYPE_WINLOTTERYB = "6";
    /** 平台手续费 */
    public static String DIC_TYPE_PLATFEE = "7";



    /** 类型：入账 */
    public static int TYPE_IN = 1;
    /** 类型：出账 */
    public static int TYPE_OUT = 0;
    /** 状态：处理中 */
    public static int STATUS_BEINGPROCESSED = 0;
    /** 状态：成功 */
    public static int STATUS_SUCCESS = 1;
    /** 状态：失败 */
    public static int STATUS_FAIL = 2;

    /**
     * 新增入账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addInSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId,BigDecimal.ZERO);
    }
    /**
     * 新增入账（处理中）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addInBeingprocessed(String memberId,String dicTypeValue,BigDecimal total,String operId){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO);
    }

    /**
     * 新增出账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param gasFee gas手续费
     * @return
     */
    public boolean addOutSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId,BigDecimal gasFee){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId,gasFee);
    }
    /**
     * 新增出账（处理中）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addOutBeingprocessed(String memberId,String dicTypeValue,BigDecimal total,String operId){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO);
    }


    /**
     * 新增记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param type 类型
     * @param total 金额
     * @param status 状态
     * @param operId  操作业务表主键ID
     * @param  gasFee 燃气费
     * @return
     */
    private boolean add(String memberId, String dicTypeValue, int type,BigDecimal total,int status,String operId,BigDecimal gasFee){
        boolean bool = false;
        if(StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(dicTypeValue) && total != null
                && status != STATUS_FAIL){ //新增时禁止直接插入失败数据，只有异步调用update来修改数据状态为失败
            BigDecimal  balance = null;
            gasFee = gasFee == null ? BigDecimal.ZERO : gasFee;
            BigDecimal amount = total.add(gasFee);
            //进账
            if(type == TYPE_IN && status == STATUS_SUCCESS){
                balance = esEthwalletService.updateBalance(amount,memberId,true);
            //出账
            }else if(type == TYPE_OUT){
                if(status == STATUS_SUCCESS){
                    //成功，直接减余额
                    balance = esEthwalletService.updateBalance(amount,memberId,false);
                }else if(status == STATUS_BEINGPROCESSED){
                    //处理中，冻结金额
                    balance = esEthwalletService.updateAddFreeze(amount,memberId);
                }
            }
            //balance是null时，以上方法执行失败，请认真查看。
            if(balance == null) return bool;
            EsEthaccounts esEthaccounts = new EsEthaccounts();
            esEthaccounts.setMemberId(memberId);
            esEthaccounts.setDicType(dicTypeValue);
            esEthaccounts.setType(type);
            esEthaccounts.setAmount(total);
            esEthaccounts.setBalance(balance);
            esEthaccounts.setStatus(status);
            esEthaccounts.setStatusTime(new Date());
            esEthaccounts.setCreateTime(new Date());
            esEthaccounts.setOperId(operId);
            esEthaccounts.setGasFee(gasFee);
            bool = super.insert(esEthaccounts);
        }
        return bool;
    }
    /**
     * 修改账目记录（成功）
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @return
     */
    public boolean updateSuccess(String memberId,String dicTypeValue,String operId){
        return this.update(memberId,dicTypeValue,operId,STATUS_SUCCESS,BigDecimal.ZERO);
    }
    /**
     * 修改账目记录（成功）
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @param gasFee 燃气费
     * @return
     */
    public boolean updateSuccess(String memberId,String dicTypeValue,String operId,BigDecimal gasFee){
        return this.update(memberId,dicTypeValue,operId,STATUS_SUCCESS,gasFee);
    }
    /**
     * 修改账目记录（失败）
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @return
     */
    public boolean updateFail(String memberId,String dicTypeValue,String operId){
        return this.update(memberId,dicTypeValue,operId,STATUS_FAIL,BigDecimal.ZERO);
    }



    /**
     * 修改账目记录
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @param status 状态
     * @param gasFee 燃气费
     * @return
     */
    private  boolean update(String memberId,String dicTypeValue,String operId,int status,BigDecimal gasFee){
        boolean bool = false;
        if(StringUtils.isBlank(dicTypeValue) || StringUtils.isBlank(operId)) return bool;
        Wrapper<EsEthaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("memberId",memberId).and().eq("dicType",dicTypeValue).and().eq("operId",operId);
        EsEthaccounts  esEthaccounts = super.selectOne(wrapper);
        gasFee = gasFee == null ? BigDecimal.ZERO : gasFee;
        if(esEthaccounts != null &&  esEthaccounts.getStatus() != STATUS_FAIL && esEthaccounts.getStatus() != STATUS_SUCCESS){
            esEthaccounts.setStatus(status);
            esEthaccounts.setStatusTime(new Date());
            esEthaccounts.setGasFee(gasFee);
            BigDecimal balance = null;
            if(status == STATUS_SUCCESS){
                balance = esEthwalletService.updateBalance(gasFee,memberId,false);
                if(balance == null) return bool;
                balance =  esEthwalletService.updateSubtractFreeze(esEthaccounts.getAmount(),memberId,true);
                if(balance == null) return bool;
            }else if(status == STATUS_FAIL){
                balance =  esEthwalletService.updateSubtractFreeze(esEthaccounts.getAmount(),memberId,false);
                if(balance == null) return bool;
            }
          bool = super.updateById(esEthaccounts);
        }
        return bool;
    }
}