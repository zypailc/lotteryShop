package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.EsEthaccounts;
import com.didu.lotteryshop.common.mapper.EsEthaccountsMapper;
import com.didu.lotteryshop.common.mapper.EsEthwalletMapper;
import com.didu.lotteryshop.common.service.form.IEsEthaccountsService;
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
    @Autowired
    private BaseService baseService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private SysTaskServiceImpl sysTaskService;
    @Autowired
    private EsEthaccountsMapper esEthaccountsMapper;

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
    /** 推广结算账款 **/
    public static String DIC_TYPE_GSA = "8";
    /** Lsb提现**/
    public static String DIC_TYPE_LSBTOETH = "9";
    /** Lsb充值 **/
    public static String DIC_TYPE_ETHTOLSB = "10";



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
     * 新增出账（成功）记录
     * @param memberId 用户ID
     * @param dicTypeValue sys_dic 字典表类型值
     * @param total 金额
     * @param operId  操作业务表主键ID
     * @param gasFee gas手续费
     * @return
     */
    public boolean addOutSuccess(String memberId,String dicTypeValue,BigDecimal total,String operId,BigDecimal gasFee){
        return this.add(memberId,dicTypeValue,TYPE_OUT,total,STATUS_SUCCESS,operId,gasFee,null);
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
     * @param  transferHashValue 转账事务哈希值
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
    private boolean add(String memberId, String dicTypeValue, int type,BigDecimal total,int status,String operId,BigDecimal gasFee,String transferHashValue){
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
                    //第一次消费奖励
                    sysTaskService.TaskFirstConsumption(memberId);
                    //第一次消费直属上级奖励
                    sysTaskService.TaskLowerFirstConsumption(memberId);
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
            esEthaccounts.setTransferHashValue(transferHashValue);
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
     * 修改账目记录（成功）
     * @param id 账目记录Id
     * @param dicTypeValue sys_dic 字典表类型值
     * @param gasFee 燃气费
     * @return
     */
    public boolean updateSuccess(Integer id,String dicTypeValue,BigDecimal gasFee){
        return  this.update(null,dicTypeValue,null,STATUS_SUCCESS,gasFee,id);
    }
    /**
     * 修改账目记录（成功）
     * @param id 账目记录Id
     * @param gasFee 燃气费
     * @return
     */
    public boolean updateSuccess(Integer id,BigDecimal gasFee){
        return  this.update(null,null,null,STATUS_SUCCESS,gasFee,id);
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
     * 修改账目记录（失败）
     * @param id 账目记录Id
     * @param dicTypeValue sys_dic 字典表类型值
     * @return
     */
    public boolean updateFail(Integer id,String dicTypeValue){
        return this.update(null,dicTypeValue,null,STATUS_FAIL,BigDecimal.ZERO,id);
    }
    /**
     * 修改账目记录（失败）
     * @param id 账目记录Id
     * @return
     */
    public boolean updateFail(Integer id){
        return this.update(null,null,null,STATUS_FAIL,BigDecimal.ZERO,id);
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
    private  boolean update(String memberId,String dicTypeValue,String operId,int status,BigDecimal gasFee,Integer id){
        boolean bool = false;
        EsEthaccounts  esEthaccounts = null;
        if(id != null && !"".equals(id)){
            esEthaccounts = super.selectById(id);
        }else {
            if(StringUtils.isBlank(dicTypeValue) || StringUtils.isBlank(operId)) return bool;
            Wrapper<EsEthaccounts> wrapper = new EntityWrapper<>();
            wrapper.eq("member_id",memberId).and().eq("dic_type",dicTypeValue).and().eq("oper_id",operId);
            esEthaccounts = super.selectOne(wrapper);
        }
        gasFee = gasFee == null ? BigDecimal.ZERO : gasFee;

        if(esEthaccounts != null &&  esEthaccounts.getStatus() != STATUS_FAIL && esEthaccounts.getStatus() != STATUS_SUCCESS){
            esEthaccounts.setStatus(status);
            esEthaccounts.setStatusTime(new Date());
            esEthaccounts.setGasFee(gasFee);
            BigDecimal balance = null;
            if(status == STATUS_SUCCESS){
                balance = esEthwalletService.updateBalance(gasFee,esEthaccounts.getMemberId(),false);
                if(balance == null) return bool;
                balance =  esEthwalletService.updateSubtractFreeze(esEthaccounts.getAmount(),esEthaccounts.getMemberId(),true);
                if(balance == null) return bool;
                //第一次消费奖励
                sysTaskService.TaskFirstConsumption(esEthaccounts.getMemberId());
                //第一次消费直属上级奖励
                sysTaskService.TaskLowerFirstConsumption(esEthaccounts.getMemberId());
            }else if(status == STATUS_FAIL){
                balance =  esEthwalletService.updateSubtractFreeze(esEthaccounts.getAmount(),esEthaccounts.getMemberId(),false);
                if(balance == null) return bool;
            }
            esEthaccounts.setBalance(balance);
          bool = super.updateById(esEthaccounts);
        }
        return bool;
    }

    /**
     * 查询出账账单未处理成功的数据；
     * @return
     */
    public List<EsEthaccounts> findOutBeingProcessed(String memberId){
        Wrapper<EsEthaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId)
                .and().eq("type",TYPE_OUT)
                .and().eq("status",STATUS_BEINGPROCESSED);
        return super.selectList(wrapper);
    }

    /**
     * 查询Eth记录里面所有待处理的数据
     */
    public List<EsEthaccounts> findSATransferStatusWait(){
        Wrapper<EsEthaccounts> wrapper = new EntityWrapper<>();
        //wrapper.and("transfer_hash_value is not null and transfer_hash_value<>''")
        wrapper.eq("type",TYPE_OUT).eq("status",STATUS_BEINGPROCESSED);
        return super.selectList(wrapper);
    }

    /**
     * 根据会员ID和天数查询消费总额
     * @param memberId
     * @param day
     * @return
     */
    public BigDecimal findConsumeTotalByDay(String memberId,int day){
        BigDecimal consumeTotal = BigDecimal.ZERO;
        SqlMapper sqlMapper =  baseService.getSqlMapper();
        String sql = "select sum(eea_.amount) as amountTotal  " +
                    " from es_ethaccounts as eea_" +
                    " where eea_.member_id='"+memberId+"' and eea_.type="+TYPE_OUT+" and eea_.status="+STATUS_SUCCESS+
                    " and eea_.dic_type<>'"+DIC_TYPE_DRAW+"'" +
                    " and  DATE_SUB(CURDATE(), INTERVAL "+day+" DAY) <= date(eea_.status_time)";
        Map<String,Object> rMap = sqlMapper.selectOne(sql);
        if(rMap != null && !rMap.isEmpty()){
            consumeTotal = new BigDecimal(rMap.get("amountTotal").toString());
        }
        return consumeTotal;
    }

    /**
     * 递归查询下级活跃用户
     * @param memberId 会员ID
     * @param consumeTotal 周期消费金额
     * @param level 循环层级
     * @param day 周期（天）
     * @param maxMembers 最多活跃用户
     * @param members 活跃用户
     * @return
     */
    //2019-12-17 lyl 注释，改方法调整到MemberServiceImpl类中
//    public int findActiveMembers(String memberId,BigDecimal consumeTotal,int level,int day,int maxMembers,int members){
//        List<Member> memberList = memberService.findLowerMembers(memberId);
//        if(memberList != null && memberList.size() > 0){
//            for(Member m : memberList){
//                if(consumeTotal.compareTo(this.findConsumeTotalByDay(m.getId(),day)) <= 0){
//                    members++;
//                    if(members >= maxMembers) return members;
//                }
//            }
//            if(level > 0){
//                level--;
//                for(Member m : memberList){
//                    members = this.findActiveMembers(m.getId(),consumeTotal,level,day,maxMembers,members);
//                    if(members >= maxMembers) return members;
//                }
//            }
//        }
//        return members;
//    }

    /**
     * 判断会員是否是第一次消费
     * @param memberId
     * @return
     */
    public boolean isFirstConsumption(String memberId){
        boolean bool = false;
        Wrapper<EsEthaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId)
                .and().eq("type",TYPE_OUT)
                .and().eq("status",STATUS_SUCCESS)
                .and("dic_type <> {0}",DIC_TYPE_DRAW);
        int c =  super.selectCount(wrapper);
        if(c == 0){
            bool = true;
        }
        return bool;
    }

    /**
     * 根據用戶Id查詢用戶的流水記錄
     * @param memberId
     * @param currentPage
     * @param pageSize
     * @param startTime 创建开始时间
     * @param endTime 创建结束时间
     * @param status  状态 1,2
     * @return
     */
    public Page<Map<String,Object>> findEthRecordPagination(String memberId,Integer currentPage,Integer pageSize,String startTime,String endTime,String status){
       /* Wrapper<EsEthaccounts> wrapper = new EntityWrapper<EsEthaccounts>();
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
        wrapper.orderBy("create_time",false);*/
        //Page<EsEthaccounts> pageEthRecord = new Page<EsEthaccounts>(currentPage,pageSize);
        Page<Map<String,Object>> pageEthRecord = new Page<>();
        currentPage = (currentPage - 1) * pageSize;
        pageEthRecord.setRecords(esEthaccountsMapper.findEthRecordPagination(currentPage,pageSize,startTime,endTime,memberId,status));
        return pageEthRecord;
    }

    /**
     * 根据operId查询数据
     * @param operId
     * @return
     */
    public EsEthaccounts findEsEthaccountsByOperId(String operId){
        Wrapper<EsEthaccounts> wrapper = new EntityWrapper<>();
        wrapper.eq("oper_id",operId);
        return super.selectOne(wrapper);
    }

}
