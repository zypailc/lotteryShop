package com.didu.lotteryshop.lotteryb.task;

import com.didu.lotteryshop.common.utils.DateUtil;
import com.didu.lotteryshop.lotteryb.entity.LotterybIssue;
import com.didu.lotteryshop.lotteryb.service.LotterybIssueService;
import com.didu.lotteryshop.lotteryb.service.LotterybStartService;
import com.didu.lotteryshop.lotteryb.service.LotterybStatisticsService;
import com.didu.lotteryshop.lotteryb.service.LotterybWinningService;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybDiServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybIssueServiceImpl;
import com.didu.lotteryshop.lotteryb.service.form.impl.LotterybPmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class LotterybCommandLineRunner implements CommandLineRunner {

    @Autowired
    private LotterybIssueService lotterybIssueService;
    @Autowired
    private LotterybStartService lotterybStartService;
    @Autowired
    private LotterybStatisticsService lotterybStatisticsService;
    @Autowired
    private LotterybPmServiceImpl lotterybPmService;
    @Autowired
    private LotterybWinningService lotterybWinningService;
    @Autowired
    private LotterybDiServiceImpl lotterybDiService;
    @Autowired
    private LotterybIssueServiceImpl lotterybIssueServiceIml;

    /**
     * 玩法Id
     */

    @Override
    public void run(String... args) throws Exception {
        //Timer timer = new Timer();
        //timer.schedule(timerTask(1),1000);
        //timer.schedule(timerTask(2),1000);
        //timer.schedule(timerTask(3),1000);

    }

    private void TimerTaskRun(LotterybIssue lotterybIssue,Integer lotterybInfoId){
        Timer timer = new Timer();
        TimerTask task = timerTask(lotterybInfoId);
        timer.schedule(task, DateUtil.getTimestamp(new Date(),lotterybIssue.getEndTime()));
    }

    private TimerTask timerTask(Integer lotterybInfoId){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //生成下一期数据
                LotterybIssue lotterybIssue = lotterybIssueService.createNextLotterybIssue(lotterybInfoId);
                TimerTaskRun(lotterybIssue,lotterybInfoId);
                //生成统计数据
                boolean b = lotterybStatisticsService.createAllCombinationData(lotterybIssue.getId(),lotterybInfoId);
                LotterybIssue lotterybIssueUp = lotterybIssueServiceIml.findUpLotteryaIssue(lotterybInfoId);
                //开奖
                lotterybStartService.lotteryBDraw(lotterybInfoId,lotterybIssueUp);
                //提成发放
                lotterybPmService.updateStatus(lotterybInfoId,lotterybIssueUp);
                //中奖发放
                lotterybWinningService.winning(lotterybInfoId,lotterybIssueUp);
                //推广商数据存储
                lotterybDiService.generalizeDividedInto(lotterybInfoId,lotterybIssueUp);
            }
        };
        return timerTask;
    }

}
