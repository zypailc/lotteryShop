package com.didu.lotteryshop.common.service.form.impl;

import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.mapper.SysConfigMapper;
import com.didu.lotteryshop.common.service.form.ISysConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-25
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    /**
     * 获取系统配置
     * @return 返回系统配置实体
     */
    public SysConfig getSysConfig(){
        return super.selectById(1);
    }

}
