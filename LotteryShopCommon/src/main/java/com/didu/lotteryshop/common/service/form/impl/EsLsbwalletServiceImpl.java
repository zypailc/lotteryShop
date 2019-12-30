package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsLsbwallet;
import com.didu.lotteryshop.common.mapper.EsLsbwalletMapper;
import com.didu.lotteryshop.common.service.form.IEsLsbwalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 平台币钱包 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-29
 */
@Service
public class EsLsbwalletServiceImpl extends ServiceImpl<EsLsbwalletMapper, EsLsbwallet> implements IEsLsbwalletService {
    /**
     * 初始化生产用户平台币钱包
     * @param memberId 会员ID
     * @return 返回平台币钱包对象,返回null执行失败
     */
    public EsLsbwallet initInsert(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        EsLsbwallet esLsbwallet = new EsLsbwallet();
        esLsbwallet.setMemberId(memberId);
        esLsbwallet.setTotal(BigDecimal.ZERO);
        esLsbwallet.setBalance(BigDecimal.ZERO);
        esLsbwallet.setFreeze(BigDecimal.ZERO);
        esLsbwallet.setCreateTime(new Date());
        esLsbwallet.setUpdateTime(new Date());
        boolean bool = super.insert(esLsbwallet);
        return bool ? esLsbwallet : null;
    }

    /**
     * 根据用户ID，查询用户平台币钱包
     * @param memberId
     * @return
     */
    public EsLsbwallet findByMemberId(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        Wrapper<EsLsbwallet> wrapper = new EntityWrapper<>();
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
        EsLsbwallet esLsbwallet = this.findByMemberId(memberId);
        if(esLsbwallet != null && esLsbwallet.getBalance().compareTo(amount) >= 0){
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
        EsLsbwallet esLsbwallet = this.findByMemberId(memberId);
        if(esLsbwallet != null && esLsbwallet.getFreeze().compareTo(amount) >= 0){
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
        EsLsbwallet esLsbwallet = this.findByMemberId(memberId);
        if(isAdd){
            esLsbwallet.setTotal(esLsbwallet.getTotal().add(balance));
            esLsbwallet.setBalance(esLsbwallet.getBalance().add(balance));
        }else{
            //账户余额，要大于出账金额
            if(this.judgeBalance(memberId,balance)){
                esLsbwallet.setBalance(esLsbwallet.getBalance().subtract(balance));
                esLsbwallet.setTotal(esLsbwallet.getTotal().subtract(balance));
            }else{
                return null;
            }
        }
        esLsbwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esLsbwallet);
        return bool ? esLsbwallet.getBalance() : null;
    }

    /**
     * 增加冻结金额
     * @param freeze 金额
     * @param memberId 会员ID
     * @return
     */
    public BigDecimal updateAddFreeze(BigDecimal freeze,String memberId){
        return this.updateFreeze(freeze,memberId,true,false,null);
    }

    /**
     * 增加冻结金额 Eth 充值
     * @param freeze 金额
     * @param memberId 会员ID
     * @param type  0：出，1：入
     * @return
     */
    public BigDecimal updateAddFreeze(BigDecimal freeze,String memberId,Integer type){
        return this.updateFreeze(freeze,memberId,true,false,type);
    }

    /**
     *  减少冻结金额
     * @param freeze 金额
     * @param memberId 会员ID
     * @param isSuc 是否是成功出账，反之解冻
     * @return 返回可用余额，返回null时执行失败
     */
    public BigDecimal updateSubtractFreeze(BigDecimal freeze,String memberId,boolean isSuc){
        return this.updateFreeze(freeze,memberId,false,isSuc,null);
    }

    /**
     *  修改冻结金额
     * @param freeze 金额
     * @param memberId 会员ID
     * @param isAdd 是否是增加，反之减少
     * @param isSuc 当isAdd为false时生效，是否是成功出账，反之解冻
     * @param type 0：出，1：入
     * @return 返回可用余额，返回null时执行失败
     */
    private BigDecimal updateFreeze(BigDecimal freeze,String memberId,boolean isAdd,boolean isSuc,Integer type){
        if(StringUtils.isBlank(memberId) || freeze == null ) return null;
        EsLsbwallet esLsbwallet = this.findByMemberId(memberId);
        if(type == null){
            type = EsLsbaccountsServiceImpl.TYPE_OUT;
        }
        if(isAdd){
            if(type == EsLsbaccountsServiceImpl.TYPE_OUT){ // 出账
                //账户余额，要大于出账金额
                if(this.judgeBalance(memberId,freeze)){
                    esLsbwallet.setBalance(esLsbwallet.getBalance().subtract(freeze));
                    esLsbwallet.setFreeze(esLsbwallet.getFreeze().add(freeze));
                }else{
                    return null;
                }
            }else if(type == EsLsbaccountsServiceImpl.TYPE_IN){//进账充值 ，直接增加冻结金额
                esLsbwallet.setFreeze(esLsbwallet.getFreeze().add(freeze));
                esLsbwallet.setTotal(esLsbwallet.getTotal().add(freeze));
            }else {
                return  null;
            }

        }else {
            if (this.judgeFreeze(memberId,freeze)) {
                //成功出账
                if (isSuc) {
                    esLsbwallet.setTotal(esLsbwallet.getTotal().subtract(freeze));
                    esLsbwallet.setFreeze(esLsbwallet.getFreeze().subtract(freeze));
                    //解冻
                } else {
                    esLsbwallet.setBalance(esLsbwallet.getBalance().add(freeze));
                    esLsbwallet.setFreeze(esLsbwallet.getFreeze().subtract(freeze));
                }
            } else {
                return null;
            }
        }
        esLsbwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esLsbwallet);
        return bool ? esLsbwallet.getBalance() : null;
    }
}
