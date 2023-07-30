package org.springybot.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.Data;
import java.util.HashMap;

public class ResponseUtils {
    private ResponseUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum) {

        ResponseData responseData = new ResponseData(retEnum.getCode());
        if (retEnum.getCode() != 200) {
            responseData.setMsg(retEnum.getMessage());
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum,
            HashMap<Object, Object> data) {

        ResponseData responseData = new ResponseData(retEnum.getCode(), data);
        if (retEnum.getCode() != 200) {
            responseData.setMsg(retEnum.getMessage());
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum,
            Object data) {

        ResponseData responseData = new ResponseData(retEnum.getCode(), data);
        if (retEnum.getCode() != 200) {
            responseData.setMsg(retEnum.getMessage());
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum,
            String msg) {

        String message = retEnum.getCode() == 200 ? msg : retEnum.getMessage();
        ResponseData responseData = new ResponseData(retEnum.getCode(), message);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseData> response(RetEnum retEnum,
            HashMap<Object, Object> data,
            String msg) {
        String message = retEnum.getCode() == 200 ? msg : retEnum.getMessage();
        ResponseData responseData = new ResponseData(retEnum.getCode(), message, data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @Data
    public static class ResponseData {
        private int code;
        private Object data;
        private String msg;

        public ResponseData(int code) {
            this.code = code;
        }

        public ResponseData(int code, Object data) {
            this.code = code;
            this.data = data;
        }

        public ResponseData(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public ResponseData(int code, String msg, Object data) {
            this.code = code;
            this.data = data;
            this.msg = msg;
        }

    }
}