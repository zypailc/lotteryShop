package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.entity.EsGdethconfigAcquire;
import com.didu.lotteryshop.lotteryb.entity.EsGdlsbconfigAcquire;
import com.didu.lotteryshop.lotteryb.mapper.EsGdlsbconfigAcquireMapper;
import com.didu.lotteryshop.lotteryb.service.form.IEsGdlsbconfigAcquireService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-03-03
 */
@Service
public class EsGdlsbconfigAcquireServiceImpl extends ServiceImpl<EsGdlsbconfigAcquireMapper, EsGdlsbconfigAcquire> implements IEsGdlsbconfigAcquireService {


    public EsGdlsbconfigAcquire findEsGdlsbconfigAcquireByActiveMembers(int activeMembers, Integer esGdlsbconfigId) {
        Wrapper<EsGdlsbconfigAcquire> wrapper = new EntityWrapper<>();
        wrapper.and("active_num <= {0}",activeMembers);
        wrapper.eq("es_gdlsbconfig_id",esGdlsbconfigId);
        wrapper.orderBy("active_num",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }
}
