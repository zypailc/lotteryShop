package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.MIntro;
import com.didu.lotteryshop.common.mapper.MIntroMapper;
import com.didu.lotteryshop.common.service.form.IMIntroService;
import org.springframework.stereotype.Service;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2019-11-21
 */
@Service
public class MIntroServiceImpl extends ServiceImpl<MIntroMapper, MIntro> implements IMIntroService {

    public static final String[] LANGUAGES = new String[]{"zh", "en"};
    public static final String LANGUAGES_STR = "zh,en";
    /**
     * 项目特点
     */
    public static final Integer TYPE_CHARACTERISTIC_PROJECT = 1;

    /**
     * 资金分配
     */
    public static final Integer TYPE_ALLOCATION_FUNDS = 2;
    /**
     * 白皮书
     */
    public static final Integer TYPE_WHITE_BOOK = 3;
    /**
     * 公告
     */
    public static  final  Integer TYPE_NOTICE = 4;

    public static final  Integer TYPE_GENERALIZE = 5;

}
