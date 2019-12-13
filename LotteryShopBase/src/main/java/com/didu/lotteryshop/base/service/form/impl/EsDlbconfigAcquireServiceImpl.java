package com.didu.lotteryshop.base.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.base.entity.EsDlbconfigAcquire;
import com.didu.lotteryshop.base.mapper.EsDlbconfigAcquireMapper;
import com.didu.lotteryshop.base.service.form.IEsDlbconfigAcquireService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台待领币配置百分比提取表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-04
 */
@Service
public class EsDlbconfigAcquireServiceImpl extends ServiceImpl<EsDlbconfigAcquireMapper, EsDlbconfigAcquire> implements IEsDlbconfigAcquireService {
    /**
     * 查询最大活跃会员数
     * @return
     */
    public int findMaxActiveMembers(){
        Wrapper<EsDlbconfigAcquire> wrapper = new EntityWrapper<>();
        wrapper.orderBy("active_num",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper).getActiveNum();
    }

    /**
     * 根据活跃用户数查询待领币百分比提取配置
     * @param activeMembers
     * @return
     */
    public EsDlbconfigAcquire findEsDlbconfigAcquireByActiveMembers(int activeMembers){
        Wrapper<EsDlbconfigAcquire> wrapper = new EntityWrapper<>();
        wrapper.and("active_num <= {0}",activeMembers);
        wrapper.orderBy("active_num",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }
}
