package com.didu.lotteryshop.common.utils;

import org.apache.commons.lang.StringUtils;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;

public class VerifyETHAddressUtil {
    public static boolean isETHValidAddress(String input) {
        if (StringUtils.isBlank(input) || !input.startsWith("0x"))
            return false;
        return VerifyETHAddressUtil.isValidAddress(input);
    }

    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }
}
