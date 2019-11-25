package com.didu.lotteryshop.lotterya.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ETH 转账异步响应定时任务
 * @author CHJ
 * @date 20119-11-11
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ETHTransferTask {

    @Scheduled(cron = "* 0/30 * * * ?")
    private void configureTasks() {
        //TODO 执行购买彩票转账定时任务
        //TODO 执行开奖转账定时任务

    }
}
