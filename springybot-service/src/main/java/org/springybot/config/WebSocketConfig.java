// package com.lwdevelop.customerServiceAdmin.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
// import com.lwdevelop.customerServiceAdmin.utils.IpHandshakeInterceptor;

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    
//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry registry) {
//         // 訂閱Broker名稱 user點對點 topic廣播即群發
//         registry.enableSimpleBroker("/user", "/topic");
//         registry.setApplicationDestinationPrefixes("/app");
//         registry.setUserDestinationPrefix("/user");
//     }

//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry registry) {
//         // 允許使用socketJs方式訪問 即可通過http://IP:PORT/tmax/ws來和服務端websocket連接
//         registry.addEndpoint("/tmax/ws")
//                 // .setHandshakeHandler(new CustomHandshakeHandler())
//                 .addInterceptors(new IpHandshakeInterceptor())
//                 .setAllowedOriginPatterns("*")
//                 .withSockJS();
//     }


// }
