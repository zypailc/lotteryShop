package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsGdethconfigWithdraw;
import com.didu.lotteryshop.common.mapper.EsGdethconfigWithdrawMapper;
import com.didu.lotteryshop.common.service.form.IEsGdethconfigWithdrawService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推广分成eth钱包配置结账时间表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-18
 */
@Service
public class EsGdethconfigWithdrawServiceImpl extends ServiceImpl<EsGdethconfigWithdrawMapper, EsGdethconfigWithdraw> implements IEsGdethconfigWithdrawService {
    /**
     * 根据活跃用户数查询推广分成eth钱包结账时间
     * @param activeMembers
     * @return
     */
    public EsGdethconfigWithdraw findEsGdethconfigWithdrawByActiveMembers(int activeMembers){
        Wrapper<EsGdethconfigWithdraw> wrapper = new EntityWrapper<>();
        wrapper.and("active_num <= {0}",activeMembers);
        wrapper.orderBy("active_num",false);
        wrapper.last("limit 1");
        return super.selectOne(wrapper);
    }
}
