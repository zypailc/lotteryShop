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

import java.util.List;
import java.util.Map;


@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> {
    @Autowired
    private BaseService baseService;
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
}
