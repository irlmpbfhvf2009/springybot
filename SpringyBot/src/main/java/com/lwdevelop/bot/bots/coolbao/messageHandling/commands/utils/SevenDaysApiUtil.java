package com.lwdevelop.bot.bots.coolbao.messageHandling.commands.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdevelop.bot.bots.coolbao.messageHandling.PrivateMessage_;
import com.lwdevelop.utils.HttpRequestUtil;

public class SevenDaysApiUtil extends HttpRequestUtil {

    protected final static String SEVENDAYS_VERCODE = "https://qimchlaf6a1mnqu4.szzzv.xyz/api/anon/auth/vercode";
    protected final static String SEVENDAYS_VALIDATE = "https://qimchlaf6a1mnqu4.szzzv.xyz/api/anon/auth/validate?ia=cWM4ODk5&ip=YWExMjM0NTY=&vc=060157&vt=70a2b7bc6638484eb589613b6e3741dd&gc=918093";

    public String imageBase64Data;
    public String vercodeToken;

    protected String generateToken() {

        return "";
    }

    protected void generateImage() {
        HashMap<String, String> data = sendGetRequest(SEVENDAYS_VERCODE);
        this.imageBase64Data = data.get("imageBase64Data");
        this.vercodeToken = data.get("vercodeToken");
    }

    protected static String sendPostRequest(String urlString, String type) {
        String fullUrl = urlString;
        String ia = "cWM4ODk5";
        String ip = "YWExMjM0NTY=";
        String vc = PrivateMessage_.vc;
        String vt = PrivateMessage_.vt;
        String gc = PrivateMessage_.gc;

        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); // 设置请求方法为POST
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // 设置Content-Type为application/json，并指定charset为UTF-8
            String requestBody = "{\"ia\":\"" + ia + "\",\"ip\":\"" + ip + "\",\"vc\":\"" + vc + "\",\"vt\":\"" + vt
                    + "\",\"gc\":\"" + gc + "\"}";
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();
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


                    data = root.path("data").path(type);
                    return data.asText();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                throw new IOException("POST request failed with response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, String> sendGetRequest(String urlString) {
        String fullUrl = urlString;
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
                    HashMap<String, String> map = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.toString());
                    JsonNode imageBase64DataNode;
                    JsonNode vercodeTokenNode;
                    imageBase64DataNode = root.path("data").path("imageBase64Data");
                    vercodeTokenNode = root.path("data").path("vercodeToken");
                    map.put("imageBase64Data", imageBase64DataNode.asText());
                    map.put("vercodeToken", vercodeTokenNode.asText());
                    return map;
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
