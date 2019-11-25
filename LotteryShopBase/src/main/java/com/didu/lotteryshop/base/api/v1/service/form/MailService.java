package com.didu.lotteryshop.base.api.v1.service.form;

import com.didu.lotteryshop.common.entity.Member;

public interface MailService {

    public void sendSimpleMail(Member member, String subject, String text);
}
