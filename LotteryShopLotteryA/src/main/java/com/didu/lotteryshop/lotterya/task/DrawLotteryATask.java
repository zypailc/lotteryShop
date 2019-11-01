package com.didu.lotteryshop.lotterya.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 彩票类型A定时任务
 * @author CHJ
 * @date 2019-11-01
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DrawLotteryATask {
    //3.添加定时任务
    //@Scheduled(cron = "* 0/30 * * * ?")
    @Scheduled(cron = "* 0/30 * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        //计划开奖，在15分钟内完成
        //step 1  关闭购买功能
        //step 2  10分钟后，开始转账到智能合约账户
        //step 3  5分钟后，开奖计算
        //step 4  转账操作
        System.out.println("关闭购买功能: " + LocalDateTime.now());
        System.out.println("10分钟后，开始转账到智能合约账户: " + LocalDateTime.now());
        System.out.println("5分钟后，开奖计算: " + LocalDateTime.now());
        System.out.println("中奖转账操作: " + LocalDateTime.now());
        System.out.println("重新开启下一期购买功能: " + LocalDateTime.now());

    }
}
