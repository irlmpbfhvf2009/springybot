package com.lwdevelop.utils;

/**
 * 調用返回碼枚舉類
 * Created by leo on 2023/3/4.
 */
public enum RetEnum {

    // 200: 成功
    RET_SUCCESS(200, ""),

    // 業務相關
    RET_USER_EXIST(1001, "用戶已經存在"),
    RET_USER_NOT_EXIST(1002, "用戶不存在"),
    RET_USER_PASSWORD_ERROR(1003, "用戶密碼錯誤"),
    RET_USER_DISABLED(1004, "用戶停用中"),
    RET_LOGIN_FAIL(1005, "登入失敗"),

    // 失败(01开始标示参数校验相关错误码)
    RET_PARAM_NOT_FOUND(0101, "参数不存在"),
    RET_PARAM_INVALID(0102, "无效的参数"),
    RET_PARAM_TOO_LARGE_LIST(0103, "列表超长"),
    RET_PARAM_TYPE_INVALID(0104, "参数类型错误"),
    RET_CURRENT_PAGE_INVALID(0105, "当前页码非法"),
    RET_VIEW_NUMBER_INVALID(0106, "分页显示数目非法"),
    RET_VIEW_LIMIT_INVALID(0107, "数据排列显示数目非法"),

    // 失败(02开始标示DB操作相关错误码)
    RET_DB_FAIL(0201, "數據庫操作失敗"),

    // 未知错误
    RET_UNKNOWN_ERROR(9999, "未知錯誤");

    private int code;
    private String message;

    private RetEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
