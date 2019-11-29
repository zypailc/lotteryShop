package com.didu.lotteryshop.lotterya.task;

import com.didu.lotteryshop.lotterya.service.TaskLotteryADrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * A彩票开奖定时任务
 * @author CHJ
 * @date 2019-11-28
 */
@Component
@Configuration      // 主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
public class DrawLotteryATask {
    private static final Logger logger = LoggerFactory.getLogger(DrawLotteryATask.class);
    @Autowired
    private TaskLotteryADrawService taskLotteryADrawService;
    //@Scheduled(fixedRate=5000) // fixedRate:上一次开始执行时间点之后5秒再执行
    //@Scheduled(fixedDelay = 5000) //fixedDelay:上一次执行完毕时间点之后5秒再执行
    /** 每5分钟执行 */
    @Scheduled(cron = "0 0/5 * * * ?") //默认是fixedDelay 上一次执行完毕时间后执行下一轮
    private void configureTasks() {
        logger.info("==============================☆☆ Start execution DrawLotteryA timing task! ☆☆==============================================");
        taskLotteryADrawService.lotteryADraw();
        logger.info("==============================☆☆ End execution DrawLotteryA timing task!   ☆☆==============================================");
    }
}
