# Docker 部署

## 一、为什么用 Docker 部署

对比手动部署：

| 手动部署 | Docker 部署 |
|----------|------------|
| 手动装 JDK、MySQL、Redis | 一个命令拉取镜像 |
| 环境不一致（开发能跑，服务器不行） | 环境一致，镜像即环境 |
| 多个应用可能冲突 | 容器隔离，互不干扰 |
| 升级/回滚麻烦 | 切换镜像版本即可 |

**前提**：已学完阶段七第五课 Docker 基础。

## 二、单应用 Docker 部署

### 2.1 准备 Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/myapp-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
```

### 2.2 构建和运行

```bash
# 本地打包
mvn clean package -DskipTests

# 构建镜像
docker build -t myapp:1.0 .

# 运行（连接宿主机的 MySQL 和 Redis）
docker run -d --name myapp \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/study_db \
  -e SPRING_DATA_REDIS_HOST=host.docker.internal \
  myapp:1.0

# 查看日志
docker logs -f myapp
```

## 三、Docker Compose 全套部署

生产环境通常 MySQL、Redis 也用容器跑，用 Docker Compose 一键编排。

### 3.1 项目结构

```
deployment/
├── docker-compose.yml
├── Dockerfile
├── app.jar              ← 打包好的 jar
├── mysql/
│   └── init.sql         ← 初始化 SQL
└── nginx/
    └── default.conf     ← Nginx 配置
```

### 3.2 docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: rootpass123
      MYSQL_DATABASE: study_db
      MYSQL_USER: appuser
      MYSQL_PASSWORD: apppass123
    volumes:
      - mysql-data:/var/lib/mysql
      - ./mysql/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - app-net
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7
    container_name: redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - app-net

  app:
    build: .
    container_name: myapp
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/study_db
      SPRING_DATASOURCE_USERNAME: appuser
      SPRING_DATASOURCE_PASSWORD: apppass123
      SPRING_DATA_REDIS_HOST: redis
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_started
    networks:
      - app-net
    restart: always

  nginx:
    image: nginx:latest
    container_name: nginx
    ports:
      - "80:80"
    volumes:
      - ./nginx/default.conf:/etc/nginx/conf.d/default.conf
    depends_on:
      - app
    networks:
      - app-net

volumes:
  mysql-data:
  redis-data:

networks:
  app-net:
```

### 3.3 Nginx 配置

```nginx
server {
    listen 80;
    server_name localhost;

    location / {
        proxy_pass http://app:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

注意容器间通信用**服务名**（`app`、`mysql`、`redis`），不用 `localhost`。

### 3.4 部署命令

```bash
# 一键启动
docker-compose up -d

# 查看状态
docker-compose ps

# 查看应用日志
docker-compose logs -f app

# 停止所有
docker-compose down

# 停止并删除数据卷（慎用！会丢数据）
docker-compose down -v
```

## 四、镜像管理

### 4.1 镜像仓库

```bash
# 登录 Docker Hub（或私有仓库）
docker login

# 打标签
docker tag myapp:1.0 yourusername/myapp:1.0

# 推送
docker push yourusername/myapp:1.0

# 在服务器上拉取
docker pull yourusername/myapp:1.0
```

### 4.2 版本更新

```bash
# 构建新版本
docker build -t myapp:2.0 .

# 更新 docker-compose.yml 中的版本号，然后
docker-compose up -d --build

# 回滚：改回旧版本号，重新 up
```

## 五、生产环境注意事项

| 事项 | 说明 |
|------|------|
| 不要用 latest 标签 | 用明确版本号，方便回滚 |
| 数据卷持久化 | MySQL、Redis 数据必须挂载卷 |
| 资源限制 | 给容器设置内存/CPU 限制 |
| 日志收集 | 用 ELK 或其他方案统一收集 |
| 健康检查 | 配置 healthcheck，异常自动重启 |
| 安全 | 不暴露数据库端口到公网 |

## 练习

### 练习 1：完整部署

用 Docker Compose 一键部署 MySQL + Redis + Spring Boot 应用，访问验证功能正常。

### 练习 2：版本更新

1. 修改应用代码，重新打包构建 2.0 版本
2. 用 `docker-compose up -d --build` 更新
3. 确认新版本生效，数据库数据不丢失

### 练习 3：思考题

1. Docker 部署相比手动部署 jar 有什么优势？
2. 为什么容器间通信用服务名而不是 localhost？
3. `docker-compose down` 和 `docker-compose down -v` 的区别是什么？
