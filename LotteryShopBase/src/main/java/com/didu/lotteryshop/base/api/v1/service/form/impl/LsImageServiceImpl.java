package com.didu.lotteryshop.base.api.v1.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.base.api.v1.mapper.LsImageMapper;
import com.didu.lotteryshop.base.api.v1.mapper.MemberMapper;
import com.didu.lotteryshop.base.api.v1.service.form.ILsImageService;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.LsImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2019-10-29
 */
@Service
public class LsImageServiceImpl extends ServiceImpl<LsImageMapper, LsImage> implements ILsImageService {

    @Autowired
    private LsImageMapper imageMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BaseService baseService;

    /**
     * 按类别查询图片
     * @param type
     * @return
     */
    public List<LsImage> findImageType(Integer type){
        Map<String,Object> map = new HashMap<>();
        map.put("type",LsImage.IMAGE_TYPE);
        List<LsImage> list = selectByMap(map);
        return list;
    }





}
