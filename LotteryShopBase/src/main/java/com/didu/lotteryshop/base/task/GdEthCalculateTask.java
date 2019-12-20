package com.didu.lotteryshop.base.task;

import com.didu.lotteryshop.base.service.TaskGdEthCalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 推广分成结账定时任务
 * @author CHJ
 * @date 2019-12-19
 */
@Component
@Configuration      // 主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
public class GdEthCalculateTask {
    private static final Logger logger = LoggerFactory.getLogger(GdEthCalculateTask.class);
    @Autowired
    private TaskGdEthCalculateService taskGdEthCalculateService;
    /** 每天0点30分钟执行一次 */
    @Scheduled(cron = "0 30 0 * * ?")//默认是fixedDelay 上一次执行完毕时间后执行下一轮
    private void configureTasks() {
        //开始执行推广分成结账定时任务
        logger.info("==============================☆☆ Start execution GdEthCalculateTask timing task! ☆☆==============================================");
        taskGdEthCalculateService.gdEthCalculate();
        //结束执行推广分成结账定时任务
        logger.info("==============================☆☆ End execution GdEthCalculateTask timing task!   ☆☆==============================================");
    }
}
