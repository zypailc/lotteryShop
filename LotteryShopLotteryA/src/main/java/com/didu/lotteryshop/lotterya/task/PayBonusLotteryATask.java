package com.didu.lotteryshop.lotterya.task;

import com.didu.lotteryshop.lotterya.service.TaskLotteryAPayBonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A彩票发放奖金定时任务
 * @author CHJ
 * @date 2019-11-28
 */
@Component
@Configuration      // 主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
public class PayBonusLotteryATask {
    private static final Logger logger = LoggerFactory.getLogger(PayBonusLotteryATask.class);
    @Autowired
    private TaskLotteryAPayBonusService lotteryAPayBonusService;
    /** 每3分钟执行 */
    @Scheduled(cron = "0 0/3 * * * ?")//默认是fixedDelay 上一次执行完毕时间后执行下一轮
    private void configureTasks() {
        //开始执行发放奖金定时任务！
        logger.info("==============================☆☆ Start execution PayBonusLotteryA timing task! ☆☆==============================================");
        //发放奖金
        lotteryAPayBonusService.LotteryAPayBonus();
        //执行发放奖金定时任务结束！
        logger.info("==============================☆☆ End execution PayBonusLotteryA timing task!   ☆☆==============================================");
    }
}
