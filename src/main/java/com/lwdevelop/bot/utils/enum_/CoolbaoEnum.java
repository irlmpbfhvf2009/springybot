package com.lwdevelop.bot.utils.enum_;

public enum CoolbaoEnum {

    PASSWORD("duv3qzXY"),
    COMMANDS_START("✅官方频道：@ddb37\n✅人工客服：@ddbpay99\n\n注意防范骗子，我们的客服 @ddbpay99 和频道 @ddb37，只要记住用户名，永不失联！"),
    COMMANDS_HELP(
            "/start - 開始\n/id_card\n/punch_in\n/cg_balance - 查詢cg01-cg04餘額\n/xxpay_serice - 四方服務\n/serven_days_service - 七天服務\n/info - 顯示收錄後台\n/help - 幫助！"),
    COMMANDS_INFO("/xxpay - 四方\n/sevendays - 七天\n/bbippo - 錢程\n/dahe - 大河\n/white_dove - 白鴿"),

    COMMANDS_XXPAY("● 正式 營運平台：http://4pay.ddb22.vip/xxpay-manage/x_mgr/start/index.html\n" +
            "● 正式 商戶平台：http://4pay.ddb22.vip/xxpay-merchant/x_mch/start/index.html\n" +
            "● 正式 代理商平台：http://4pay.ddb22.vip/xxpay-agent/x_agent/start/index.html\n\n" +
            "正式機(寶塔)：http://115.126.8.2:8893/252dc6c6/\n" +
            "username: bmevlegx\n" +
            "綁定域名：http://4pay.ddb22.vip/ ...>> www/wwwroot/192.168.11.105\n" +
            "代付接口地址:\n" +
            "下單：https://etpay888.com/api/payments/pay_order\n" +
            "查詢訂單：https://etpay888.com/api/payments/query_transaction\n" +
            "回調通知ip:   35.220.238.192\n" +
            "=====================\n" +
            "代收接口地址:\n" +
            "下單：https://etpay888.com/api/pay_order\n" +
            "查詢訂單：https://etpay888.com/api/query_transaction\n" +
            "回調通知ip:  35.220.238.192\n" +
            "======================\n" +
            "玩家直充订单\n" +
            "查询或添加玩家钱包地址：\n" +
            "https://etpay888.com/api/wallet/address\n" +
            "查询直充订单：https://etpay888.com/api/wallet/query"),

    COMMANDS_SEVENDAYS("商户后台: https://qimchlaf6a1mnqu4.szzzv.xyz\n\n" +
            "登录账号:  qc8899\n" +
            "登录密码: \n" +
            "商户号： M1679749474\n" +
            "商户密钥： 7f9431619ca743a29295a86d10275a8f\n\n" +
            "兼容性网关：\n" +
            "下单网关: https://qipay7o1ped6el4b.szzzv.xyz/api/pay/unifiedorder/v2\n" +
            "订单查询: https://qipay7o1ped6el4b.szzzv.xyz/api/pay/query/v2\n" +
            "IP 白名单: 206.119.66.5,206.119.66.6,206.119.66.7\n" +
            "API 文档: https://qimchlaf6a1mnqu4.szzzv.xyz/api/anon/apidoc"),

    COMMANDS_BBIPPO("商户后台地址：https://q9vzgmsk.sepcoremercury.com/4sw3aCO7/uWEaOulY.php\n" +
            "商户号：28953\n" +
            "商户账号：bbippo\n" +
            "商户密码：\n" +
            "资金密码：\n" +
            "绑定手机：12364\n" +
            "接口类型：卡转卡 宝转卡 宝转宝 USDT-erc20 USDT-trc20"),

    COMMANDS_DAHE("大河支付系統\n" +
            "商户后台登录地址：http://dahe.dahe2022.com\n" +
            "商户账号： dahepp\n" +
            "登录密码： aa123456\n" +
            "支付密码： aa123456"),
    COMMANDS_WHITEDOVE("白鴿系統\n" +
            "商户后台登录地址：http://ws.333035.com/pages/console/login.html \n" +
            "账号： 1000\n" +
            "密码： 1000");

    private String text;

    private CoolbaoEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
