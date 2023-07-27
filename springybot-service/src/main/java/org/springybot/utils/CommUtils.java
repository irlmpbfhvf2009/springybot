package org.springybot.utils;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Getter;
import eu.bitwalker.useragentutils.OperatingSystem;

/**
 * 一般工具類別
 */
public class CommUtils {

    /**
     * 判斷字串是否為空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static String getClientIP(HttpServletRequest request) {
        String ipAddress;

        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddress.contains(",")) {
                return ipAddress.split(",")[0];
            } else {
                return ipAddress;
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        if (ipAddress != null && ipAddress.length() > 255)
            ipAddress.substring(0, 255);

        return ipAddress;
    }

    // 取得使用者端設備名稱
    public static String getClientDevice(HttpServletRequest request) {
        String device = "";

        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem agent = userAgent.getOperatingSystem();

        device = agent.getDeviceType().getName() + "-" + agent.getName();

        return device;
    }

    // 取得序列號：UUID
    public static String getUUIDSerialNo() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Object Pager(int page, int pageSize, int total) {
        Pager pager = new Pager(page, pageSize, total);
        return pager;
    }

    @Getter
    public static class Pager {
        private final int page;
        private final int pageSize;
        private final int total;

        public Pager(int page, int pageSize, int total) {
            this.page = page;
            this.pageSize = pageSize;
            this.total = total;
        }

    }

}
