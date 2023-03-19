# backend

這個客服系統的架構使用了多個技術，包括Spring Boot、WebSocket、RabbitMQ、和Security。以下是一些關於這個系統的詳細信息：

Spring Boot: Spring Boot是一個快速開發框架，能夠幫助開發者快速搭建Web應用程序。在這個客服系統中，Spring Boot用於構建整個應用程序的框架。

WebSocket: WebSocket是一種基於TCP協議的通信協議，可以實現全雙工通信。在這個客服系統中，WebSocket用於實現客戶與客服人員之間的即時通信。

RabbitMQ: RabbitMQ是一種消息隊列，可以實現分布式系統中的異步通信。在這個客服系統中，RabbitMQ用於處理客戶的請求，並將它們分發到相應的客服人員。

Security: Security是Spring框架中的安全框架，用於保護Web應用程序免受各種攻擊。在這個客服系統中，Security用於實現管理員帳號密碼登入和IP驗證。

管理員可以使用其帳號密碼登入到系統後台，以便進行系統管理和客服人員分配。客戶的身分則通過IP地址來辨識，這樣可以避免客戶需要註冊和登入。當客戶發送消息時，系統會使用RabbitMQ將消息分發到可用的客服人員中。當客戶和客服人員之間進行通信時，系統會使用WebSocket實現即時通信。

project  
│   README.md  
│   pom.xml  
│  
└───src  
│   └───main  
│       ├───java  
│       │   └───com.example  
│       │       ├───config  
│       │       │   ├───RabbitMQConfig.java  
│       │       │   ├───SecurityConfig.java  
│       │       │   └───WebSocketConfig.java  
│       │       ├───controller  
│       │       │   ├───AdminPanelController.java  
│       │       │   └───ChatController.java  
│       │       ├───dto  
│       │       │   ├───AdminDTO.java  
│       │       │   ├───ChatMessageDTO.java  
│       │       │   └───UserDTO.java  
│       │       ├───model  
│       │       │   ├───Admin.java  
│       │       │   ├───ChatMessage.java  
│       │       │   └───User.java  
│       │       ├───repository  
│       │       │   ├───AdminRepository.java  
│       │       │   ├───ChatMessageRepository.java  
│       │       │   └───UserRepository.java  
│       │       ├───service     
│       │       │   ├───AdminService.java  
│       │       │   ├───ChatMessageService.java  
│       │       │   ├───UserService.java  
│       │       │   └───impl  
│       │       │       ├───AdminServiceImpl.java  
│       │       │       ├───ChatMessageServiceImpl.java  
│       │       │       └───UserServiceImpl.java  
│       │       └───WebSocketHandler.java   以此架構分別寫出ChatMessageServiceImpl.java、UserServiceImpl.java  
│       └───resources   以此架構分別寫出WebSocketHandler.java  
│           ├───static  
│           │   ├───css  
│           │   ├───js  
│           │   └───img  
│           ├───templates  
│           │   ├───admin_panel.html  
│           │   └───chat.html  
│           └───application.properties  
└───test  
    └───java  
        └───com.example  
            ├───controller  
            ├───service  
            │   ├───impl  
            │   └───mock  
            └───WebSocketHandlerTest.java  


