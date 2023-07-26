package com.lwdevelop.bot.bots.coolbao.messageHandling.commands.xxpayapi;

import java.math.BigDecimal;
import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.utils.XxPayApiUtil;
import com.lwdevelop.bot.bots.utils.Common;

public class cgBalance extends XxPayApiUtil {

    public String cmd(Common common) {
        String token = generateToken();
        String cg01_params = "mchId=20000007&access_token=" + token;
        String cg02_params = "mchId=20000008&access_token=" + token;
        String cg03_params = "mchId=20000009&access_token=" + token;
        String cg04_params = "mchId=20000010&access_token=" + token;

        String cg01 = sendGetRequest(XXPAY_ACCOUNT_URL, cg01_params, "balance");
        String cg02 = sendGetRequest(XXPAY_ACCOUNT_URL, cg02_params, "balance");
        String cg03 = sendGetRequest(XXPAY_ACCOUNT_URL, cg03_params, "balance");
        String cg04 = sendGetRequest(XXPAY_ACCOUNT_URL, cg04_params, "balance");

        BigDecimal cg01_result = divideBy100(cg01);
        BigDecimal cg02_result = divideBy100(cg02);
        BigDecimal cg03_result = divideBy100(cg03);
        BigDecimal cg04_result = divideBy100(cg04);

        String cg01_ = cg01_result.toString();
        String cg02_ = cg02_result.toString();
        String cg03_ = cg03_result.toString();
        String cg04_ = cg04_result.toString();

        BigDecimal totalAmount = cg01_result.add(cg02_result).add(cg03_result).add(cg04_result);

        String str = "cg01    " + cg01_ + "\n" +
                "cg02    " + cg02_ + "\n" +
                "cg03    " + cg03_ + "\n" +
                "cg04    " + cg04_ + "\n\n" +
                "总余额    " + totalAmount.toString();
        return str;
    }

    private BigDecimal divideBy100(String value) {
        BigDecimal result = new BigDecimal(value).divide(BigDecimal.valueOf(100));
        return result;
    }

}
