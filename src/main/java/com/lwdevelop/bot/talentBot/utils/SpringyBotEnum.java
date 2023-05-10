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
    EDIT_RECRUITMENT("发布招聘"),
    EDIT_JOBSEARCH("发布求职"),
    JOBPOSTING("jobPosting"),
    JOBSEEKER("jobSeeker"),
    REMINDEDITOR("提醒：请复制上列信息到输入框并进行编辑，编辑完毕发送"),
    EDITJOBPOSTING("editJobPosting_"),
    EDITJOBSEEKER("editJobSeeker_"),
    SUCCESSFULLYDELETED("删除成功"),
    JOBSEEKERDEFAULTFORM("求职人员\n\n" +
                            "姓名：\n" +
                            "男女：\n" +
                            "出生_年_月_日：\n" +
                            "年龄：\n" +
                            "国籍：\n" +
                            "学历：\n" +
                            "技能：\n" +
                            "目标职位：\n" +
                            "手上有什么资源：\n" +
                            "期望薪资：\n" +
                            "工作经历：（限50字以内）\n" +
                            "自我介绍：（限50字以内）\n" +
                            "✈️咨询飞机号："),
                            
                            
    JOB_MANAGEMENT("招聘和求职信息管理"),
    POST_JOB("发布"),

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
