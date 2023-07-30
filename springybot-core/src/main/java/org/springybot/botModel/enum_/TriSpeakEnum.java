package org.springybot.botModel.enum_;

public enum TriSpeakEnum {
    // 雲集
    YUNJI_ID(5901295307L),
    YUNJI_DK1("云集担保流程"),
    YUNJI_DK2("供需资源免费发布，将开放机器人"),
    YUNJI_URL1("https://t.me/yj5999"),
    YUNJI_URL2("https://t.me/yunji88"),

    // ddb37
    DDB37_ID(5822751184L),
    DDB37_DK1("四方支付开户"),
    DDB37_DK2("im聊天系统搭建"),
    DDB37_URL1("https://t.me/ddb37/437"),
    DDB37_URL2("https://t.me/ddb37/437");

    private String text;
    private Long id;

    private TriSpeakEnum(String text) {
        this.text = text;
    }
    
    private TriSpeakEnum(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Long getId() {
        return this.id;
    }

    public static String warning_text(String username,String channel_title){
        return username + " : 关注频道 " + channel_title + " 后才能发言";
    }

    public static String warning_text(String channel_title){
        return "\n————————————\n以上用户需要关注频道👇 "+channel_title+" 后才能发言\n————————————";
    }

    public static String warning_text2(int inviteFriendsQuantity){
        return "\n————————————\n以上用户需要邀请 "+inviteFriendsQuantity+" 人后才能发言\n————————————";
    }
}
