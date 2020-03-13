package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.entity.EsGdethconfig;
import com.didu.lotteryshop.lotteryb.entity.EsGdlsbconfig;
import com.didu.lotteryshop.lotteryb.mapper.EsGdlsbconfigMapper;
import com.didu.lotteryshop.lotteryb.service.form.IEsGdlsbconfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-03-03
 */
@Service
public class EsGdlsbconfigServiceImpl extends ServiceImpl<EsGdlsbconfigMapper, EsGdlsbconfig> implements IEsGdlsbconfigService {



    /**
     * 根据玩法Id查询推广商提成配置信息
     * @param lotterybInfoId
     * @return
     */
    public EsGdlsbconfig findEsGdethconfig(Integer lotterybInfoId) {
        Wrapper<EsGdlsbconfig> wrapper = new EntityWrapper<>();
        wrapper.and().eq("lotteryb_info_id",lotterybInfoId);
        List<EsGdlsbconfig> list = super.selectList(wrapper);
        if(list.isEmpty() && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
