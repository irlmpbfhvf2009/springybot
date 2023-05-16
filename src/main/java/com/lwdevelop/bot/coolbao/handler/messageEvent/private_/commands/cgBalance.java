package com.lwdevelop.bot.coolbao.handler.messageEvent.private_.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdevelop.bot.coolbao.utils.Common;

public class cgBalance {

    private final static String xxpay_account_url = "http://4pay.ddb22.vip/xxpay-manage/api/account/get";
    private final static String xxpay_login_url = "http://4pay.ddb22.vip/xxpay-manage/api/auth";

    public String cmd(Common common) {

        String token = generateToken();
        String cg01_params = "mchId=20000007&access_token="+token;
        String cg02_params = "mchId=20000008&access_token="+token;
        String cg03_params = "mchId=20000009&access_token="+token;
        String cg04_params = "mchId=20000010&access_token="+token;

        String cg01 = sendGetRequest(xxpay_account_url, cg01_params);
        String cg02 = sendGetRequest(xxpay_account_url, cg02_params);
        String cg03 = sendGetRequest(xxpay_account_url, cg03_params);
        String cg04 = sendGetRequest(xxpay_account_url, cg04_params);
        
        double number = Double.parseDouble(cg01);
        double cg01_result = number / 100;
        String cg01_ = String.valueOf(cg01_result);

        double number2 = Double.parseDouble(cg02);
        double cg02_result = number2 / 100;
        String cg02_ = String.valueOf(cg02_result);

        double number3 = Double.parseDouble(cg03);
        double cg03_result = number3 / 100;
        String cg03_ = String.valueOf(cg03_result);

        double number4 = Double.parseDouble(cg04);
        double cg04_result = number4 / 100;
        String cg04_ = String.valueOf(cg04_result);

        String str = "cg01   " + cg01_ + "\n" +
                "cg02   " + cg02_ + "\n" +
                "cg03   " + cg03_ + "\n" +
                "cg04   " + cg04_ + "\n";

        return str;
    }

    private static String sendGetRequest(String urlString, String params) {
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
                    JsonNode data = root.path("data").path("balance");
                    ;
                    return data.asText();
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

    private static String generateToken() {
        String params = "username=leo&password=as794613";
        String response = sendGetRequest(xxpay_login_url, params, "access_token");
        return response;
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
}
