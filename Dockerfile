# 使用官方的OpenJDK映像作為基礎
FROM openjdk:11

# 設定工作目錄
WORKDIR /app

# 複製Spring Boot應用程式的JAR檔到容器中
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# 安裝MySQL客戶端
RUN apt-get update && apt-get install -y default-mysql-client

# 暴露應用程式的埠號
EXPOSE 5487

# 設定啟動指令
CMD ["java", "-jar", "app.jar"]
