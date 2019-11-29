package com.didu.lotteryshop.lotterya.task;

import com.didu.lotteryshop.lotterya.service.TaskLotteryABuyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ETH 转账异步响应定时任务
 * @author CHJ
 * @date 20119-11-28
 */
@Component
@Configuration      // 主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
public class ETHTransferTask {
    private static final Logger logger = LoggerFactory.getLogger(ETHTransferTask.class);
    @Autowired
    private TaskLotteryABuyService taskLotteryABuyService;
    /** 每2分钟执行 */
    @Scheduled(cron = "0 0/2 * * * ?")//默认是fixedDelay 上一次执行完毕时间后执行下一轮
    private void configureTasks() {
        //开始执行转账定时任务！
        logger.info("==============================☆☆ Start execution BuyLotteryA timing task! ☆☆==============================================");
        //处理购买彩票待确认的数据
        taskLotteryABuyService.disposeLotteryABuyWait();
        //执行转账定时任务结束！
        logger.info("==============================☆☆ End execution BuyLotteryA timing task!   ☆☆==============================================");
    }
}
