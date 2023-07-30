package org.springybot.botModel.enum_;

public enum TalentEnum {

    // seeker
    JOBSEEKER_DEFAULT_FORM(
            "求职人员\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：（限50字以内）\n自我介绍：（限50字以内）\n✈️咨询飞机号："),
    JOBSEEKE_REDITOR_DEFAULT_FORM(
            "编辑求职\n\n姓名：\n男女：\n人数：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作地址：\n精通语言：\n工作经历：\n自我介绍：\n✈️咨询飞机号："),

    // posting
    JOBPOSTING_DEFAULT_FORM(
            "招聘人才\n\n公司：\n职位：\n底薪：\n提成：\n国籍：\n男女：\n人数：\n语言要求：\n是否中介：\n上班时间：\n要求内容：（限50字以内）\n🐌地址：\n✈️咨询飞机号："),
    JOBPOSTING_EDITOR_DEFAULT_FORM(
            "编辑招聘\n\n公司：\n职位：\n底薪：\n提成：\n国籍：\n男女：\n人数：\n语言要求：\n是否中介：\n上班时间：\n要求内容：\n🐌地址：\n✈️咨询飞机号："),

    // other

    REMIND_EDITOR_("提醒：复制模板到输入框编辑发送，一个账号只能发布一次。可以删除重新发布显示最新时间，或是使用新的账号发布"),
    REMIND_EDITOR("提醒：请复制上列信息到输入框并进行编辑，编辑完毕发送"),
    FIFTY_CHARACTERS_LIMIT("（限50字以内）"),
    JOB_MANAGEMENT("招聘和求职信息管理"),
    SUCCESSFULLYDELETED("删除成功"),
    FILTERKEY("过滤键值 "),
    PLEASE_SET_TELEGRAM_USERNAME("请设置 Telegram 用户名称"),
    BOT_NOT_ENOUGH_RIGHTS(" 群组的权限设定不足以让机器人有效地管理该群组。\n为了让机器人能够正常运作，请将其设定为该群组的管理员，以便它能够更好地管理该群组。");

    private String text;

    private TalentEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String send_recruitment_text(String result) {
        return "招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 无限发布";
    }

    // JOBSEARCH
    public static String send_jobsearch_text(String result) {
        return "求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 无限发布";
    }

    public static String help_text(String name, String botName) {
        return "👋🏻 嗨 " + name + "！\n" +
                "欢迎使用我们的机器人！\n" +
                botName + " 可以帮助您快速找到合适的工作或人才。\n\n" +
                "我们希望这个机器人能为您提供帮助！";
    }

    public static String subscribeChannel_text() {
        return "✅ 官方频道 ➡️ @rc499 ️ \uD83D\uDD08关注后可发布";
    }

}
