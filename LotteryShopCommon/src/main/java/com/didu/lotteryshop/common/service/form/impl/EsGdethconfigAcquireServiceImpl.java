package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsGdethconfigAcquire;
import com.didu.lotteryshop.common.mapper.EsGdethconfigAcquireMapper;
import com.didu.lotteryshop.common.service.form.IEsGdethconfigAcquireService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推广分成eth钱包配置百分比分成表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-18
 */
@Service
public class EsGdethconfigAcquireServiceImpl extends ServiceImpl<EsGdethconfigAcquireMapper, EsGdethconfigAcquire> implements IEsGdethconfigAcquireService {
    /**
     * 根据活跃用户数查询推广分成eth钱包百分比分成配置
     * @param activeMembers
     * @return
     */
    public EsGdethconfigAcquire findEsGdethconfigAcquireByActiveMembers(int activeMembers){
        Wrapper<EsGdethconfigAcquire> wrapper = new EntityWrapper<>();
        wrapper.and("active_num <= {0}",activeMembers);
        wrapper.orderBy("active_num",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }
}
