package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsLsbaccounts;
import com.didu.lotteryshop.common.mapper.EsLsbaccountsMapper;
import com.didu.lotteryshop.common.service.form.IEsLsbaccountsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * lsb平台账目流水记录 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
@Service
public class EsLsbaccountsServiceImpl extends ServiceImpl<EsLsbaccountsMapper, EsLsbaccounts> implements IEsLsbaccountsService {
    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;

    /** dic_type 在sys_dic字段值*/
    public static String DIC_TYPE = "lsbaccounts_dictype";
    /** 充值 */
    public static String DIC_TYPE_IN = "1";
    /** 提现 */
    public static String DIC_TYPE_DRAW = "2";
    /** 提取待领币 **/
    public static String DIC_TYPE_EXTRACT = "3";

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
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId,BigDecimal.ZERO,null);
    }
    /**
     * 新增入账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param  gasFee 燃气费
     * @param  transferHashValue 转账事务哈希值
     * @return
     */
    public boolean addInSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId,BigDecimal gasFee,String transferHashValue){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId,gasFee,transferHashValue);
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
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,null);
    }
    /**
     * 新增入账（处理中）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param  transferHashValue 转账事务哈希值
     * @return
     */
    public boolean addInBeingprocessed(String memberId,String dicTypeValue,BigDecimal total,String operId,String transferHashValue){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,transferHashValue);
    }

    /**
     * 新增出账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addOutSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId,BigDecimal.ZERO,null);
    }
    /**
     * 新增出账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param  gasFee 燃气费
     * @param  transferHashValue 转账事务哈希值
     * @return
     */
    public boolean addOutSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId,BigDecimal gasFee,String transferHashValue){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId,gasFee,transferHashValue);
    }
    /**
     * 新增出账（处理中）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addOutBeingProcessed(String memberId,String dicTypeValue,BigDecimal total,String operId){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,null);
    }
    /**
     * 新增出账（处理中）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param transferHashValue  转账事务哈希值
     * @return
     */
    public boolean addOutBeingProcessed(String memberId,String dicTypeValue,BigDecimal total,String operId,String transferHashValue){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,transferHashValue);
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
     * @param  transferHashValue 转账事务哈希值
     * @return
     */
    private boolean add(String memberId, String dicTypeValue, int type, BigDecimal total, int status, String operId,BigDecimal gasFee,String transferHashValue){
        boolean bool = false;
        if(StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(dicTypeValue) && total != null
                && status != STATUS_FAIL){ //新增时禁止直接插入失败数据，只有异步调用update来修改数据状态为失败
            BigDecimal  balance = null;
            //进账
            if(type == TYPE_IN && status == STATUS_SUCCESS){
                balance = esLsbwalletService.updateBalance(total,memberId,true);
            //出账
            }else if(type == TYPE_OUT){
                if(status == STATUS_SUCCESS){
                    //成功，直接减余额
                    balance = esLsbwalletService.updateBalance(total,memberId,false);
                }else if(status == STATUS_BEINGPROCESSED){
                    //处理中，冻结金额
                    balance = esLsbwalletService.updateAddFreeze(total,memberId);
                }
            }
            //balance是null时，以上方法执行失败，请认真查看。
            if(balance == null) return bool;
            EsLsbaccounts esLsbaccounts = new EsLsbaccounts();
            esLsbaccounts.setMemberId(memberId);
            esLsbaccounts.setDicType(dicTypeValue);
            esLsbaccounts.setType(type);
            esLsbaccounts.setAmount(total);
            esLsbaccounts.setBalance(balance);
            esLsbaccounts.setStatus(status);
            esLsbaccounts.setStatusTime(new Date());
            esLsbaccounts.setCreateTime(new Date());
            esLsbaccounts.setOperId(operId);
            esLsbaccounts.setGasFee(gasFee);
            esLsbaccounts.setTransferHashValue(transferHashValue);
            bool = super.insert(esLsbaccounts);
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
        return this.update(memberId,dicTypeValue,operId,STATUS_SUCCESS);
    }
    /**
     * 修改账目记录（失败）
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @return
     */
    public boolean updateFail(String memberId,String dicTypeValue,String operId){
        return this.update(memberId,dicTypeValue,operId,STATUS_FAIL);
    }



    /**
     * 修改账目记录
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @param status 状态
     * @return
     */
    private  boolean update(String memberId,String dicTypeValue,String operId,int status){
        boolean bool = false;
        if(StringUtils.isBlank(dicTypeValue) || StringUtils.isBlank(operId)) return bool;
        Wrapper<EsLsbaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId).and().eq("dic_type",dicTypeValue).and().eq("oper_id",operId);
        EsLsbaccounts  esLsbaccounts = super.selectOne(wrapper);
        if(esLsbaccounts != null &&  esLsbaccounts.getStatus() != STATUS_FAIL && esLsbaccounts.getStatus() != STATUS_SUCCESS){
            esLsbaccounts.setStatus(status);
            esLsbaccounts.setStatusTime(new Date());
            BigDecimal balance = null;
            if(status == STATUS_SUCCESS){
                if(esLsbaccounts.getType() == TYPE_IN) {
                    balance = esLsbwalletService.updateSubtractFreeze(esLsbaccounts.getAmount(), esLsbaccounts.getMemberId(), false);
                }else{
                    balance = esLsbwalletService.updateSubtractFreeze(esLsbaccounts.getAmount(), esLsbaccounts.getMemberId(), true);
                }
                if(balance == null) return bool;
            }else if(status == STATUS_FAIL){
                if(esLsbaccounts.getType() == TYPE_IN) {
                    balance = esLsbwalletService.updateSubtractFreeze(esLsbaccounts.getAmount(), esLsbaccounts.getMemberId(), true);
                }else{
                    balance = esLsbwalletService.updateSubtractFreeze(esLsbaccounts.getAmount(), esLsbaccounts.getMemberId(), false);
                }
                if(balance == null) return bool;
            }
            esLsbaccounts.setBalance(balance);
            bool = super.updateById(esLsbaccounts);
        }
        return bool;
    }


    /**
     * 查询平台币流水记录
     * @param memberId
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    public Page<EsLsbaccounts> findLsbRecordPagination(String memberId,Integer currentPage,Integer pageSize,String startTime,String endTime,String status) {
        Wrapper<EsLsbaccounts> wrapper = new EntityWrapper<EsLsbaccounts>();
        wrapper.where("1=1");
        if(startTime != null && !"".equals(startTime)){
            wrapper.and(" status_time < {0}",startTime);
        }
        if(endTime != null && !"".equals(endTime)){
            wrapper.and("  status_time < {0}",endTime);
        }
        if(memberId != null && !"".equals(memberId)){
            wrapper.and("member_id = {0}",memberId);
        }
        if(status != null && !"".equals(status)){
            wrapper.in("status",status);
        }
        wrapper.orderBy("create_time",false);
        Page<EsLsbaccounts> pageLsbRecord = new Page<EsLsbaccounts>(currentPage,pageSize);
        return super.selectPage(pageLsbRecord,wrapper);
    }
}
