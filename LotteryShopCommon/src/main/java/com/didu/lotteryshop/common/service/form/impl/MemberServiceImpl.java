package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.mapper.MemberMapper;
import com.github.abel533.sql.SqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> {

    /**
     * 推广类型 ：推广商
     */
    public static final String GENERALIZE_TYPE_1 = "1";
    /**
     * 推广类型 ：散户
     */
    public static final String GENERALIZE_TYPE_0 = "0";

    @Autowired
    private BaseService baseService;

    /**
     * 查询会员下级活跃用户
     * @param memberId 会员ID
     * @param consumeTotal 周期消费金额
     * @param level 层级，如果为null，则不限制层级
     * @param day 周期（天）
     * @return
     */
    public int findActiveMembers(String memberId, BigDecimal consumeTotal, Integer level, int day){
        int cnts = 0;
        if(level != null){
            Member member = super.selectById(memberId);
            level += member.getGeneralizeMemberLevel() == null ? 0 : member.getGeneralizeMemberLevel();
        }
        String sql = " select count(1) as cnts from(" +
                            " select" +
                                 " sum(eea_.amount) as amountTotal " +
                            " from es_ethaccounts as eea_ " +
                            " left join es_member em_ on(eea_.member_id = em_.id )" +
                            " where  em_.generalize_member_ids like '%"+memberId+"%'";
                if(level != null) {
                         sql += " and em_.generalize_member_level <= " + level;
                }
                         sql += " and eea_.type = "+EsEthaccountsServiceImpl.TYPE_OUT+
                                " and eea_.status = "+EsEthaccountsServiceImpl.STATUS_SUCCESS+
                                " and eea_.dic_type<>'"+EsEthaccountsServiceImpl.DIC_TYPE_DRAW+"' " +
                                " and  DATE_SUB(CURDATE(), INTERVAL "+day+" DAY) <= date(eea_.status_time)" +
                            " GROUP BY eea_.member_id " +
                        ")as t where t.amountTotal >= "+consumeTotal;
        SqlMapper sqlMapper =  baseService.getSqlMapper();
        Map<String,Object> mMap = sqlMapper.selectOne(sql);
        if(mMap != null && !mMap.isEmpty()){
            return Integer.valueOf(mMap.get("cnts").toString()) ;
        }
        return cnts;
    }


    /**
     * 递归查询上级会员ID
     * @param memberId
     * @param level
     * @return
     */
    public String findLevelMemberId(String memberId,Integer level){
        SqlMapper sqlMapper =  baseService.getSqlMapper();
        String sql = "select generalize_member_id as parentId from es_member where id = '"+memberId+"'";
        Map<String,Object> mMap = sqlMapper.selectOne(sql);
        if(mMap != null && !mMap.isEmpty()){
            level--;
            if(level == 0){
                return mMap.get("parentId").toString();
            }else{
                return this.findLevelMemberId(mMap.get("parentId").toString(),level);
            }
        }
        return null;
    }

    /**
     * 查询下级会员
     * @param memberId
     * @return
     */
    public List<Member> findLowerMembers(String memberId){
        Wrapper<Member> wrapper = new EntityWrapper<>();
        wrapper.eq("generalize_member_id",memberId);
        return super.selectList(wrapper);
    }

    /**
     * 修改用戶信息
     * @param member
     * @return
     */
    public boolean updateMember(Member member){
        member.setUpdateTime(new Date());
        return  super.updateById(member);
    }

    /**
     * 查询推广商会员
     * @return
     */
    public List<Member> findGeneralizeMembers(){
        Wrapper<Member> wrapper = new EntityWrapper<>();
        wrapper.eq("generalize_type",1);
        return super.selectList(wrapper);
    }

    /**
     * 更新用户的登录信息
     * @param memberId
     * @param ip
     * @return
     */
    public boolean updateMemberLoginInfo(String memberId,String ip){
        Member member = new Member();
        member.setId(memberId);
        member.setLoginTime(new Date());
        member.setLoginIp(ip);
        return super.updateById(member);
    }

}
