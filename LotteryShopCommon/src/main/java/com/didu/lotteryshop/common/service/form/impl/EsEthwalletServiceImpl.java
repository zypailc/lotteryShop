package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsEthaccounts;
import com.didu.lotteryshop.common.entity.EsEthwallet;
import com.didu.lotteryshop.common.entity.SysConfig;
import com.didu.lotteryshop.common.mapper.EsEthwalletMapper;
import com.didu.lotteryshop.common.service.form.IEsEthwalletService;
import com.didu.lotteryshop.common.utils.ResultUtil;
import com.didu.lotteryshop.common.utils.Web3jUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * eth钱包 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-07
 */
@Service
public class EsEthwalletServiceImpl extends ServiceImpl<EsEthwalletMapper, EsEthwallet> implements IEsEthwalletService {
    @Autowired
    private SysConfigServiceImpl sysConfigService;
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;

    /**
     * 初始化生产用户ETH钱包
     * @param memberId 会员ID
     * @return 返回ETH钱包对象,返回null执行失败
     */
    public EsEthwallet initInsert(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        EsEthwallet esEthwallet = new EsEthwallet();
        esEthwallet.setMemberId(memberId);
        esEthwallet.setTotal(BigDecimal.ZERO);
        esEthwallet.setBalance(BigDecimal.ZERO);
        esEthwallet.setFreeze(BigDecimal.ZERO);
        esEthwallet.setCreateTime(new Date());
        esEthwallet.setUpdateTime(new Date());
        boolean bool = super.insert(esEthwallet);
        return bool ? esEthwallet : null;
    }

    /**
     * 根据用户ID，查询用户ETH钱包
     * @param memberId
     * @return
     */
    public EsEthwallet findByMemberId(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        Wrapper<EsEthwallet> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId);
        return super.selectOne(wrapper);
    }

    /**
     * 判断余额是否充足
     * @param memberId 会员ID
     * @param amount 金额
     * @return 返回true 充足，返回false 余额不足
     */
    public boolean judgeBalance(String memberId,BigDecimal amount){
        if(StringUtils.isBlank(memberId) || amount == null ) return false;
        EsEthwallet esEthwallet = this.findByMemberId(memberId);
        SysConfig sysConfig = sysConfigService.getSysConfig();
        BigDecimal gasPrice = sysConfig.getGasPrice();
        BigDecimal gasLimit = sysConfig.getGasLimit();
        //本次需要燃气费增加本次燃气
        BigDecimal allAmount = Web3jUtils.etherToAllEtherByBigDecimal(amount,gasPrice,gasLimit);
        //还未处理的出账数据每条增加当前燃气费
        List<EsEthaccounts> esEthaccountsList = esEthaccountsService.findOutBeingProcessed(memberId);
        if(esEthaccountsList != null && esEthaccountsList.size() > 0 ){
            BigDecimal gasAmount = Web3jUtils.gasToEtherByBigDecimal(gasPrice,gasLimit);
            gasAmount = gasAmount.multiply(BigDecimal.valueOf(esEthaccountsList.size()));
            allAmount = allAmount.add(gasAmount);
        }
        if(esEthwallet != null && esEthwallet.getBalance().compareTo(allAmount) >= 0){
            return true;
        }
        return false;
    }

    /**
     * 判断冻结金额是否充足
     * @param memberId 会员ID
     * @param amount 金额
     * @return 返回true 充足，返回false 冻结金额不足
     */
    public boolean judgeFreeze(String memberId,BigDecimal amount){
        if(StringUtils.isBlank(memberId) || amount == null ) return false;
        EsEthwallet esEthwallet = this.findByMemberId(memberId);
        if(esEthwallet != null && esEthwallet.getFreeze().compareTo(amount) >= 0){
            return true;
        }
        return false;
    }

    /**
     * 修改余额方法
     * @param balance 金额
     * @param memberId 会员ID
     * @param isAdd 是否是增加，反之减少
     * @return 返回账户可用余额，返回null时执行失败
     */
    public BigDecimal updateBalance(BigDecimal balance,String memberId,boolean isAdd){
        if(StringUtils.isBlank(memberId) || balance == null ) return null;
        EsEthwallet esEthwallet = this.findByMemberId(memberId);
        if(isAdd){
            esEthwallet.setTotal(esEthwallet.getTotal().add(balance));
            esEthwallet.setBalance(esEthwallet.getBalance().add(balance));
        }else{
            //账户余额，要大于出账金额
            // if(esEthwallet.getBalance().compareTo(freeze) != -1) {
            if(this.judgeBalance(memberId,balance)){
                esEthwallet.setBalance(esEthwallet.getBalance().subtract(balance));
                esEthwallet.setTotal(esEthwallet.getTotal().subtract(balance));
            }else{
                return null;
            }
        }
        esEthwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esEthwallet);
        return bool ? esEthwallet.getBalance() : null;
    }

    /**
     * 增加冻结金额
     * @param freeze 金额
     * @param memberId 会员ID
     * @return
     */
    public BigDecimal updateAddFreeze(BigDecimal freeze,String memberId){
        return this.updateFreeze(freeze,memberId,true,false);
    }
    /**
     *  减少冻结金额
     * @param freeze 金额
     * @param memberId 会员ID
     * @param isSuc 是否是成功出账，反之解冻
     * @return 返回可用余额，返回null时执行失败
     */
    public BigDecimal updateSubtractFreeze(BigDecimal freeze,String memberId,boolean isSuc){
        return this.updateFreeze(freeze,memberId,false,isSuc);
    }
    /**
     *  修改冻结金额
     * @param freeze 金额
     * @param memberId 会员ID
     * @param isAdd 是否是增加，反之减少
     * @param isSuc 当isAdd为false时生效，是否是成功出账，反之解冻
     * @return 返回可用余额，返回null时执行失败
     */
    private BigDecimal updateFreeze(BigDecimal freeze,String memberId,boolean isAdd,boolean isSuc){
        if(StringUtils.isBlank(memberId) || freeze == null ) return null;
        EsEthwallet esEthwallet = this.findByMemberId(memberId);
        if(isAdd){
            //账户余额，要大于出账金额
           // if(esEthwallet.getBalance().compareTo(freeze) != -1) {
            if(this.judgeBalance(memberId,freeze)){
                esEthwallet.setBalance(esEthwallet.getBalance().subtract(freeze));
                esEthwallet.setFreeze(esEthwallet.getFreeze().add(freeze));
            }else{
                return null;
            }
        }else {
          //  if (esEthwallet.getFreeze().compareTo(freeze) != -1) {
            if (this.judgeFreeze(memberId,freeze)) {
                //成功出账
                if (isSuc) {
                    esEthwallet.setTotal(esEthwallet.getTotal().subtract(freeze));
                    esEthwallet.setFreeze(esEthwallet.getFreeze().subtract(freeze));
                //解冻
                } else {
                    esEthwallet.setBalance(esEthwallet.getBalance().add(freeze));
                    esEthwallet.setFreeze(esEthwallet.getFreeze().subtract(freeze));
                }
             } else {
                return null;
            }
        }
        esEthwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esEthwallet);
        return bool ? esEthwallet.getBalance() : null;
    }




}
