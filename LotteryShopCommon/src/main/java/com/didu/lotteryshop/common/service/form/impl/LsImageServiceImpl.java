package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.service.form.ILsImageService;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2019-11-18
 */
@Service
public class LsImageServiceImpl extends ServiceImpl<LsImageMapper, LsImage> implements ILsImageService {



}
