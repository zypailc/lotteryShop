package com.didu.lotteryshop.base.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.base.entity.EsDlbconfig;
import com.didu.lotteryshop.base.mapper.EsDlbconfigMapper;
import com.didu.lotteryshop.base.service.form.IEsDlbconfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台待领币提取配置表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-04
 */
@Service
public class EsDlbconfigServiceImpl extends ServiceImpl<EsDlbconfigMapper, EsDlbconfig> implements IEsDlbconfigService {
    /**
     * 查询待领币配置
     * @return
     */
	public EsDlbconfig findEsDlbconfig(){
	    return super.selectById(1);
    }
}
