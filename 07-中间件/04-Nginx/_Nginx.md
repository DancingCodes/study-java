# 第七阶段 · 第四课：Nginx

---

## 一、Nginx 简介

### 什么是 Nginx？

Nginx 是一个高性能的 **HTTP 服务器** 和 **反向代理服务器**，特点：
- **高并发**：支持数万并发连接，内存消耗低
- **反向代理**：隐藏后端服务器，统一入口
- **负载均衡**：将请求分发到多个后端服务器
- **静态资源托管**：高效处理 HTML/CSS/JS/图片

### 正向代理 vs 反向代理

| 类型 | 代理谁 | 场景 |
|------|--------|------|
| 正向代理 | 代理**客户端** | VPN、翻墙、隐藏客户端IP |
| 反向代理 | 代理**服务端** | 隐藏后端服务器、负载均衡、SSL |

```
正向代理：客户端 → [代理] → 服务器（服务器不知道真实客户端）
反向代理：客户端 → [Nginx] → 后端服务器（客户端不知道真实服务器）
```

---

## 二、Nginx 安装

### Docker 安装（推荐）

```bash
# 拉取镜像
docker pull nginx:latest

# 启动容器
docker run -d --name nginx \
  -p 80:80 \
  -p 443:443 \
  nginx:latest

# 验证
curl http://localhost
```

访问 http://localhost 看到 "Welcome to nginx!" 即成功。

### 常用命令

```bash
nginx -t              # 测试配置文件是否正确
nginx -s reload       # 重新加载配置（不停服务）
nginx -s stop         # 停止
nginx -s quit         # 优雅退出
nginx -V              # 查看版本和编译参数
```

---

## 三、Nginx 配置文件结构

```nginx
# 全局配置
worker_processes auto;  # 工作进程数，auto=CPU核心数

events {
    worker_connections 1024;  # 每个进程最大连接数
}

http {
    # HTTP 全局配置
    include       mime.types;
    default_type  application/octet-stream;
    sendfile      on;
    keepalive_timeout 65;

    # 日志格式
    log_format main '$remote_addr - $request - $status';
    access_log /var/log/nginx/access.log main;

    # 可以有多个 server 块
    server {
        listen 80;
        server_name localhost;

        location / {
            root   /usr/share/nginx/html;
            index  index.html;
        }
    }
}
```

### 配置层级

```
nginx.conf
├── 全局块（worker_processes 等）
├── events 块（连接配置）
└── http 块
    ├── http 全局（日志、gzip 等）
    ├── server 块（虚拟主机 1）
    │   ├── listen / server_name
    │   └── location 块（路由规则）
    └── server 块（虚拟主机 2）
```

---

## 四、反向代理

### 基本配置：将请求转发到 Spring Boot 应用

```nginx
server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

### 按路径分发

```nginx
server {
    listen 80;
    server_name www.example.com;

    # API 请求转发到后端
    location /api/ {
        proxy_pass http://localhost:8080/;
    }

    # 静态文件由 Nginx 直接处理
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;  # SPA 路由支持
    }

    # 图片等静态资源
    location /uploads/ {
        alias /data/uploads/;
        expires 30d;  # 浏览器缓存30天
    }
}
```

---

## 五、负载均衡

### 配置多个后端实例

```nginx
upstream backend {
    server 192.168.1.10:8080;
    server 192.168.1.11:8080;
    server 192.168.1.12:8080;
}

server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://backend;
    }
}
```

### 负载均衡策略

| 策略 | 配置 | 说明 |
|------|------|------|
| **轮询** | 默认 | 按顺序逐个分配 |
| **权重** | `weight=3` | 按权重分配，性能好的服务器给更多请求 |
| **IP Hash** | `ip_hash` | 同一 IP 固定到同一后端（解决 Session 问题） |
| **最少连接** | `least_conn` | 优先分配给连接数最少的服务器 |

```nginx
# 权重轮询
upstream backend {
    server 192.168.1.10:8080 weight=3;  # 处理 3/6 请求
    server 192.168.1.11:8080 weight=2;  # 处理 2/6 请求
    server 192.168.1.12:8080 weight=1;  # 处理 1/6 请求
}

# IP Hash
upstream backend {
    ip_hash;
    server 192.168.1.10:8080;
    server 192.168.1.11:8080;
}

# 最少连接
upstream backend {
    least_conn;
    server 192.168.1.10:8080;
    server 192.168.1.11:8080;
}
```

---

## 六、静态资源托管

### 前后端分离部署

```nginx
server {
    listen 80;
    server_name www.example.com;

    # 前端静态文件
    location / {
        root /usr/share/nginx/html/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API
    location /api/ {
        proxy_pass http://localhost:8080/;
    }
}
```

### Gzip 压缩

```nginx
http {
    gzip on;
    gzip_min_length 1k;
    gzip_types text/plain application/json application/javascript text/css;
    gzip_comp_level 6;
}
```

---

## 七、HTTPS 配置

```nginx
server {
    listen 443 ssl;
    server_name www.example.com;

    ssl_certificate     /etc/nginx/ssl/cert.pem;
    ssl_certificate_key /etc/nginx/ssl/key.pem;

    location / {
        proxy_pass http://localhost:8080;
    }
}

# HTTP 自动跳转 HTTPS
server {
    listen 80;
    server_name www.example.com;
    return 301 https://$host$request_uri;
}
```

---

## 八、实际部署示例

### Spring Boot + Vue 前后端分离

```nginx
server {
    listen 80;
    server_name myapp.com;

    # 前端 Vue 打包产物
    location / {
        root /usr/share/nginx/html/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_connect_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 上传文件目录
    location /uploads/ {
        alias /data/uploads/;
        expires 7d;
    }
}
```

---

## 九、练习

### 练习 1：反向代理
用 Docker 启动 Nginx，将 80 端口的请求代理到本机 8080 端口的 Spring Boot 应用。

### 练习 2：负载均衡
启动两个 Spring Boot 实例（8080 和 8081），配置 Nginx 轮询负载均衡，观察请求分发。

### 练习 3：前后端分离部署
将一个 HTML 文件放到 Nginx 的静态目录，通过 Nginx 配置实现前端文件和后端 API 的分离访问。
