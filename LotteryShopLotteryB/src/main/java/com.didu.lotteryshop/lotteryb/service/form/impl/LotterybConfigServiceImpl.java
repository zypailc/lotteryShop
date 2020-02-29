package com.didu.lotteryshop.lotteryb.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.lotteryb.entity.LotterybConfig;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.mapper.LotterybConfigMapper;
import com.didu.lotteryshop.lotteryb.service.form.ILotterybConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-20
 */
@Service
public class LotterybConfigServiceImpl extends ServiceImpl<LotterybConfigMapper, LotterybConfig> implements ILotterybConfigService {


    /**
     * 单
     */
    public static final  Integer SINGLE_ID = 1;
    /**
     * 双
     */
    public static final  Integer DOUBLE_ID = 2;
    /**
     * 大
     */
    public static final  Integer LARGE_ID = 3;
    /**
     * 小
     */
    public static final  Integer SMALL_ID = 4;


    public static  final Integer ID_1 = 4;

    public static  final Integer ID_2 = 5;

    public static final Integer  DIFFERENCE_VALUE = 2;

    public static final  Integer TYPE_1 = 1;
    public static final  Integer TYPE_value_1 = 0;
    public static final  String [] TYPE_COMBINATION_1 = {};

    public static final  Integer TYPE_2 = 2;
    public static final  Integer TYPE_VALUE_2 = 0;
    public static final  String [] TYPE_COMBINATION_2 = {};

    public static final  Integer TYPE_3 = 3;
    public static final  Integer TYPE_VALUE_3 = 0;
    public static final  String [] TYPE_COMBINATION_3 = {};

    public static final  Integer TYPE_4 = 4;
    public static final  Integer TYPE_VALUE_4 = 0;
    public static final  String [] TYPE_COMBINATION_4 = {};

    public static final  Integer TYPE_5 = 5;
    public static final  Integer TYPE_VALUE_5 = 3;
    public static final  String [] TYPE_COMBINATION_5 = {"111"};

    public static final  Integer TYPE_6 = 6;
    public static final  Integer TYPE_VALUE_6 = 4;
    public static final  String [] TYPE_COMBINATION_6 = {"112"};

    public static final  Integer TYPE_7 = 7;
    public static final  Integer TYPE_VALUE_7 = 5;
    public static final  String [] TYPE_COMBINATION_7 = {"122","113"};

    public static final  Integer TYPE_8 = 8;
    public static final  Integer TYPE_VALUE_8 = 6;
    public static final  String [] TYPE_COMBINATION_8 = {"114","123","222"};

    public static final  Integer TYPE_9 = 9;
    public static final  Integer TYPE_VALUE_9 = 7;
    public static final  String [] TYPE_COMBINATION_9 = {"114","124","133","223"};

    public static final  Integer TYPE_10 = 10;
    public static final  Integer TYPE_VALUE_10 = 8;
    public static final  String [] TYPE_COMBINATION_10 = {"116","125","134","224","233","332"};

    public static final  Integer TYPE_11 = 11;
    public static final  Integer TYPE_VALUE_11 = 9;
    public static final  String [] TYPE_COMBINATION_11 = {"126","135","144","225","234","333"};

    public static final  Integer TYPE_12 = 12;
    public static final  Integer TYPE_VALUE_12 = 10;
    public static final  String [] TYPE_COMBINATION_12 = {"136","145","226","235","244","334","442"};

    public static final  Integer TYPE_13 = 13;
    public static final  Integer TYPE_VALUE_13 = 11;
    public static final  String [] TYPE_COMBINATION_13 = {"146","155","164","236","245","335","344"};

    public static final  Integer TYPE_14 = 14;
    public static final  Integer TYPE_VALUE_14 = 12;
    public static final  String [] TYPE_COMBINATION_14 = {"156","246","255","336","345","444"};

    public static final  Integer TYPE_15 = 15;
    public static final  Integer TYPE_VALUE_15 = 13;
    public static final  String [] TYPE_COMBINATION_15 = {"166","256","346","355","445","553"};

    public static final  Integer TYPE_16 = 16;
    public static final  Integer TYPE_VALUE_16 = 14;
    public static final  String [] TYPE_COMBINATION_16 = {"266","356","446"};

    public static final  Integer TYPE_17 = 17;
    public static final  Integer TYPE_VALUE_17 = 15;
    public static final  String [] TYPE_COMBINATION_17 = {"366","456"};

    public static final  Integer TYPE_18 = 18;
    public static final  Integer TYPE_VALUE_18 = 16;
    public static final  String [] TYPE_COMBINATION_18 = {"466","556"};

    public static final  Integer TYPE_19 = 19;
    public static final  Integer TYPE_VALUE_19 = 17;
    public static final  String [] TYPE_COMBINATION_19 = {"566"};

    public static final  Integer TYPE_20 = 20;
    public static final  Integer TYPE_VALUE_20 = 18;
    public static final  String [] TYPE_COMBINATION_20 = {"666"};



    /**
     * 获取玩法配置
     * @return
     */
    public List<LotterybConfig> getConfig(){
        Wrapper<LotterybConfig> wrapper = new EntityWrapper<>();
        wrapper.orderBy("sort",true);
        return super.selectList(wrapper);
    }

    /**
     * 获取玩法配置 返回一个Map 键值是玩法ID
     * @return
     */
    public Map<String,Object> getConfigMap(){
        List<LotterybConfig> list = getConfig();
        Map<String,Object> map = new HashMap<>();
        for (LotterybConfig c : list) {
            map.put(c.getId().toString(),c.getLines());
        }
        return  map;
    }

    /**
     * 根据玩法配置id获取中奖号码
     * @param lotterybConfigId
     * @return
     */
    public String getLuckNum(Integer lotterybConfigId){
        LotterybConfig lotterybConfig = super.selectById(lotterybConfigId);
        Random random = new Random();
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_1){

        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_2){

        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_3){

        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_4){

        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_5){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_5[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_5.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_6){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_6[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_6.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_7){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_7[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_7.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_8){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_8[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_8.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_9){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_9[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_9.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_10){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_10[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_10.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_11){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_11[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_11.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_12){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_12[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_12.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_13){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_13[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_13.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_14){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_14[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_14.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_15){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_15[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_15.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_16){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_16[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_16.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_17){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_17[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_17.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_18){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_18[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_18.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_19){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_19[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_19.length + 1)];
        }
        if(lotterybConfig.getType() == LotterybConfigServiceImpl.TYPE_20){
            return LotterybConfigServiceImpl.TYPE_COMBINATION_20[random.nextInt(LotterybConfigServiceImpl.TYPE_COMBINATION_20.length + 1)];
        }
        return "";
    }

}
