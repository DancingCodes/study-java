# 第七阶段 · 第五课：Docker

---

## 一、Docker 简介

### 什么是 Docker？

Docker 是一个**容器化**平台，将应用和依赖打包到一个可移植的容器中运行。

**核心理念**："Build once, run anywhere" —— 开发环境能跑的，生产环境一定能跑。

### 虚拟机 vs 容器

| 对比 | 虚拟机 | Docker 容器 |
|------|--------|------------|
| 隔离级别 | 硬件级别（Hypervisor） | 操作系统级别（共享内核） |
| 启动速度 | 分钟级 | 秒级 |
| 体积 | GB 级 | MB 级 |
| 性能 | 有损耗 | 接近原生 |
| 数量 | 一台机器跑几个 | 一台机器跑几十上百个 |

### 为什么 Java 开发要学 Docker？

1. **环境一致**：不再有"我机器上能跑"的问题
2. **快速部署**：一个命令部署整套环境（MySQL + Redis + 应用）
3. **微服务基础**：每个服务一个容器，独立部署
4. **CI/CD**：自动构建镜像、自动部署

---

## 二、核心概念

| 概念 | 说明 | 类比 |
|------|------|------|
| **镜像（Image）** | 只读模板，包含运行环境和应用 | 类 |
| **容器（Container）** | 镜像的运行实例 | 对象 |
| **仓库（Registry）** | 存放镜像的地方 | Maven 仓库 |
| **Dockerfile** | 构建镜像的脚本 | Makefile |

```
Dockerfile → docker build → Image → docker run → Container
                              ↑                        ↓
                        docker pull              docker stop/rm
                              ↑
                     Docker Hub / 私有仓库
```

---

## 三、Docker 安装

### Windows

1. 安装 Docker Desktop：https://www.docker.com/products/docker-desktop/
2. 确保开启 WSL 2 或 Hyper-V
3. 验证：`docker version`

### 配置镜像加速

Docker Desktop → Settings → Docker Engine，添加：

```json
{
  "registry-mirrors": [
    "https://mirror.ccs.tencentyun.com",
    "https://docker.m.daocloud.io"
  ]
}
```

---

## 四、Docker 常用命令

### 镜像操作

```bash
docker pull nginx:latest        # 拉取镜像
docker images                    # 查看本地镜像
docker rmi nginx:latest          # 删除镜像
docker build -t myapp:1.0 .      # 构建镜像
docker tag myapp:1.0 myapp:latest  # 打标签
```

### 容器操作

```bash
docker run -d --name mynginx -p 80:80 nginx   # 启动容器
docker ps                                       # 查看运行中的容器
docker ps -a                                    # 查看所有容器（含停止的）
docker stop mynginx                             # 停止
docker start mynginx                            # 启动
docker restart mynginx                          # 重启
docker rm mynginx                               # 删除容器
docker rm -f mynginx                            # 强制删除
```

### 调试容器

```bash
docker logs mynginx              # 查看日志
docker logs -f mynginx           # 实时日志
docker exec -it mynginx bash     # 进入容器
docker inspect mynginx           # 查看容器详情
docker cp file.txt mynginx:/tmp/ # 拷贝文件到容器
```

### docker run 常用参数

| 参数 | 说明 | 示例 |
|------|------|------|
| `-d` | 后台运行 | `docker run -d nginx` |
| `--name` | 容器名称 | `--name mynginx` |
| `-p` | 端口映射 | `-p 8080:80` (宿主:容器) |
| `-v` | 数据卷挂载 | `-v /data:/app/data` |
| `-e` | 环境变量 | `-e MYSQL_ROOT_PASSWORD=123456` |
| `--network` | 加入网络 | `--network mynet` |
| `--restart` | 重启策略 | `--restart always` |

---

## 五、数据卷（Volume）

容器删除后数据就丢了，数据卷用于**持久化数据**。

```bash
# 方式1：命名卷（推荐）
docker run -d --name mysql \
  -v mysql-data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -p 3306:3306 \
  mysql:8.0

# 方式2：绑定挂载（指定宿主目录）
docker run -d --name mysql \
  -v /home/data/mysql:/var/lib/mysql \
  -p 3306:3306 \
  mysql:8.0

# 查看卷
docker volume ls
docker volume inspect mysql-data
```

---

## 六、Dockerfile 构建 Java 应用镜像

### 基本 Dockerfile

```dockerfile
# 基础镜像
FROM eclipse-temurin:21-jre

# 工作目录
WORKDIR /app

# 复制 jar 包
COPY target/myapp-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 构建和运行

```bash
# 先打包
mvn clean package -DskipTests

# 构建镜像
docker build -t myapp:1.0 .

# 运行
docker run -d --name myapp -p 8080:8080 myapp:1.0

# 查看日志
docker logs -f myapp
```

### 多阶段构建（推荐）

```dockerfile
# 第一阶段：编译
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行（镜像更小）
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 七、Docker Compose 编排

### 为什么需要 Compose？

一个项目可能依赖 MySQL、Redis、RabbitMQ 等多个服务，一个个 `docker run` 太麻烦。
Docker Compose 用 **YAML 文件** 一次定义和启动多个容器。

### docker-compose.yml 示例

```yaml
version: '3.8'

services:
  # MySQL
  mysql:
    image: mysql:8.0
    container_name: mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: study_db
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - app-net

  # Redis
  redis:
    image: redis:7
    container_name: redis
    ports:
      - "6379:6379"
    networks:
      - app-net

  # Spring Boot 应用
  app:
    build: .  # 使用当前目录的 Dockerfile
    container_name: myapp
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/study_db
      SPRING_DATA_REDIS_HOST: redis
    networks:
      - app-net

volumes:
  mysql-data:

networks:
  app-net:
```

### Compose 常用命令

```bash
docker-compose up -d           # 后台启动所有服务
docker-compose down            # 停止并删除所有服务
docker-compose ps              # 查看服务状态
docker-compose logs -f app     # 查看指定服务日志
docker-compose restart app     # 重启指定服务
docker-compose build           # 重新构建镜像
docker-compose up -d --build   # 重新构建并启动
```

### 注意事项

1. **容器间通信**用服务名（如 `mysql`、`redis`），不用 `localhost`
2. `depends_on` 只保证启动顺序，不保证服务就绪
3. 数据卷保证数据持久化，`docker-compose down` 不会删除卷，`down -v` 会

---

## 八、实际开发流程

```
1. 开发环境：docker-compose up -d（启动 MySQL、Redis 等依赖）
2. 开发代码：IDE 中运行 Spring Boot（连接 Docker 中的数据库）
3. 打包：mvn clean package
4. 构建镜像：docker build -t myapp:1.0 .
5. 部署：docker-compose up -d（含应用容器）
```

---

## 九、常见问题排查

| 问题 | 排查命令 |
|------|----------|
| 容器启动失败 | `docker logs 容器名` |
| 端口冲突 | `docker ps` 查看占用，`netstat -tlnp` |
| 磁盘空间不足 | `docker system df`，`docker system prune` 清理 |
| 容器间无法通信 | 检查是否在同一 network |
| 镜像拉取慢 | 配置镜像加速器 |

---

## 十、练习

### 练习 1：容器化 Spring Boot
将之前的 redis-demo 项目打包成 Docker 镜像并运行。

### 练习 2：Docker Compose 一键部署
编写 docker-compose.yml，一键启动 MySQL + Redis + Spring Boot 应用。

### 练习 3：多阶段构建
使用多阶段构建优化镜像大小，对比单阶段和多阶段的镜像体积。
