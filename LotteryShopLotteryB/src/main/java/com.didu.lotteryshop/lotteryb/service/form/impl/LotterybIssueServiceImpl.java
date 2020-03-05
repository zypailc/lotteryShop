package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.utils.DateUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.mapper.LotterybIssueMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybIssueService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-20
 */
@Service
public class LotterybIssueServiceImpl extends ServiceImpl<LotterybIssueMapper, LotterybIssue> implements ILotterybIssueService {

    /**
     * 购买状态 0 开启
     */
    public static final int BY_STATUS_START = 0;

    /**
     * 购买状态 1 关闭
     */
    public static final  int BY_STATUS_OFF = 1;

    /**
     * 解析期数返回日期数字
     */
    public static final  int TYPE_DATE = 1;

    /**
     * 解析期数返回数字
     */
    public static final  int TYPE_NUM = 2;

    /**
     * 根据玩法Id获取期数
     * @return
     */
    public LotterybIssue getLotterybIssue(Integer lotteryInfoId){
        Wrapper<LotterybIssue> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotteryInfoId);
        wrapper.orderBy("issue_num",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }

    /**
     * 生成下一期期数
     * @param lotterybInfoId 玩法Id
     * @param issueNum 期数
     * @return
     */
    public String createIssueNum(Integer lotterybInfoId,String issueNum){
        String newDateStr = DateUtil.getDateFormat("yyyyMMdd");
        if(issueNum == null || "".equals(issueNum))
            return newDateStr + "00001";
        //解析期数
        String issueDate = issueNum.substring(0,8);
        String issueNumber = issueNum.substring(8,issueNum.length());
        Integer num = Integer.parseInt(issueNumber) + 1;
        String.format("%04d", num);//补位数字长度4位
        // 判断时间和今天是否一致
        if(!newDateStr.equals(issueDate)){
            issueDate = newDateStr;
        }
        if(lotterybInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_1)){
            return issueDate + String.format("%04d", num);
        }
        if(lotterybInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_3)){
            return issueDate + String.format("%04d", num);
        }
        if(lotterybInfoId.equals(LotterybInfoServiceImpl.TYPE_ID_5)){
            return issueDate + String.format("%04d", num);
        }
        return "";
    }

    /**
     * 解析期数Id
     * @param issueNum
     * @param type type == 1 return 日期数字 type == 2 return 数字
     * @return
     */
    public Integer  parseIssueNum(String issueNum,Integer type){
        String issueDate = issueNum.substring(0,8);
        String issueNumber = issueNum.substring(8,issueNum.length());
        if(type == TYPE_DATE){
            return Integer.parseInt(issueDate);
        }
        if(type == TYPE_NUM){
            return Integer.parseInt(issueNumber);
        }
        return null;
    }
}
