package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdevelop.bot.coolbaoBot.utils.Common;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class addMerchant {

    private final static String xxpay_login_url = "http://4pay.ddb22.vip/xxpay-manage/api/auth";
    private final static String xxpay_mch_info_get_url = "http://4pay.ddb22.vip/xxpay-manage/api/mch_info/get";
    private static String access_token ="";

    public void am(Common common) {
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());
        String text = message.getText();

        int length = generateRandomLength(8, 16);
        String password = generatePassword(length);
        log.info("Generated Password: {}", password);

        String str = "商户后台:http://4pay.ddb22.vip/xxpay-merchant/x_mch/start/index.html#/user/login/redirect=%2F\n\n" +
                "登录账号 : " + text + "\n" +
                "登录密码 : " + password + "\n" +
                "支付密码 : " + password + "\n" +
                "商户ID：20000045\n\n" +
                "温馨提醒:\n" +
                "1.登录后请尽早修改密码\n" +
                "2.建议尽早设置谷歌验证码，保障安全；\n" +
                "3.请参考清单下方对接文挡完成对接";

        SendMessage response = new SendMessage(chatId, str);
        common.sendResponseAsync(response);
        common.getUserState().put(message.getChatId(), "");

    }

    private static String generateToken() {
        String params = "username=leo&password=as794613";
        String response = sendRequest(xxpay_login_url,params,"access_token");
        return response;
    }

    public static void main(String[] args) {
        create_mch_id();
    }

    public static String create_mch_id() {
        Integer mchId = 20000045;
        String into_mchId= String.valueOf(mchId);
        while(exist_mchId(into_mchId)){
            mchId=mchId+1;
            into_mchId = String.valueOf(mchId);
        }
        return into_mchId;
    }

    private static Boolean exist_mchId(String mchId){
        String params = "mchId="+mchId+"&access_token="+generateToken();
        String response = sendRequest(xxpay_mch_info_get_url,params,"mchId");
        if(response==""){
            return false;
        }else{
            return true;
        }
    }

    private static String sendRequest(String urlString, String params,String type) {
        String fullUrl = urlString + "?" + params;
        try{
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
                    switch(type){
                        case "access_token":
                        data = root.path("data").path("access_token");
                        return data.asText();
                        case "mchId":
                        data = root.path("data").path("mchId");
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
