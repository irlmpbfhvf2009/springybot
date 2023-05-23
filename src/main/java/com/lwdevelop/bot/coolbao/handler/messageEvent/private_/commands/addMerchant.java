package com.lwdevelop.bot.coolbao.handler.messageEvent.private_.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdevelop.bot.Common;
import com.lwdevelop.bot.coolbao.utils.SpringyBotEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class addMerchant {

    private final static String xxpay_login_url = "http://4pay.ddb22.vip/xxpay-manage/api/auth";
    private final static String xxpay_mch_info_get_url = "http://4pay.ddb22.vip/xxpay-manage/api/mch_info/get";
    private final static String xxpay_mch_info_add_url = "http://4pay.ddb22.vip/xxpay-manage/api/mch_info/add";

    static String name;
    static String token;

    public void am(Common common) {
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());
        String text = message.getText();

        if (text.equals("/quit")) {

            common.getUserState().put(message.getChatId(), "");
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText("歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
            common.executeAsync(response);

        } else {

            int length = generateRandomLength(8, 16);
            String password = generatePassword(length);
            log.info("Generated Password: {}", password);

            String new_mchId = create_mch_id();
            log.info("Generated new mchId: {}", new_mchId);
            log.info("Generated name: {}", name);

            String str = "商户后台:http://4pay.ddb22.vip/xxpay-merchant/x_mch/start/index.html#/user/login/redirect=%2F\n\n"
                    +
                    "登录账号 : " + text + "\n" +
                    "登录密码 : " + password + "\n" +
                    "支付密码 : " + password + "\n" +
                    "商户ID：" + new_mchId + "\n\n" +
                    "温馨提醒:\n" +
                    "1.登录后请尽早修改密码\n" +
                    "2.建议尽早设置谷歌验证码，保障安全；\n" +
                    "3.请参考清单下方对接文挡完成对接";

            SendMessage response = new SendMessage(chatId, str);
            common.executeAsync(response);
            common.getUserState().put(message.getChatId(), "");

            response = new SendMessage(chatId, "建立商戶 " + name + " 中.......");
            common.executeAsync(response);

            long timestamp = System.currentTimeMillis();
            String timestampString = formatTimestamp(timestamp);

            String params = "name=" + name + "&userName=" + text + "&email=" + text + "@abc.com&status=1&access_token="
                    + token + "&mobile="+timestampString;
            String create_mch_response = sendPostRequest(xxpay_mch_info_add_url, params);

            response = new SendMessage(chatId, "新增成功 response : " + create_mch_response);
            common.executeAsync(response);
        }

    }

    private static String formatTimestamp(long timestamp) {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000000");
        return decimalFormat.format(timestamp);
    }
    
    private static String generateToken() {
        String params = "username=leo&password=as794613";
        String response = sendGetRequest(xxpay_login_url, params, "access_token");
        token = response;
        return response;
    }

    public static String create_mch_id() {
        Integer mchId = 20000045;
        String into_mchId = String.valueOf(mchId);
        while (exist_mchId(into_mchId)) {
            mchId = mchId + 1;
            into_mchId = String.valueOf(mchId);
        }
        return into_mchId;
    }

    private static Boolean exist_mchId(String mchId) {
        String access_token;
        if (token != null) {
            access_token = token;
        } else {
            access_token = generateToken();
        }
        String params = "mchId=" + mchId + "&access_token=" + access_token;
        String response = sendGetRequest(xxpay_mch_info_get_url, params, "mchId");
        String get_name = sendGetRequest(xxpay_mch_info_get_url, params, "name");

        String prefix = "ku";
        int number = 0;

        if (get_name.startsWith(prefix)) {
            String numberString = get_name.substring(prefix.length());
            number = Integer.parseInt(numberString);
            number++;
        }

        if (number != 0) {
            name = "ku" + String.valueOf(number);
        }

        if (response == "") {
            return false;
        } else {
            return true;
        }
    }

    private static String sendGetRequest(String urlString, String params, String type) {
        String fullUrl = urlString + "?" + params;
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.toString());
                    JsonNode data;
                    switch (type) {
                        case "access_token":
                            data = root.path("data").path("access_token");
                            return data.asText();
                        case "mchId":
                            data = root.path("data").path("mchId");
                            return data.asText();
                        case "name":
                            data = root.path("data").path("name");
                            return data.asText();
                        case "mobile":
                            data = root.path("data").path("mobile");
                            return data.asText();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new IOException("GET request failed with response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String sendPostRequest(String urlString, String params) {
        String fullUrl = urlString + "?" + params;
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                return response.toString();

            } else {
                throw new IOException("GET request failed with response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static int generateRandomLength(int minLength, int maxLength) {
        Random random = new Random();
        return random.nextInt(maxLength - minLength + 1) + minLength;
    }

    private static String generatePassword(int length) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Generate at least 2 digits
        int digitsCount = 0;
        while (digitsCount < 2) {
            char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            if (Character.isDigit(randomChar)) {
                password.append(randomChar);
                digitsCount++;
            }
        }

        // Generate remaining characters
        for (int i = 0; i < length - 2; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        // Shuffle the password characters
        char[] passwordChars = password.toString().toCharArray();
        for (int i = 0; i < passwordChars.length; i++) {
            int randomIndex = random.nextInt(passwordChars.length);
            char temp = passwordChars[i];
            passwordChars[i] = passwordChars[randomIndex];
            passwordChars[randomIndex] = temp;
        }

        return new String(passwordChars);
    }
}
