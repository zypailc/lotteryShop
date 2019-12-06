package com.didu.lotteryshop.common.service.form.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.entity.SysTask;
import com.didu.lotteryshop.common.mapper.SysTaskMapper;
import com.didu.lotteryshop.common.service.form.ISysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统任务配置表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-05
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTask> implements ISysTaskService {
    @Autowired
    private EsEthaccountsServiceImpl esEthaccountsService;
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;
    @Autowired
    private MemberServiceImpl memberService;
    /**
     * 注册送待领币
     * @return
     */
    public SysTask findRegister(){
        return super.selectById(1);
    }

    /**
     * 第一次消费送
     * @return
     */
    public SysTask findFirstConsumption(){
        return super.selectById(2);
    }
    /**
     * 直属下级第一消费送
     * @return
     */
    public SysTask findLowerFirstConsumption(){
        return super.selectById(3);
    }

    /**
     * 注册送待领币
     * @param memberId
     * @return
     */
    public boolean TaskRegister(String memberId) {
        boolean bool = false;
        SysTask sysTask = this.findRegister();
        if (sysTask.getStatus() == 1) {
            bool = esDlbaccountsService.addInSuccess(memberId, EsDlbaccountsServiceImpl.DIC_TYPE_REGISTER, sysTask.getDlb(), "-1");
        }else{
            bool = true;
        }
        return bool;
    }

    /**
     * 第一消费送待领币
     * @param memberId
     */
    public boolean TaskFirstConsumption(String memberId){
        boolean bool = false;
        //第一消费送待领币
        if(esEthaccountsService.isFirstConsumption(memberId)) {
            //第一消费送待领币
            SysTask sysTask = this.findFirstConsumption();
            if (sysTask.getStatus() == 1) {
                bool = esDlbaccountsService.addInSuccess(memberId, EsDlbaccountsServiceImpl.DIC_TYPE_FIRSTCONSUMPTION, sysTask.getDlb(), "-1");
            } else {
                bool = true;
            }
        }
        return bool;
    }

    /**
     * 第一消费直属上级送待领币
     * @param memberId
     * @return
     */
    public boolean TaskLowerFirstConsumption(String memberId) {
        boolean bool = false;
        //第一消费直属上级送待领币
        if(esEthaccountsService.isFirstConsumption(memberId)) {
            SysTask sysTask = this.findLowerFirstConsumption();
            if (sysTask.getStatus() == 1) {
                Member m = memberService.selectById(memberId);
                bool = esDlbaccountsService.addInSuccess(m.getGeneralizeMemberId(), EsDlbaccountsServiceImpl.DIC_TYPE_LOWERFIRSTCONSUMPTION, sysTask.getDlb(), "-1");
            } else {
                bool = true;
            }
        }
        return bool;
    }
}
