package com.didu.lotteryshop.lotteryb.task;

import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybDiServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration      // 主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
public class LotterybDiTask {

    private static final Logger logger = LoggerFactory.getLogger(LotterybDiTask.class);

    @Autowired
    private LotterybDiServiceImpl lotterybDiService;

    @Scheduled(cron = "0 10 0 * * ?")//默认是fixedDelay 上一次执行完毕时间后执行下一轮
    public void diTask(){
        //开始执行转账定时任务！
        logger.info("==============================☆☆ Start execution dividedSettleAccounts timing task! ☆☆==============================================");
        lotterybDiService.dividedSettleAccounts();
        logger.info("==============================☆☆ Start execution dividedSettleAccounts timing task! ☆☆==============================================");
    }

}
