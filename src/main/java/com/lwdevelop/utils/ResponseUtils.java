package com.lwdevelop.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.Data;
import java.util.HashMap;

public class ResponseUtils {
    private ResponseUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum,
                                                        HashMap<String, Object> data) {


        ResponseData responseData = new ResponseData(retEnum.getCode(), data);
        if(retEnum.getCode() != 200) {
            responseData.setMsg(retEnum.getMessage());
        } 
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum,
                                                        HashMap<String, Object> data,
                                                        String msg) {
        String message = retEnum.getCode() == 200 ? msg : retEnum.getMessage();
        ResponseData responseData = new ResponseData(retEnum.getCode(), message, data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @Data
    public static class ResponseData {
        private final int code;
        private final HashMap<String, Object> data;
        private String msg;

        public ResponseData(int code, HashMap<String, Object> data) {
            this.code = code;
            this.data = data;
        }

        public ResponseData(int code, String msg, HashMap<String, Object> data) {
            this.code = code;
            this.data = data;
            this.msg = msg;
        }

    }
}