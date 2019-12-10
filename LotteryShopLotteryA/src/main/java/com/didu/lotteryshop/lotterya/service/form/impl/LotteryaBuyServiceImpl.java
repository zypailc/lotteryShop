package com.didu.lotteryshop.lotterya.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.lotterya.entity.LotteryaBuy;
import com.didu.lotteryshop.lotterya.entity.LotteryaInfo;
import com.didu.lotteryshop.lotterya.mapper.LotteryaBuyMapper;
import com.didu.lotteryshop.lotterya.service.form.ILotteryaBuyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * A彩票购买记录 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-11
 */
@Service
public class LotteryaBuyServiceImpl extends ServiceImpl<LotteryaBuyMapper, LotteryaBuy> implements ILotteryaBuyService {
    @Autowired
    private LotteryaPmDetailServiceImpl lotteryaPmDetailService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private LotteryaInfoServiceImpl lotteryaInfoService;
    /** 等待确认 */
    public static final String TRANSFER_STATUS_WAIT = "0";
    /** 已经确认 */
    public static final String TRANSFER_STATUS_SUCCESS = "1";
    /** 失败 */
    public static final String TRANSFER_STATUS_FAIL = "2";

    /**
     * 根据主键ID查询数据
     * @param id 主键ID
     * @return LotteryaBuy
     */
    public LotteryaBuy findById(Integer id){
        if(id == null) return null;
        return super.selectById(id);
    }

    /**
     * 修改购买彩票状态
     * @param id 主键ID
     * @param transferStatus 状态
     * @param gasFee gas燃气费
     * @return
     */
    public boolean updateLotteryABuyTransferStatus(Integer id, String transferStatus, BigDecimal gasFee){
        boolean bool = false;
        if(id == null || StringUtils.isBlank(transferStatus)) return bool;
        LotteryaBuy lotteryaBuy = this.findById(id);
        if(lotteryaBuy != null){
            lotteryaBuy.setTransferStatus(transferStatus);
            lotteryaBuy.setTransferStatusTime(new Date());
            bool = super.updateAllColumnById(lotteryaBuy);
            if(bool){
                if(transferStatus.equals(LotteryaBuyServiceImpl.TRANSFER_STATUS_SUCCESS)){
                    bool = esEthaccountsService.updateSuccess(lotteryaBuy.getMemberId(),EsEthaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA,lotteryaBuy.getId().toString(),gasFee);
                    //购买提成
                    if(bool){
                        LotteryaInfo lotteryaInfo = lotteryaInfoService.findLotteryaInfo();
                        bool = lotteryaPmDetailService.buyPM(lotteryaBuy,lotteryaInfo);
                    }
                }
                if(transferStatus.equals(LotteryaBuyServiceImpl.TRANSFER_STATUS_FAIL)){
                    bool = esEthaccountsService.updateFail(lotteryaBuy.getMemberId(),EsEthaccountsServiceImpl.DIC_TYPE_BUYLOTTERYA,lotteryaBuy.getId().toString());
                }
            }
        }
        return bool;
    }

    /**
     * 查询等待处理的购买彩票数据
     * @return List<LotteryaBuy>
     */
    public List<LotteryaBuy> findTransferStatusWait(){
        Wrapper<LotteryaBuy> wrapper = new EntityWrapper<>();
        wrapper.eq("transfer_status",LotteryaBuyServiceImpl.TRANSFER_STATUS_WAIT);
        return super.selectList(wrapper);
    }

    /**
     * 根据期数主键ID&中奖号码，查询中奖用户
     * @param lotteryaIssueId 期数ID
     * @param luckNum 开奖号码
     * @return  List<LotteryaBuy>
     */
    public List<LotteryaBuy> findLuckLotteryaBuy(Integer lotteryaIssueId,String luckNum){
        Wrapper<LotteryaBuy> wrapper = new EntityWrapper<>();
        wrapper.eq("transfer_status",LotteryaBuyServiceImpl.TRANSFER_STATUS_SUCCESS)
        .and().eq("lotterya_issue_id",lotteryaIssueId)
        .and().eq("luck_num",luckNum);
        return super.selectList(wrapper);
    }

    /**
     * 更新中奖状态
     * @param lotteryaIssueId
     * @param luckNum
     * @param lotteryaInfo
     * @return
     */
    public boolean updateIsLuck(Integer lotteryaIssueId, String luckNum, LotteryaInfo lotteryaInfo){
        boolean bool = false;
        //单注奖金，公式：(price*1000)/100*50
        BigDecimal pTotal = lotteryaInfoService.calculateSingleBonus(lotteryaInfo);
        List<LotteryaBuy> lotteryaBuyList = this.findLuckLotteryaBuy(lotteryaIssueId,luckNum);
        if(lotteryaBuyList != null && lotteryaBuyList.size() > 0){
            for(LotteryaBuy lb : lotteryaBuyList){
                lb.setIsLuck("1");
                BigDecimal luckTotal = pTotal.multiply(new BigDecimal(lb.getMultipleNum()));
                lb.setLuckTotal(luckTotal);
                bool =  super.updateAllColumnById(lb);
                //新增流水账记录
                if(bool)
                bool =  esEthaccountsService.addInSuccess(lb.getMemberId(),EsEthaccountsServiceImpl.DIC_TYPE_WINLOTTERYA,luckTotal,lb.getId().toString());
                //中奖分成
                if(bool)
                bool = lotteryaPmDetailService.drawPM(lb,lotteryaInfo);
                if(!bool) return false;
;            }
            bool = true;
        }
        return bool;
    }

    /**
     * 查询购买总笔数
     * @param memberId
     * @param lotteryAIssueId
     * @return
     */
    public int getBuyCount(String memberId,Integer lotteryAIssueId){
        Wrapper<LotteryaBuy> wrapper = new EntityWrapper<>();
        wrapper.eq("transfer_status",LotteryaBuyServiceImpl.TRANSFER_STATUS_SUCCESS)
            .and().eq("lotterya_issue_id",lotteryAIssueId)
            .and().eq("member_id",memberId);
        return super.selectCount(wrapper);
    }

    /**
     * 分页查询购买数据
     * @param currentPage
     * @param pageSize
     * @param lotteryaBuy
     * @return
     */
    public Page<LotteryaBuy> getPageLotteryBuy(Integer currentPage, Integer pageSize,LotteryaBuy lotteryaBuy){
        Wrapper<LotteryaBuy> wrapper = new EntityWrapper<>();
        //子查询字段
        // wrapper.setSqlSelect();
        wrapper.and("transfer_status<>{0}","2");
        if(lotteryaBuy != null){
            if(lotteryaBuy.getLotteryaIssueId() != null)
                wrapper.and().eq("lotterya_issue_id",lotteryaBuy.getLotteryaIssueId());
            if(lotteryaBuy.getIsLuck() != null)
                wrapper.and().eq("is_luck",lotteryaBuy.getIsLuck());
            if(lotteryaBuy.getMemberId() != null )
                wrapper.and().eq("member_id",lotteryaBuy.getMemberId());
        }
        wrapper.orderBy("createTime",false);
        Page<LotteryaBuy> pageLB = new Page<>(currentPage,pageSize);
        return super.selectPage(pageLB,wrapper);
    }

}
