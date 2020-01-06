package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.entity.EsMemberProperties;
import com.didu.lotteryshop.common.mapper.EsMemberPropertiesMapper;
import com.didu.lotteryshop.common.service.form.IEsMemberPropertiesService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-01-03
 */
@Service
public class EsMemberPropertiesServiceImpl extends ServiceImpl<EsMemberPropertiesMapper, EsMemberProperties> implements IEsMemberPropertiesService {

    /**
     * 钱
     */
    public static final  Integer TYPE_MONEY = 1;

    /**
     * 公告
     */
    public static final  Integer TYPE_NOTICE = 2;

    /**
     * 修改金额是否显示
     * @param isView
     * @return
     */
    public ResultUtil updateMoneyIsView(String memberId,String isView){
       boolean b = update(null,memberId,isView,EsMemberPropertiesServiceImpl.TYPE_MONEY,null);
       if(b){
            return ResultUtil.successJson("success");
       }
        return ResultUtil.errorJson("error");
    }

    /**
     * 修改公告为已读
     * @param memberId
     * @return
     */
    public ResultUtil updateNoticeIsView(String memberId){
        boolean b = this.update(null,memberId,"0",EsMemberPropertiesServiceImpl.TYPE_NOTICE,null);
        if(b){
            return ResultUtil.successJson("success");
        }
        return ResultUtil.errorJson("error");
    }

    private boolean update(String id,String memberId,String isView,Integer type,String relevanceId){
        EsMemberProperties esMemberProperties = null;
        if(id != null && !"".equals(id)){
            esMemberProperties = super.selectById(id);
        }else if(memberId != null && !"".equals(memberId)){
            Wrapper<EsMemberProperties> wrapper = new EntityWrapper<>();
            wrapper.and().eq("member_id",memberId);
            wrapper.and().eq("type",type);
            if(relevanceId != null && !"".equals(relevanceId)){
                wrapper.and().eq("relevance_id",relevanceId);
            }
            esMemberProperties = super.selectOne(wrapper);
        }else {
            return false;
        }
        if(esMemberProperties != null){
            esMemberProperties.setIsView(Integer.parseInt(isView));
            boolean b = super.updateById(esMemberProperties);
            if(b){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
	
}
