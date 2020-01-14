package com.didu.lotteryshop.base.kafka;

import com.didu.lotteryshop.base.api.v1.service.WalletService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafKaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafKaConsumer.class);
    @Autowired
    private WalletService walletService;
    /**
     * ETH提现到外部钱包
     * @param record
     */
    @KafkaListener(topics = {"kafkaWithdrawCashEth"})
    public void receive(ConsumerRecord<?, ?> record) {
        logger.info("Start execution kafkaWithdrawCashEth: [" + record.key()+"="+record.value().toString()+"]");
        walletService.kafkaWithdrawCashEth(record.value().toString());
        logger.info("End execution kafkaWithdrawCashEth: [" + record.key()+"="+record.value().toString()+"]");
    }
    /**
     * 平台币兑换ETH
     * @param record
     */
    @KafkaListener(topics = {"withdrawCashLsbToEth"})
    public void receive1(ConsumerRecord<?, ?> record) {
        logger.info("Start execution withdrawCashLsbToEth: [" + record.key()+"="+record.value().toString()+"]");
        walletService.kafkaWithdrawCashLsbToEth(record.value().toString());
        logger.info("End execution withdrawCashLsbToEth: [" + record.key()+"="+record.value().toString()+"]");
    }

    /**
     * ETH充值平台币
     * @param record
     */
    @KafkaListener(topics = {"withdrawCashEthToLsb"})
    public void receive2(ConsumerRecord<?, ?> record) {
        logger.info("Start execution withdrawCashEthToLsb: [" + record.key()+"="+record.value().toString()+"]");
        walletService.kafkaWithdrawCashEthToLsb(record.value().toString());
        logger.info("End execution withdrawCashEthToLsb: [" + record.key()+"="+record.value().toString()+"]");
    }


}
