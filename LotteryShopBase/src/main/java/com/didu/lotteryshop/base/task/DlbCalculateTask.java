package com.didu.lotteryshop.base.task;

import com.didu.lotteryshop.base.service.TaskDlbCalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 待领币核算定时任务
 * @author CHJ
 * @date 2019-12-04
 */
@Component
@Configuration      // 主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
public class DlbCalculateTask {
    private static final Logger logger = LoggerFactory.getLogger(DlbCalculateTask.class);
    @Autowired
    private TaskDlbCalculateService taskDlbCalculateService;
    /** 每天0点10分钟执行一次 */
    //@Scheduled(cron = "0 10 0 * * ?")//默认是fixedDelay 上一次执行完毕时间后执行下一轮
    /** 每3分钟执行 */
    @Scheduled(cron = "0 0/3 * * * ?")
    private void configureTasks() {
        //开始执行待领币核算定时任务
        logger.info("==============================☆☆ Start execution DlbCalculateTask timing task! ☆☆==============================================");
        taskDlbCalculateService.dlbCalculate();
        //结束执行待领币核算定时任务
        logger.info("==============================☆☆ End execution DlbCalculateTask timing task!   ☆☆==============================================");
    }
}
