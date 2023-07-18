package com.lwdevelop.bot.bots.coolbao.messageHandling.commands.utils;

import com.lwdevelop.utils.HttpRequestUtil;

public class XxPayApiUtil extends HttpRequestUtil {

    protected final static String XXPAY_LOGIN_URL = "http://4pay.ddb22.vip/xxpay-manage/api/auth";
    protected final static String XXPAY_MCH_INFO_GET_URL = "http://4pay.ddb22.vip/xxpay-manage/api/mch_info/get";
    protected final static String XXPAY_MCH_INFO_ADD_URL = "http://4pay.ddb22.vip/xxpay-manage/api/mch_info/add";
    protected final static String XXPAY_ACCOUNT_URL = "http://4pay.ddb22.vip/xxpay-manage/api/account/get";

    protected static String generateToken() {
        String params = "username=ddbpay99_bot&password=tt186186";
        String response = sendGetRequest(XXPAY_LOGIN_URL, params, "access_token");
        return response;
    }

}
