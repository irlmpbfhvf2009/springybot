package org.springybot.botModel.enum_;

import org.telegram.telegrambots.meta.api.objects.User;

public enum CoolbaoEnum {

        PASSWORD("duv3qzXY"),
        COMMANDS_START("✅官方频道：@ddb37\n✅人工客服：@ddbpay99\n\n注意防范骗子，我们的客服 @ddbpay99 和频道 @ddb37，只要记住用户名，永不失联！"),
        HELP("/start\n/id_card\n/punch_in\n/help - 幫助\n\n四方服務\n/cg_balance - 查詢商戶餘額");

        private String text;

        private CoolbaoEnum(String text) {
                this.text = text;
        }

        public String getText() {
                return this.text;
        }

        public static String idCard(User user) {
                String usernameWithLink = "<a href='https://t.me/" + user.getUserName() + "'>" + user.getUserName() + "</a>";

                String info = "👤 You\n" +
                                "├ id: " + user.getId() + "\n" +
                                "├ is_bot: " + user.getIsBot() + "\n" +
                                "├ first_name: " + user.getFirstName() + "\n" +
                                "├ username: " + usernameWithLink + "\n" +
                                "└ language_code: " + user.getLanguageCode() + "\n\n";
                return info;
        }
        public static String commandsHelp(String botUsername, User user) {
                return "歡迎使用 @" + botUsername + "\n\n" + idCard(user) + HELP.getText();
        }
}
