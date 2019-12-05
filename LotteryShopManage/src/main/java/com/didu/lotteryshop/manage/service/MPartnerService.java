package com.didu.lotteryshop.manage.service;

import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.entity.MPartner;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.mapper.MPartnerMapper;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MPartnerService {
    @Autowired
    private MPartnerMapper mPartnerMapper;
    @Autowired
    private LsImageMapper lsImageMapper;

    public List<Map<String,Object>> getPartnerList(){
        List<Map<String,Object>> list = mPartnerMapper.findPartnerList(MPartner.TYPE_PARTNER);
        return  list;
    }

    public List<Map<String,Object>> getExternalList(){
        List<Map<String,Object>> list = mPartnerMapper.findPartnerList(MPartner.TYPE_EXTERNAL);
        return  list;
    }

    public ResultUtil savePartner(String linkAddress, HttpServletRequest request){
        int i = save(linkAddress,request,Integer.parseInt(MPartner.TYPE_PARTNER));
        if(i == -1){
            return ResultUtil.successJson("error");
        }
        return  ResultUtil.successJson("success");
    }



    public ResultUtil saveExternal(String linkAddress, HttpServletRequest request){
        int i = save(linkAddress,request,Integer.parseInt(MPartner.TYPE_EXTERNAL));
        if(i == -1){
            return ResultUtil.successJson("error");
        }
        return  ResultUtil.successJson("success");
    }

    public ResultUtil updateSort(String id,String sort){
        MPartner mPartner = new MPartner();
        mPartner.setId(Integer.parseInt(id));
        mPartner.setSort(Integer.parseInt(sort));
        int i = mPartnerMapper.updateById(mPartner);
        if(i < 1){
            return ResultUtil.successJson("error");
        }
        return  ResultUtil.successJson("success");
    }

    public ResultUtil deleteMPartner(String id){
        if(id != null && !"".equals(id)) {
            int i = mPartnerMapper.deleteById(Integer.parseInt(id));
            if (i < 1) {
                return ResultUtil.successJson("success");
            }
        }
        return ResultUtil.successJson("error");
    }

    public Integer save(String linkAddress, HttpServletRequest request,Integer type){
        byte[] img = FileUtil.getFileByte(request);
            LsImage lsImage = new LsImage();
            lsImage.setType(2);
            lsImage.setByteData(img);
            Integer i = lsImageMapper.insert(lsImage);
            if(i < 1){
                return -1;
            }
            MPartner mPartner = new MPartner();
            mPartner.setLinkAddress(linkAddress);
            mPartner.setLsImageId(lsImage.getId().toString());
            mPartner.setCreateTime(new Date());
            mPartner.setType(type);
            i = mPartnerMapper.insert(mPartner);
            if(i < 1){
                return -1;
            }
        return 1;
    }
}
