package com.lwdevelop.bot.bots.coolbao.messageHandling.commands.servendayapi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.utils.SevenDaysApiUtil;
import com.lwdevelop.bot.bots.utils.Common;

public class SevenDaysEnterImageCode extends SevenDaysApiUtil {

    public SevenDaysEnterImageCode(Common common){
        Message message = common.getUpdate().getMessage();
        generateImage();
        String base64Data = imageBase64Data.substring(imageBase64Data.indexOf(",") + 1);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        InputFile inputFile = new InputFile(inputStream, "photo");
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(common.getUpdate().getMessage().getChatId());
        sendPhoto.setPhoto(inputFile);
        common.executeAsync(sendPhoto);
        common.getUserState().put(message.getChatId(), "enterImageCode");
        SendMessage response = new SendMessage(message.getChatId().toString(), "輸入圖片驗證碼");
        common.executeAsync_(response);
    }
    
}
