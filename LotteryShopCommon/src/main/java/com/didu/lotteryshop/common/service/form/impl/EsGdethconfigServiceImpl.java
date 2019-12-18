package com.didu.lotteryshop.common.service.form.impl;

import com.didu.lotteryshop.common.entity.EsGdethconfig;
import com.didu.lotteryshop.common.mapper.EsGdethconfigMapper;
import com.didu.lotteryshop.common.service.form.IEsGdethconfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推广分成eth钱包分成配置表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-18
 */
@Service
public class EsGdethconfigServiceImpl extends ServiceImpl<EsGdethconfigMapper, EsGdethconfig> implements IEsGdethconfigService {
    /**
     * 推广分成eth钱包分成配置
     * @return
     */
    public EsGdethconfig findEsGdethconfig(){
        return super.selectById(1);
    }
}
