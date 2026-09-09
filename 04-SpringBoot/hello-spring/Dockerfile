# 第一阶段：用 Maven 打包
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：用轻量 JDK 运行
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 启动时激活 prod 环境
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
