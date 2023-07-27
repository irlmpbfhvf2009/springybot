package org.springybot.utils;

/**
 * 調用返回碼枚舉類
 * Created by leo on 2023/3/4.
 */
public enum RetEnum {

    // 200: 成功
    RET_SUCCESS(200),

    // 業務相關
    RET_USER_EXIST(1001, "用戶已經存在"),
    RET_USER_NOT_EXIST(1002, "用戶不存在"),
    RET_USER_PASSWORD_ERROR(1003, "用戶密碼錯誤"),
    RET_USER_DISABLED(1004, "用戶停用中"),
    RET_LOGIN_FAIL(1005, "登入失敗"),

    // BOT相關
    RET_START_FAIL(1006, "啟動失敗"),
    RET_START_NOT_EXIST(1007, "BOT不存在,請重新載入頁面"),
    RET_START_EXIST(1008, "程序進行中"),
    RET_STOP_FAIL(1009, "停止失敗"),
    RET_TOKEN_EMPTY(1010, "啟動失敗，非法Token"),
    RET_CHANNEL_MANAGED_NOT_FOUND(1011, "無管理的頻道"),

    // 參數校驗
    RET_PARAM_NOT_FOUND(0101, "参数不存在"),
    RET_PARAM_INVALID(0102, "無效参数"),
    RET_PARAM_TOO_LARGE_LIST(0103, "列表過長"),
    RET_PARAM_TYPE_INVALID(0104, "参数類型錯誤"),
    RET_CURRENT_PAGE_INVALID(0105, "當前頁碼非法"),
    RET_VIEW_NUMBER_INVALID(0106, "分頁顯示數目非法"),
    RET_VIEW_LIMIT_INVALID(0107, "數據排列顯示數目非法"),

    // DB操作相關
    RET_DB_FAIL(0201, "數據庫操作失敗"),

    // 未知錯誤
    RET_UNKNOWN_ERROR(9999, "未知錯誤");

    private int code;
    private String message;

    private RetEnum(int code) {
        this.code = code;
    }

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
