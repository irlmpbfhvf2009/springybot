package com.lwdevelop.backend;

// import java.net.InetAddress;
// import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    public void test() {
        // try {
        //     InetAddress localhost = InetAddress.getLocalHost();
        //     String localhostIp = localhost.getHostAddress();

        //     // 獲取當前服務器的IP地址
        //     String serverIp = InetAddress.getLocalHost().getHostAddress();
        //     String str = "";
        //     // 判斷當前是本地還是服務器上啟動的
        //     if (serverIp.equals("127.0.0.1")) {
        //         str = "服務器啟動";
        //     } else {
        //         str = "本地啟動";
        //     }
        //     str = str + "localhostIp:" + localhostIp + " serverIp:" + serverIp;
        //     System.out.println(str);
        // } catch (UnknownHostException e) {
        //     e.printStackTrace();
        // }
    }

}
