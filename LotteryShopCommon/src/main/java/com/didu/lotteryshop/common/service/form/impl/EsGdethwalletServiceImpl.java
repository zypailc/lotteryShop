package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsGdethwallet;
import com.didu.lotteryshop.common.mapper.EsGdethwalletMapper;
import com.didu.lotteryshop.common.service.form.IEsGdethwalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 推广分成eth钱包 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-17
 */
@Service
public class EsGdethwalletServiceImpl extends ServiceImpl<EsGdethwalletMapper, EsGdethwallet> implements IEsGdethwalletService {
    /**
     * 初始化生产用户推广分成ETH钱包
     * @param memberId 会员ID
     * @return 返回ETH钱包对象,返回null执行失败
     */
    public EsGdethwallet initInsert(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        EsGdethwallet esGdethwallet = new EsGdethwallet();
        esGdethwallet.setMemberId(memberId);
        esGdethwallet.setTotal(BigDecimal.ZERO);
        esGdethwallet.setBalance(BigDecimal.ZERO);
        esGdethwallet.setFreeze(BigDecimal.ZERO);
        esGdethwallet.setCreateTime(new Date());
        esGdethwallet.setUpdateTime(new Date());
        boolean bool = super.insert(esGdethwallet);
        return bool ? esGdethwallet : null;
    }

    /**
     * 根据用户ID，查询用户推广分成ETH钱包
     * @param memberId
     * @return
     */
    public EsGdethwallet findByMemberId(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        Wrapper<EsGdethwallet> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id",memberId);
        EsGdethwallet gdethwallet = super.selectOne(wrapper);
        if(gdethwallet == null || gdethwallet.getId() == null){
            gdethwallet = this.initInsert(memberId);
        }
        return gdethwallet;
    }

    /**
     * 判断余额是否充足
     * @param memberId 会员ID
     * @param amount 金额
     * @return 返回true 充足，返回false 余额不足
     */
    public boolean judgeBalance(String memberId,BigDecimal amount){
        if(StringUtils.isBlank(memberId) || amount == null ) return false;
        EsGdethwallet esGdethwallet = this.findByMemberId(memberId);
        if(esGdethwallet != null && esGdethwallet.getBalance().compareTo(amount) >= 0){
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
        EsGdethwallet esGdethwallet = this.findByMemberId(memberId);
        if(esGdethwallet != null && esGdethwallet.getFreeze().compareTo(amount) >= 0){
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
        EsGdethwallet esGdethwallet = this.findByMemberId(memberId);
        if(isAdd){
            esGdethwallet.setTotal(esGdethwallet.getTotal().add(balance));
            esGdethwallet.setBalance(esGdethwallet.getBalance().add(balance));
        }else{
            //账户余额，要大于出账金额
            // if(esEthwallet.getBalance().compareTo(freeze) != -1) {
            if(this.judgeBalance(memberId,balance)){
                esGdethwallet.setBalance(esGdethwallet.getBalance().subtract(balance));
                esGdethwallet.setTotal(esGdethwallet.getTotal().subtract(balance));
            }else{
                return null;
            }
        }
        esGdethwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esGdethwallet);
        return bool ? esGdethwallet.getBalance() : null;
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
        EsGdethwallet esGdethwallet = this.findByMemberId(memberId);
        if(isAdd){
            //账户余额，要大于出账金额
            // if(esEthwallet.getBalance().compareTo(freeze) != -1) {
            if(this.judgeBalance(memberId,freeze)){
                esGdethwallet.setBalance(esGdethwallet.getBalance().subtract(freeze));
                esGdethwallet.setFreeze(esGdethwallet.getFreeze().add(freeze));
            }else{
                return null;
            }
        }else {
            //  if (esEthwallet.getFreeze().compareTo(freeze) != -1) {
            if (this.judgeFreeze(memberId,freeze)) {
                //成功出账
                if (isSuc) {
                    esGdethwallet.setTotal(esGdethwallet.getTotal().subtract(freeze));
                    esGdethwallet.setFreeze(esGdethwallet.getFreeze().subtract(freeze));
                    //解冻
                } else {
                    esGdethwallet.setBalance(esGdethwallet.getBalance().add(freeze));
                    esGdethwallet.setFreeze(esGdethwallet.getFreeze().subtract(freeze));
                }
            } else {
                return null;
            }
        }
        esGdethwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esGdethwallet);
        return bool ? esGdethwallet.getBalance() : null;
    }

    /**
     * 查询推广分成eth钱包 > 0 的所有账户
     * @return
     */
    public List<EsGdethwallet> findBalanceGtZero(){
        Wrapper<EsGdethwallet> wrapper = new EntityWrapper<>();
        wrapper.and("balance > {0}",0);
        return super.selectList(wrapper);
    }

}
