package com.lwdevelop.bot.talentBot.utils;

public enum SpringyBotEnum {

    // chatType
    CHAT_TYPE_PRIVATE("private"),
    CHAT_TYPE_GROUP("supergroup"),
    CHAT_TYPE_CHANNEL("channel"),

    // command manage
    HOW_TO_ADD_ME_TO_YOUR_GROUP("如何将我添加到您的群组"),
    ADMIN_PANEL("管理面板"),
    HOW_TO_ADD_ME_TO_YOUR_CHANNEL("如何将我添加到您的频道"),
    SUPPORT_TEAM_LIST("支援团队列表"),
    ADMINISTRATOR_SETTING("管理员设置"),

    // command job
    RECRUITMENT("招聘人才"),
    JOBSEARCH("求职人员"),
    EDIT_RECRUITMENT("编辑招聘"),
    EDIT_JOBSEARCH("编辑求职"),
    POST_RECRUITMENT("发布招聘"),
    POST_JOBSEARCH("发布求职"),
    UNPUBLISHED_RECRUITMENT("未发布招聘"),
    UNPUBLISHED_JOBSEARCH("未发布求职"),
    JOBPOSTING("jobPosting"),
    JOBSEEKER("jobSeeker"),
    EDIT_JOBPOSTING("editJobPosting_"),
    EDIT_JOBSEEKER("editJobSeeker_"),
    CLEAR_JOBPOSTING("clearJobPosting_"),
    REMIND_EDITOR("提醒：请复制上列信息到输入框并进行编辑，编辑完毕发送"),
    REMIND_EDITOR_("提醒：复制模板到输入框编辑发送，一个账号只能发布一次。可以删除重新发布显示最新时间，或是使用新的账号发布"),
    ALREADY_POST_POSTING("您已经发布过[招聘人才]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。"),
    ALREADY_POST_SEEKER("您已经发布过[求职人员]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。"),
    JOBSEEKER_DEFAULT_FORM("求职人员\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：（限50字以内）\n自我介绍：（限50字以内）\n✈️咨询飞机号：\n\n 关注 @rc499 点击 @rc899Bot 发布"),
    JOBPOSTING_DEFAULT_FORM("招聘人才\n\n公司：\n职位：\n底薪：\n提成：\n国籍：\n男女：\n人数：\n语言要求：\n是否中介：\n上班时间：\n要求内容：（限50字以内）\n🐌地址：\n✈️咨询飞机号：\n\n关注 @rc499 点击 @rc899Bot 发布"),
    JOBPOSTING_EDITOR_DEFAULT_FORM("编辑招聘\n\n公司：\n职位：\n底薪：\n提成：\n国籍：\n男女：\n人数：\n语言要求：\n是否中介：\n上班时间：\n要求内容：\n🐌地址：\n✈️咨询飞机号：\n\n关注 @rc499 点击 @rc899Bot 发布"),
    JOBSEEKE_REDITOR_DEFAULT_FORM("编辑求职\n\n姓名：\n男女：\n人数：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作地址：\n精通语言：\n工作经历：\n自我介绍：\n✈️咨询飞机号：\n\n 关注 @rc499 点击 @rc899Bot 发布"),
    FIFTY_CHARACTERS_LIMIT("（限50字以内）"),
    REMIND_WORKEXPERIENCE_LIMIT("发送失败,工作经历超过50字"),
    REMIND_SELFINTRODUCTION_LIMIT("发送失败,自我介绍超过50字"),
    REMIND_REQUIREMENTS_LIMIT("发送失败,要求内容超过50字"),
    FILTERKEY("过滤键值 "),
        
    SUCCESSFULLYDELETED("删除成功"),


    JOB_MANAGEMENT("招聘和求职信息管理"),
    POST_JOB("发布"),
    PLEASE_SET_TELEGRAM_USERNAME("请设置Telegram 用户名称"),
    COMMEND_MANAGE("What con this bot do?\nPlease tap on START"),
    BOT_NOT_ENOUGH_RIGHTS(" 群组的权限设定不足以让机器人有效地管理该群组。\n为了让机器人能够正常运作，请将其设定为该群组的管理员，以便它能够更好地管理该群组。");

    private String text;

    private SpringyBotEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
