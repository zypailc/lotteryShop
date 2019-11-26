package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.IntroEn;
import com.didu.lotteryshop.common.entity.IntroZh;
import com.didu.lotteryshop.common.entity.MIntro;
import com.didu.lotteryshop.common.mapper.MIntroMapper;
import com.didu.lotteryshop.common.service.form.IMIntroService;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
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



}