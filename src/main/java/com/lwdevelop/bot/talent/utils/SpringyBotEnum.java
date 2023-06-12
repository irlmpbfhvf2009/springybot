package com.lwdevelop.bot.talent.utils;

public enum SpringyBotEnum {

    // chatType
    CHAT_TYPE_PRIVATE("private"),
    CHAT_TYPE_GROUP("supergroup"),
    CHAT_TYPE_CHANNEL("channel"),

    // seeker
    JOBSEARCH("求职人员"),
    EDIT_JOBSEARCH("编辑求职"),
    POST_JOBSEARCH("发布求职"),
    UNPUBLISHED_JOBSEARCH("未发布求职"),
    JOBSEEKER("jobSeeker"),
    EDIT_JOBSEEKER("editJobSeeker_"),
    CLEAR_JOBSEEKER("clearJobSeeker_"),
    ALREADY_POST_SEEKER("您已经发布过[求职人员]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。"),
    JOBSEEKER_DEFAULT_FORM("求职人员\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：（限50字以内）\n自我介绍：（限50字以内）\n✈️咨询飞机号："),
    JOBSEEKE_REDITOR_DEFAULT_FORM("编辑求职\n\n姓名：\n男女：\n人数：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作地址：\n精通语言：\n工作经历：\n自我介绍：\n✈️咨询飞机号："),
    REMIND_WORKEXPERIENCE_LIMIT("发送失败,工作经历超过50字"),
    REMIND_SELFINTRODUCTION_LIMIT("发送失败,自我介绍超过50字"),

    // posting
    RECRUITMENT("招聘人才"),
    EDIT_RECRUITMENT("编辑招聘"),
    POST_RECRUITMENT("发布招聘"),
    UNPUBLISHED_RECRUITMENT("未发布招聘"),
    JOBPOSTING("jobPosting"),
    EDIT_JOBPOSTING("editJobPosting_"),
    CLEAR_JOBPOSTING("clearJobPosting_"),
    ALREADY_POST_POSTING("您已经发布过[招聘人才]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。"),
    JOBPOSTING_DEFAULT_FORM("招聘人才\n\n公司：\n职位：\n底薪：\n提成：\n国籍：\n男女：\n人数：\n语言要求：\n是否中介：\n上班时间：\n要求内容：（限50字以内）\n🐌地址：\n✈️咨询飞机号："),
    JOBPOSTING_EDITOR_DEFAULT_FORM("编辑招聘\n\n公司：\n职位：\n底薪：\n提成：\n国籍：\n男女：\n人数：\n语言要求：\n是否中介：\n上班时间：\n要求内容：\n🐌地址：\n✈️咨询飞机号："),
    REMIND_REQUIREMENTS_LIMIT("发送失败,要求内容超过50字"),

    // other
    REMIND_EDITOR_("提醒：复制模板到输入框编辑发送，一个账号只能发布一次。可以删除重新发布显示最新时间，或是使用新的账号发布"),
    REMIND_EDITOR("提醒：请复制上列信息到输入框并进行编辑，编辑完毕发送"),
    FIFTY_CHARACTERS_LIMIT("（限50字以内）"),
    JOB_MANAGEMENT("招聘和求职信息管理"),
    COMMEND_MANAGE("What con this bot do?\nPlease tap on START"),
    HOW_TO_ADD_ME_TO_YOUR_GROUP("如何将我添加到您的群组"),
    ADMIN_PANEL("管理面板"),
    HOW_TO_ADD_ME_TO_YOUR_CHANNEL("如何将我添加到您的频道"),
    SUPPORT_TEAM_LIST("支援团队列表"),
    ADMINISTRATOR_SETTING("管理员设置"),
    SUCCESSFULLYDELETED("删除成功"),
    FILTERKEY("过滤键值 "),
    PLEASE_SET_TELEGRAM_USERNAME("请设置Telegram 用户名称"),
    BOT_NOT_ENOUGH_RIGHTS(" 群组的权限设定不足以让机器人有效地管理该群组。\n为了让机器人能够正常运作，请将其设定为该群组的管理员，以便它能够更好地管理该群组。");

    private String text;

    private SpringyBotEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String send_recruitment_text(String result) {
        return "招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布";
    }
    //JOBSEARCH
    public static String send_jobsearch_text(String result) {
        return "求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布";
    }

    public static String help_text(String name,String botName){
        return "👋🏻 嗨 " + name + "！\n" +
        "欢迎使用我们的机器人！\n" +
        botName + " 可以帮助您快速找到合适的工作或人才。\n\n" +
        "我们希望这个机器人能为您提供帮助！";
        // "我们希望这个机器人能为您提供帮助，如果您有任何问题或建议，请随时联系我们。谢谢！";
    }

    public static String subscribeChannel_text(){
        return "✅ 官方频道\n➡️ @rc499 ️\n\uD83D\uDD08关注后可发布";
    }

}
