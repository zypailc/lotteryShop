package com.didu.lotteryshop.manage.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.mapper.LsImageMapper;
import com.didu.lotteryshop.common.utils.FileUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LsImageService {

    @Autowired
    private LsImageMapper lsImageMapper;

    public List<LsImage> getHeadImgList(String type){
        Map<String ,Object> map = new HashMap<>();
        map.put("type",type);
        Wrapper wrapper = new EntityWrapper();
        wrapper.allEq(map);
        List<LsImage> lsImages = lsImageMapper.selectList(wrapper);
        return lsImages;
    }

    public ResultUtil saveFileImg(HttpServletRequest request) {
        LsImage lsImage = saveLsImage(request,LsImage.IMAGE_TYPE);
        if(lsImage == null){
            return  ResultUtil.errorJson("error");
        }
        return ResultUtil.successJson("success");
    }

    public ResultUtil deleteByid(String id){
        Integer i = lsImageMapper.deleteById(Integer.parseInt(id));
        if(i > 0){
            return ResultUtil.successJson("success");
        }
        return ResultUtil.errorJson("error");
    }

    public LsImage saveLsImage(HttpServletRequest request,Integer type){
        List<byte[]> list = FileUtil.getFileByteList(request);
        if (list != null && list.size() > 0) {
            for (byte[] b:list ) {
                LsImage lsImage = new LsImage();
                lsImage.setType(type);
                lsImage.setByteData(b);
                int i = lsImageMapper.insert(lsImage);
                if(i > 0){
                    return  lsImage;
                }
            }
        }
        return null;
    }

    public ResultUtil saveFileQRBackground(HttpServletRequest request,String qRbackgroundId){
        byte [] imgByte = FileUtil.getFileByte(request);
        LsImage lsImage = new LsImage();
        lsImage.setId(Integer.parseInt(qRbackgroundId));
        lsImage.setFileName("QR_Background");
        lsImage.setByteData(imgByte);
        int i = lsImageMapper.updateById(lsImage);
        if(i > 0){
            return  ResultUtil.successJson("success");
        }
        return ResultUtil.errorJson("error");
    }


}
