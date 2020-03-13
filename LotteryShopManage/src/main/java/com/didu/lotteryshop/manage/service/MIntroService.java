package com.didu.lotteryshop.manage.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.IntroEn;
import com.didu.lotteryshop.common.entity.IntroZh;
import com.didu.lotteryshop.common.entity.LsImage;
import com.didu.lotteryshop.common.entity.MIntro;
import com.didu.lotteryshop.common.mapper.MIntroMapper;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.github.abel533.sql.SqlMapper;
import jnr.ffi.annotations.In;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MIntroService extends BaseService {
    @Autowired
    private MIntroMapper mIntroMapper;
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private IntroEnServiceImpl introEnService;
    @Autowired
    private IntroZhServiceImpl introZhService;
    @Autowired
    private LsImageService lsImageService;
    @Autowired
    private EsMemberPropertiesServiceImpl esMemberPropertiesService;

    public List<Map<String,Object>> findMIntro(String type){
        Wrapper wrapper = new EntityWrapper();
        wrapper.orderBy("sort");
        wrapper.eq("type",type);

        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        String sql = "";
        String fild = "";
        String table = "";
        for (String s: MIntroServiceImpl.LANGUAGES) {
            fild += " ,m_"+s+".title as title_" + s + " , m_"+s+".content as content_"+s;
            table += " left join intro_"+s+" m_" + s + " on (mi_.language_id = m_"+s+".id) ";
        }
        sql = "select mi_.play_type as playType, mi_.id,li_.id as imgId,mi_.sort "+fild+" from m_intro mi_" +
                " left join ls_image li_ on (mi_.ls_image_id = li_.id) " + table + " where mi_.type = " + type;
        List<Map<String,Object>> mIntroList = sqlMapper.selectList(sql);
        return  mIntroList;
    }

    public ResultUtil saveCharacteristicProject(HttpServletRequest request, String title_zh, String content_zh, String title_en, String content_en,Integer type){
        LsImage lsImage = new LsImage();
        if(type == MIntroServiceImpl.TYPE_CHARACTERISTIC_PROJECT) {
            lsImage = lsImageService.saveLsImage(request, LsImage.PROJECT_TYPE);
            if (lsImage == null) {
                return ResultUtil.successJson("error !");
            }
        }
        if(type == MIntroServiceImpl.TYPE_NOTICE){
            //更新所有的用户公告为未查看
            getSqlMapper().update("update es_member_properties set is_view = 1 where type = " + EsMemberPropertiesServiceImpl.TYPE_NOTICE);
        }
        boolean b = save(title_zh,content_zh,title_en,content_en,type,lsImage.getId());
        if(b){
            return ResultUtil.successJson("success !");
        }
        return ResultUtil.successJson("error !");
    }

    public ResultUtil saveAllocationfunds(String title_zh,String content_zh,String title_en,String content_en,Integer type){
        boolean b = save(title_zh,content_zh,title_en,content_en,type,null);
        if(b){
            return ResultUtil.successJson("success !");
        }
        return ResultUtil.successJson("error !");
    }


    public ResultUtil updateCharacteristicProject(HttpServletRequest request,String id,String title_zh,String content_zh,String title_en,String content_en,String sort){
        LsImage lsImage = lsImageService.saveLsImage(request,LsImage.PROJECT_TYPE);
        boolean b;
        if(lsImage != null){
            b = update(id,title_zh,content_zh,title_en,content_en,sort,lsImage.getId());
        }else {
            b = update(id, title_zh, content_zh, title_en, content_en, sort, null);
        }
        if(b){
            return ResultUtil.successJson("success !");
        }
        return ResultUtil.successJson("error !");
    }

    public ResultUtil deleteMIntro(String id){
        int i = mIntroMapper.deleteById(Integer.parseInt(id));
        if(i < 1){
            return ResultUtil.successJson("error !");
        }
        return ResultUtil.successJson("success !");
    }

    public ResultUtil updateWhiteBoob(String id,String title_zh,String content_zh,String title_en,String content_en){
        IntroZh introZh = new IntroZh();
        introZh.setId(Integer.parseInt(id));
        introZh.setTitle(title_zh);
        introZh.setContent(content_zh);
        boolean b = introZhService.updateById(introZh);
        if(!b){
            return ResultUtil.successJson("error !");
        }
        IntroEn introEn = new IntroEn();
        introEn.setId(Integer.parseInt(id));
        introEn.setTitle(title_en);
        introEn.setContent(content_en);
        b = introEnService.updateById(introEn);
        if(!b){
            return ResultUtil.successJson("error !");
        }
        return ResultUtil.successJson("success !");
    }

    private Boolean update(String id,String title_zh,String content_zh,String title_en,String content_en,String sort,Integer lsImageId){
        MIntro mIntro = new MIntro();
        mIntro.setId(Integer.parseInt(id));
        mIntro.setSort(Integer.parseInt(sort));
        mIntro.setLsImageId(lsImageId);
        int i = mIntroMapper.updateById(mIntro);
        if(i < 1){
            return false;
        }
        IntroZh introZh = new IntroZh();
        introZh.setId(Integer.parseInt(id));
        introZh.setTitle(title_zh);
        introZh.setContent(content_zh);
        boolean b = introZhService.updateById(introZh);
        if(!b){
            return false;
        }
        IntroEn introEn = new IntroEn();
        introEn.setId(Integer.parseInt(id));
        introEn.setTitle(title_en);
        introEn.setContent(content_en);
        b = introEnService.updateById(introEn);
        if(!b){
            return  false;
        }
        return true;
    }

    private Boolean save(String title_zh,String content_zh,String title_en,String content_en,Integer type,Integer lsImageId){
        MIntro mIntro = new MIntro();
        mIntro.setType(type);
        mIntro.setCreateTime(new Date());
        mIntro.setLsImageId(lsImageId);
        int i = mIntroMapper.insert(mIntro);
        if(i < 1){
            return false;
        }
        //保存语言
        mIntro.setLanguageId(mIntro.getId());
        mIntroMapper.updateById(mIntro);
        IntroZh introZh = new IntroZh();
        introZh.setId(mIntro.getId());
        introZh.setTitle(title_zh);
        introZh.setContent(content_zh);
        boolean b = introZhService.insert(introZh);
        if(!b){
            return false;
        }
        IntroEn introEn = new IntroEn();
        introEn.setId(mIntro.getId());
        introEn.setTitle(title_en);
        introEn.setContent(content_en);
        b = introEnService.insert(introEn);
        if(!b){
            return false;
        }
        return true;
    }
}
