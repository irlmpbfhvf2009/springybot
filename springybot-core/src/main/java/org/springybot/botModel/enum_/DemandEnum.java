package org.springybot.botModel.enum_;

public enum DemandEnum {

    ALREADY_POST_DEMAND("您已经发布过[需求]信息，请点选[供需信息管理]进行编辑或删除信息后重新发布。"),
    DEMAND_DEFAULT_FORM(
            "发布需求\n\n项目名称：\n佣金奖赏：\n是否中介：\n上班时间：\n是否支持担保：\n项目内容：（限50字以内）\n✈️咨询飞机号："),
    DEMAND_EDITOR_DEFAULT_FORM(
            "编辑需求\n\n项目名称：\n佣金奖赏：\n是否中介：\n上班时间：\n是否支持担保：\n项目内容：（限50字以内）\n✈️咨询飞机号："),
    REMIND_PROJECTDETAIL_LIMIT("发送失败,项目内容超过50字"),

    SUPPLY_DEFAULT_FORM(
            "发布供应\n\n项目名称：\n佣金奖赏：\n是否中介：\n上班时间：\n是否支持担保：\n项目内容：（限50字以内）\n✈️咨询飞机号："),
    SUPPLY_EDITOR_DEFAULT_FORM(
            "编辑供应\n\n项目名称：\n佣金奖赏：\n是否中介：\n上班时间：\n是否支持担保：\n项目内容：（限50字以内）\n✈️咨询飞机号："),

    // other
    SUPPLY_AND_DEMAND_MANAGEMENT("供需信息管理");
    private String text;

    private DemandEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String send_supply_text(String result) {
        return "供应\n\n" + result + "\n\n⭐️走云集担保群 @zsw0012  @yunji999， 咨询 @cx699 其他勿扰⭐️";
    }

    // JOBSEARCH
    public static String send_demand_text(String result) {
        return "需求\n\n" + result + "\n\n ⭐️走云集担保群 @zsw0012  @yunji999， 咨询 @cx699 其他勿扰⭐️";
    }

    public static String help_text(String name, String botName) {
        return "👋🏻 嗨 " + name + "！\n" +
                "欢迎使用我们的机器人！\n" +
                botName + " 可以帮助您快速發布您的的供應或需求。\n\n" +
                "我们希望这个机器人能为您提供帮助！";
        // "我们希望这个机器人能为您提供帮助，如果您有任何问题或建议，请随时联系我们。谢谢！";
    }

}
