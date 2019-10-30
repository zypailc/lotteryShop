package com.didu.lotteryshop.auth.config.security;

import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 〈自定义无加密密码验证〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
public class NoEncryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        //return AesEncryptUtil.encrypt_code((String)charSequence,AesEncryptUtil.KEY_TOW);
        return (String)charSequence;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {

        return s.equals(AesEncryptUtil.encrypt_code((String)charSequence,AesEncryptUtil.KEY_TOW));
    }

}
