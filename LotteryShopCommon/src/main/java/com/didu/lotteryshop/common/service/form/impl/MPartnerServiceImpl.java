package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.entity.MPartner;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.mapper.MPartnerMapper;
import com.didu.lotteryshop.common.service.form.IMPartnerService;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2019-11-19
 */
@Service
public class MPartnerServiceImpl extends ServiceImpl<MPartnerMapper, MPartner> implements IMPartnerService {

}
