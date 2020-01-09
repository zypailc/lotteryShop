package com.didu.lotteryshop.lotterya.kafka;

import com.didu.lotteryshop.lotterya.api.v1.service.LotteryAService;
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
    private LotteryAService lotteryAService;

    @KafkaListener(topics = {"kafkaBuyLottery"})
    public void receive(ConsumerRecord<?, ?> record) {
        logger.info("Start execution kafkaBuyLottery: [" + record.key()+"="+record.value().toString()+"]");
        lotteryAService.kafkaBuyLottery(Integer.valueOf(record.value().toString()));
        logger.info("End execution kafkaBuyLottery: [" + record.key()+"="+record.value().toString()+"]");
    }
}
