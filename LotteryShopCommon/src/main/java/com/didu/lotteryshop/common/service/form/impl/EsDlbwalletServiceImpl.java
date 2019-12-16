package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.EsDlbwallet;
import com.didu.lotteryshop.common.mapper.EsDlbwalletMapper;
import com.didu.lotteryshop.common.service.form.IEsDlbwalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 待领币钱包 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-03
 */
@Service
public class EsDlbwalletServiceImpl extends ServiceImpl<EsDlbwalletMapper, EsDlbwallet> implements IEsDlbwalletService {
    /**
     * 初始化生产用户平台币钱包
     * @param memberId 会员ID
     * @return 返回平台币钱包对象,返回null执行失败
     */
    public EsDlbwallet initInsert(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        EsDlbwallet esDlbwallet = new EsDlbwallet();
        esDlbwallet.setMemberId(memberId);
        esDlbwallet.setTotal(BigDecimal.ZERO);
        esDlbwallet.setBalance(BigDecimal.ZERO);
        esDlbwallet.setFreeze(BigDecimal.ZERO);
        esDlbwallet.setCreateTime(new Date());
        esDlbwallet.setUpdateTime(new Date());
        boolean bool = super.insert(esDlbwallet);
        return bool ? esDlbwallet : null;
    }

    /**
     * 根据用户ID，查询用户平台币钱包
     * @param memberId
     * @return
     */
    public EsDlbwallet findByMemberId(String memberId){
        if(StringUtils.isBlank(memberId)) return null;
        Wrapper<EsDlbwallet> wrapper = new EntityWrapper<>();
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
        EsDlbwallet esDlbwallet = this.findByMemberId(memberId);
        if(esDlbwallet != null && esDlbwallet.getBalance().compareTo(amount) >= 0){
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
        EsDlbwallet esDlbwallet = this.findByMemberId(memberId);
        if(esDlbwallet != null && esDlbwallet.getFreeze().compareTo(amount) >= 0){
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
        EsDlbwallet esDlbwallet = this.findByMemberId(memberId);
        if(isAdd){
            esDlbwallet.setTotal(esDlbwallet.getTotal().add(balance));
            esDlbwallet.setBalance(esDlbwallet.getBalance().add(balance));
        }else{
            //账户余额，要大于出账金额
            if(this.judgeBalance(memberId,balance)){
                esDlbwallet.setBalance(esDlbwallet.getBalance().subtract(balance));
                esDlbwallet.setTotal(esDlbwallet.getTotal().subtract(balance));
            }else{
                return null;
            }
        }
        esDlbwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esDlbwallet);
        return bool ? esDlbwallet.getBalance() : null;
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
        EsDlbwallet esDlbwallet = this.findByMemberId(memberId);
        if(isAdd){
            //账户余额，要大于出账金额
            if(this.judgeBalance(memberId,freeze)){
                esDlbwallet.setBalance(esDlbwallet.getBalance().subtract(freeze));
                esDlbwallet.setFreeze(esDlbwallet.getFreeze().add(freeze));
            }else{
                return null;
            }
        }else {
            if (this.judgeFreeze(memberId,freeze)) {
                //成功出账
                if (isSuc) {
                    esDlbwallet.setTotal(esDlbwallet.getTotal().subtract(freeze));
                    esDlbwallet.setFreeze(esDlbwallet.getFreeze().subtract(freeze));
                    //解冻
                } else {
                    esDlbwallet.setBalance(esDlbwallet.getBalance().add(freeze));
                    esDlbwallet.setFreeze(esDlbwallet.getFreeze().subtract(freeze));
                }
            } else {
                return null;
            }
        }
        esDlbwallet.setUpdateTime(new Date());
        boolean bool = super.updateById(esDlbwallet);
        return bool ? esDlbwallet.getBalance() : null;
    }

    /**
     * 查询dlb余额 >= 10 的所有账户
     * @return
     */
    public List<EsDlbwallet> findBalanceGtTen(){
        Wrapper<EsDlbwallet> wrapper = new EntityWrapper<>();
        wrapper.and("balance >= {0}",10);
        return super.selectList(wrapper);
    }
}
