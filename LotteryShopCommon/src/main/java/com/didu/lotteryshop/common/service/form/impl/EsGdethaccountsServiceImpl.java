package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.EsGdethaccounts;
import com.didu.lotteryshop.common.mapper.EsGdethaccountsMapper;
import com.didu.lotteryshop.common.service.form.IEsGdethaccountsService;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 推广分成eth账目流水记录 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-17
 */
@Service
public class EsGdethaccountsServiceImpl extends ServiceImpl<EsGdethaccountsMapper, EsGdethaccounts> implements IEsGdethaccountsService {
    @Autowired
    private EsGdethwalletServiceImpl esGdethwalletService;
    @Autowired
    private BaseService baseService;
    /** dic_type 在sys_dic字段值*/
    public static String DIC_TYPE = "gdethaccounts_dictype";
    /** A彩票推广分成 */
    public static String DIC_TYPE_GENERALIZELOTTERYA = "1";
    /** 结算账款 */
    public static String DIC_TYPE_SA = "2";

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
    /** 状态标识符 **/
    public static  String STATUS_MSG_FAIL = "transfer_failed_insufficient_balance";

    /**
     * 新增入账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addInSuccess(String memberId, String dicTypeValue, BigDecimal total, String operId){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId,BigDecimal.ZERO,null,null);
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
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,null,null);
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
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId,gasFee,null,null);
    }
    /**
     * 新增出账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param gasFee gas手续费
     * @param transferHashValue 转账事务哈希值
     * @return
     */
    public boolean addOutSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId,BigDecimal gasFee,String transferHashValue){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId,gasFee,transferHashValue,null);
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
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,null,null);
    }
    /**
     * 新增出账（处理中）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param  transferHashValue 转账事务哈希值
     * @return
     */
    public boolean addOutBeingProcessed(String memberId,String dicTypeValue,BigDecimal total,String operId,String transferHashValue){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_BEINGPROCESSED,operId,BigDecimal.ZERO,transferHashValue,null);
    }

    /**
     * 新增入账（失敗）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param statusMsg 状态信息
     * @return
     */
    public boolean addInFail(String memberId,String dicTypeValue,BigDecimal total,String operId,String statusMsg){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId,BigDecimal.ZERO,null,statusMsg);
    }
    /**
     * 新增入账（失敗）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @return
     */
    public boolean addInFail(String memberId,String dicTypeValue,BigDecimal total,String operId){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId,BigDecimal.ZERO,null,null);
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
     * @param  statusMsg 状态标识符
     * @return
     */
    private boolean add(String memberId, String dicTypeValue, int type,BigDecimal total,int status,String operId,BigDecimal gasFee,String transferHashValue,String statusMsg){
        boolean bool = false;
        if(StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(dicTypeValue) && total != null){
                //&& status != STATUS_FAIL){ //新增时禁止直接插入失败数据，只有异步调用update来修改数据状态为失败
            BigDecimal  balance = null;
            gasFee = gasFee == null ? BigDecimal.ZERO : gasFee;
            BigDecimal amount = total.add(gasFee);
            //进账
            /*if(type == TYPE_IN && status == STATUS_SUCCESS){
                balance = esGdethwalletService.updateBalance(amount,memberId,true);
                //出账
            }else if(type == TYPE_OUT){
                if(status == STATUS_SUCCESS){
                    //成功，直接减余额
                    balance = esGdethwalletService.updateBalance(amount,memberId,false);
                }else if(status == STATUS_BEINGPROCESSED){
                    //处理中，冻结金额
                    balance = esGdethwalletService.updateAddFreeze(amount,memberId);
                }
            }*/
            if(status == STATUS_SUCCESS){
                if(type == TYPE_IN ){
                    balance = esGdethwalletService.updateBalance(amount,memberId,true);
                }else if(type == TYPE_OUT ){
                    //成功，直接减余额
                    balance = esGdethwalletService.updateBalance(amount,memberId,false);
                }
                statusMsg = statusMsg == null ? "success":statusMsg;
            }else if(status == STATUS_BEINGPROCESSED){
                if(type == TYPE_OUT){
                    //处理中，冻结金额
                    balance = esGdethwalletService.updateAddFreeze(amount,memberId);
                }
                statusMsg = statusMsg == null ? "beingprocessed":statusMsg;
            }else if(status == STATUS_FAIL){
                balance = esGdethwalletService.findByMemberId(memberId).getBalance();
                statusMsg = statusMsg == null ? "fail":statusMsg;
            }
            //balance是null时，以上方法执行失败，请认真查看。
            if(balance == null) return bool;
            EsGdethaccounts esGdethaccounts = new EsGdethaccounts();
            esGdethaccounts.setMemberId(memberId);
            esGdethaccounts.setDicType(dicTypeValue);
            esGdethaccounts.setType(type);
            esGdethaccounts.setAmount(total);
            esGdethaccounts.setBalance(balance);
            esGdethaccounts.setStatus(status);
            esGdethaccounts.setStatusMsg(statusMsg);
            esGdethaccounts.setStatusTime(new Date());
            esGdethaccounts.setCreateTime(new Date());
            esGdethaccounts.setOperId(operId);
            esGdethaccounts.setGasFee(gasFee);
            esGdethaccounts.setTransferHashValue(transferHashValue);
            bool = super.insert(esGdethaccounts);
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
        return this.update(memberId,dicTypeValue,operId,STATUS_SUCCESS,BigDecimal.ZERO,null);
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
        return this.update(memberId,dicTypeValue,operId,STATUS_SUCCESS,gasFee,null);
    }
    /**
     * 修改账目记录（失败）
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @return
     */
    public boolean updateFail(String memberId,String dicTypeValue,String operId){
        return this.update(memberId,dicTypeValue,operId,STATUS_FAIL,BigDecimal.ZERO,null);
    }

    /**
     * 修改账目记录（成功）
     * @param id 主键ID
     * @return
     */
    public boolean updateSuccessById(Integer id){
        return this.update(null,null,null,STATUS_SUCCESS,BigDecimal.ZERO,id);
    }
    /**
     * 修改账目记录（成功）
     * @param id 主键ID
     * @param gasFee 燃气费
     * @return
     */
    public boolean updateSuccessById(Integer id,BigDecimal gasFee){
        return this.update(null,null,null,STATUS_SUCCESS,gasFee,id);
    }
    /**
     * 修改账目记录（失败）
     * @param id 操作业务表主键ID
     * @return
     */
    public boolean updateFailById(Integer id){
        return this.update(null,null,null,STATUS_FAIL,BigDecimal.ZERO,id);
    }


    /**
     * 修改账目记录
     * @param memberId 会议ID
     * @param dicTypeValue  sys_dic 字典表类型值
     * @param operId 操作业务表主键ID
     * @param status 状态
     * @param gasFee 燃气费
     * @param id 主键ID
     * @return
     */
    private  boolean update(String memberId,String dicTypeValue,String operId,int status,BigDecimal gasFee,Integer id){
        boolean bool = false;
        EsGdethaccounts  esGdethaccounts = null;
        if(id != null){
            esGdethaccounts = super.selectById(id);
        }else{
            if(StringUtils.isBlank(dicTypeValue) || StringUtils.isBlank(operId)) return bool;
            Wrapper<EsGdethaccounts> wrapper = new EntityWrapper<>();
            wrapper.eq("member_id",memberId).and().eq("dic_type",dicTypeValue).and().eq("oper_id",operId);
            esGdethaccounts = super.selectOne(wrapper);
        }
        gasFee = gasFee == null ? BigDecimal.ZERO : gasFee;
        if(esGdethaccounts != null &&  esGdethaccounts.getStatus() != STATUS_FAIL && esGdethaccounts.getStatus() != STATUS_SUCCESS){
            esGdethaccounts.setStatus(status);
            esGdethaccounts.setStatusTime(new Date());
            esGdethaccounts.setGasFee(gasFee);
            BigDecimal balance = null;
            if(status == STATUS_SUCCESS){
                balance = esGdethwalletService.updateBalance(gasFee,esGdethaccounts.getMemberId(),false);
                if(balance == null) return bool;
                balance =  esGdethwalletService.updateSubtractFreeze(esGdethaccounts.getAmount(),esGdethaccounts.getMemberId(),true);
                if(balance == null) return bool;
            }else if(status == STATUS_FAIL){
                balance =  esGdethwalletService.updateSubtractFreeze(esGdethaccounts.getAmount(),esGdethaccounts.getMemberId(),false);
                if(balance == null) return bool;
            }
            esGdethaccounts.setBalance(balance);
            bool = super.updateById(esGdethaccounts);
        }
        return bool;
    }

    /**
     * 查询出账账单未处理成功的数据；
     * @return
     */
    public List<EsGdethaccounts> findOutBeingProcessed(String memberId){
        Wrapper<EsGdethaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId)
                .and().eq("type",TYPE_OUT)
                .and().eq("status",STATUS_BEINGPROCESSED);
        return super.selectList(wrapper);
    }
    /**
     * 根據用戶Id查詢用戶的流水記錄
     * @param memberId
     * @param currentPage
     * @param pageSize
     * @param startTime 创建开始时间
     * @param endTime 创建结束时间
     * @return
     */
    public Page<EsGdethaccounts> findGdethRecordPagination(String memberId, Integer currentPage, Integer pageSize, String startTime, String endTime){
        Wrapper<EsGdethaccounts> wrapper = new EntityWrapper<EsGdethaccounts>();
        wrapper.where("1=1");
        if(startTime != null && !"".equals(startTime)){
            wrapper.and(" create_time < {0}",startTime);
        }
        if(endTime != null && !"".equals(endTime)){
            wrapper.and("  create_time < {0}",endTime);
        }
        if(memberId != null && !"".equals(memberId)){
            wrapper.and("member_id = {0}",memberId);
        }
        wrapper.orderBy("create_time",false);
        Page<EsGdethaccounts> pageEthRecord = new Page<EsGdethaccounts>(currentPage,pageSize);
        return super.selectPage(pageEthRecord,wrapper);
    }

    /**
     * 根据天数查询是否有结算账款数据。
     * @param day
     * @param memberId
     * @return
     */
    public  boolean findToSAByDay(String memberId,int day){
        boolean bool = false;
        SqlMapper sqlMapper = baseService.getSqlMapper();
        String sql = "select " +
                         " count(egea_.id) as cnts " +
                    " from es_gdethaccounts egea_" +
                    " where egea_.status in(0,1) and egea_.dic_type = '"+DIC_TYPE_SA+"'" +
                        " and egea_.member_id='"+memberId+"'"+
                        " and DATE_SUB(CURDATE(), INTERVAL "+day+" DAY) <= date(egea_.create_time)";
        Map<String,Object> rMap = sqlMapper.selectOne(sql);
        if(rMap != null && !rMap.isEmpty() && rMap.get("cnts") != null && Integer.valueOf(rMap.get("cnts").toString()) > 0){
            bool = true;
        }
        return bool;
    }

    /**
     * 查询等待处理的结账数据
     * @return List<EsGdethaccounts>
     */
    public List<EsGdethaccounts> findSATransferStatusWait(){
        Wrapper<EsGdethaccounts> wrapper = new EntityWrapper<>();
        wrapper.and("transfer_hash_value is not null and transfer_hash_value<>''")
                .and().eq("status",EsGdethaccountsServiceImpl.STATUS_BEINGPROCESSED)
                .and().eq("dic_type",EsGdethaccountsServiceImpl.DIC_TYPE_SA);
        return super.selectList(wrapper);
    }
}
