package com.didu.lotteryshop.base.api.v1.controller;

import com.didu.lotteryshop.base.controller.BaseBaseController;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.service.form.impl.LsImageServiceImpl;
import com.didu.lotteryshop.common.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/authorization/v1/lsimage")
public class LsImageAuthController extends BaseBaseController {

    @Autowired
    private LsImageServiceImpl lsImageService;

    @ResponseBody
    @RequestMapping("/getImg")
    public void getImg(String id){
        if(id != null && !"".equals(id)){
            LsImage lsImage = new LsImage();
            lsImage = lsImageService.selectById(Integer.parseInt(id));
            FileUtil.outImg(super.getResponse(),lsImage.getByteData());
        }
    }

}
