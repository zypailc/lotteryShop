package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.EsDlbaccounts;
import com.didu.lotteryshop.common.mapper.EsDlbaccountsMapper;
import com.didu.lotteryshop.common.service.form.IEsDlbaccountsService;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 待领币平台账目流水记录 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@Service
public class EsDlbaccountsServiceImpl extends ServiceImpl<EsDlbaccountsMapper, EsDlbaccounts> implements IEsDlbaccountsService {
    @Autowired
    private EsDlbwalletServiceImpl esDlbwalletService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private EsDlbaccountsMapper dlbaccountsMapper;

    /** dic_type 在sys_dic字段值*/
    public static String DIC_TYPE = "dlbaccounts_dictype";
    /** 购买A彩票提成 */
    public static String DIC_TYPE_BUYLOTTERYA_PM = "1";
    /** 赢得A彩票提成 */
    public static String DIC_TYPE_WINLOTTERYA_PM = "2";
    /** 购买B彩票提成 */
    public static String DIC_TYPE_BUYLOTTERYB_PM = "7";
    /** 赢得B彩票提成 */
    public static String DIC_TYPE_WINLOTTERYB_PM = "8";
    /** 注册奖励*/
    public static String DIC_TYPE_REGISTER = "3";
    /** 提现(平台币) */
    public static String DIC_TYPE_DRAW = "4";
    /** 第一次消费奖金 */
    public static String DIC_TYPE_FIRSTCONSUMPTION = "5";
    /** 直属下级第一次消费奖励 */
    public static String DIC_TYPE_LOWERFIRSTCONSUMPTION = "6";


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
    public boolean addInSuccess(String memberId, String dicTypeValue, BigDecimal total, String operId){
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_SUCCESS,operId);
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
        return this.add(memberId,dicTypeValue,TYPE_IN,total,STATUS_BEINGPROCESSED,operId);
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
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId);
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
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_BEINGPROCESSED,operId);
    }
    /**
     * 新增记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param type 类型
     * @param total 金额
     * @param status 状态
     * @param operId  操作业务表主键ID
     * @return
     */
    private boolean add(String memberId, String dicTypeValue, int type, BigDecimal total, int status, String operId){
        boolean bool = false;
        if(StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(dicTypeValue) && total != null
                && status != STATUS_FAIL){ //新增时禁止直接插入失败数据，只有异步调用update来修改数据状态为失败
            BigDecimal  balance = null;
            //进账
            if(type == TYPE_IN && status == STATUS_SUCCESS){
                balance = esDlbwalletService.updateBalance(total,memberId,true);
                //出账
            }else if(type == TYPE_OUT){
                if(status == STATUS_SUCCESS){
                    //成功，直接减余额
                    balance = esDlbwalletService.updateBalance(total,memberId,false);
                }else if(status == STATUS_BEINGPROCESSED){
                    //处理中，冻结金额
                    balance = esDlbwalletService.updateAddFreeze(total,memberId);
                }
            }
            //balance是null时，以上方法执行失败，请认真查看。
            if(balance == null) return bool;
            EsDlbaccounts esDlbaccounts = new EsDlbaccounts();
            esDlbaccounts.setMemberId(memberId);
            esDlbaccounts.setDicType(dicTypeValue);
            esDlbaccounts.setType(type);
            esDlbaccounts.setAmount(total);
            esDlbaccounts.setBalance(balance);
            esDlbaccounts.setStatus(status);
            esDlbaccounts.setStatusTime(new Date());
            esDlbaccounts.setCreateTime(new Date());
            esDlbaccounts.setOperId(operId);
            bool = super.insert(esDlbaccounts);
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
        Wrapper<EsDlbaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId).and().eq("dic_type",dicTypeValue).and().eq("oper_id",operId);
        EsDlbaccounts  esDlbaccounts = super.selectOne(wrapper);
        if(esDlbaccounts != null &&  esDlbaccounts.getStatus() != STATUS_FAIL && esDlbaccounts.getStatus() != STATUS_SUCCESS){
            esDlbaccounts.setStatus(status);
            esDlbaccounts.setStatusTime(new Date());
            BigDecimal balance = null;
            if(status == STATUS_SUCCESS){
                balance =  esDlbwalletService.updateSubtractFreeze(esDlbaccounts.getAmount(),memberId,true);
                if(balance == null) return bool;
            }else if(status == STATUS_FAIL){
                balance =  esDlbwalletService.updateSubtractFreeze(esDlbaccounts.getAmount(),memberId,false);
                if(balance == null) return bool;
            }
            esDlbaccounts.setBalance(balance);
            bool = super.updateById(esDlbaccounts);
        }
        return bool;
    }

    /**
     * 查询代领币流水记录
     * @param memberId
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    public Page<Map<String,Object>> findDlbRecordPagination(String memberId,Integer currentPage,Integer pageSize,String startTime,String endTime,String status) {
        Wrapper<EsDlbaccounts> wrapper = new EntityWrapper<EsDlbaccounts>();
        wrapper.where("1=1");
        if(startTime != null && !"".equals(startTime)){
            wrapper.and(" status_time < {0}",startTime);
        }
        if(endTime != null && !"".equals(endTime)){
            wrapper.and("  status_time < {0}",endTime);
        }
        if(memberId != null && !"".equals(memberId)){
            wrapper.and().eq("member_id",memberId);
        }
        if(status != null && !"".equals(status)){
            //wrapper.in("status",status);
            wrapper.and("status in ({0})",status);
        }
        wrapper.orderBy("create_time",false);
        Page<Map<String,Object>> pageDlbRecord = new Page<Map<String,Object>>();
        currentPage = (currentPage - 1) * pageSize;
        pageDlbRecord.setRecords(dlbaccountsMapper.findDlbRecordPagination(currentPage,pageSize,memberId,startTime,endTime,status));
        return pageDlbRecord;
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
                " count(eda_.id) as cnts " +
                " from es_dlbaccounts eda_" +
                " where eda_.status in(0,1) and eda_.dic_type = '"+DIC_TYPE_DRAW+"'" +
                " and eda_.member_id='"+memberId+"' "+
                " and DATE_SUB(CURDATE(), INTERVAL "+day+" DAY) <= date(eda_.create_time)";
        Map<String,Object> rMap = sqlMapper.selectOne(sql);
        if(rMap != null && !rMap.isEmpty() && rMap.get("cnts") != null && Integer.valueOf(rMap.get("cnts").toString()) > 0){
            bool = true;
        }
        return bool;
    }
}
