package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.didu.lotteryshop.common.service.form.impl.EsEthaccountsServiceImpl;
import com.didu.lotteryshop.lotteryb.entity.LotterybBuy;
import com.didu.lotteryshop.lotteryb.mapper.LotterybBuyMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybBuyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-18
 */
@Service
public class LotterybBuyServiceImpl extends ServiceImpl<LotterybBuyMapper, LotterybBuy> implements ILotterybBuyService {

    @Autowired
    private LotterybBuyMapper lotterybBuyMapper;

    /**
     * 根据期数Id查询本次购买的所有金额统计000
     * @param lotterybIssueId
     * @return
     */
    public BigDecimal findBuyStatistics(Integer lotterybIssueId){
        List<LotterybBuy> list = findBuy(lotterybIssueId);
        BigDecimal allTotal = BigDecimal.ZERO;
        for (LotterybBuy b : list) {
            allTotal = allTotal.add(b.getTotal());
        }
        return allTotal;
    }

    /**
     * 查询购买数据
     * @param lotterybIssueId 期数Id
     * @return
     */
    public List<LotterybBuy> findBuy(Integer lotterybIssueId){
        Wrapper<LotterybBuy> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_issue_id",lotterybIssueId);
        return  super.selectList(wrapper);
    }


    /**
     * 分页查询购买数据
     * @param currentPage
     * @param pageSize
     * @param mTransferStatus
     * @param lotterybBuy
     * @return
     */
    public List<Map<String,Object>> getPageLotteryBuyAll(Integer currentPage, Integer pageSize, String mTransferStatus, LotterybBuy lotterybBuy) {
        currentPage = (currentPage - 1) * pageSize;
        return lotterybBuyMapper.getPageLotteryBuyAll(currentPage,pageSize,mTransferStatus,lotterybBuy);
    }

    /**
     * 分页查询购买数据
     * @param currentPage
     * @param pageSize
     * @param mTransferStatus 状态格式 :1,2
     * @param lotterybBuy
     * @return
     */
    public Object getPageLotteryBuy(Integer currentPage, Integer pageSize, String mTransferStatus, LotterybBuy lotterybBuy) {
        Wrapper<LotterybBuy> wrapper = new EntityWrapper<>();
        //子查询字段
        wrapper.setSqlSelect();
        wrapper.and().and("1=1");
        if(StringUtils.isNotBlank(mTransferStatus)){
            wrapper.in("transfer_status",mTransferStatus);
        }
        if(lotterybBuy != null){
            if(lotterybBuy.getLotterybIssueId() != null)
                wrapper.and().eq("lotteryb_issue_id",lotterybBuy.getLotterybIssueId());
            if(lotterybBuy.getIsLuck() != null)
                wrapper.and().eq("is_luck",lotterybBuy.getIsLuck());
            if(lotterybBuy.getMemberId() != null )
                wrapper.and().eq("member_id",lotterybBuy.getMemberId());
        }
        wrapper.orderBy("createTime",false);
        Page<LotterybBuy> pageLB = new Page<>(currentPage,pageSize);
        return super.selectPage(pageLB,wrapper);
    }
}
