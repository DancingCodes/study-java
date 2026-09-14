# Linux 部署

## 一、部署方式概览

| 方式 | 说明 | 适用场景 |
|------|------|----------|
| **手动部署 jar** | 上传 jar，手动 java -jar 启动 | 学习、小项目 |
| **systemd 服务** | 注册为系统服务，开机自启、自动重启 | 生产环境 |
| **Docker 部署** | 容器化部署（下一课） | 中大型项目 |

本课学习前两种。

## 二、准备工作

### 2.1 服务器环境

```bash
# 查看系统版本
cat /etc/os-release

# 查看内存
free -h

# 查看磁盘
df -h
```

### 2.2 安装 JDK

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-21-jre-headless -y

# CentOS/RHEL
sudo yum install java-21-openjdk -y

# 验证
java -version
```

### 2.3 安装 MySQL

```bash
# Ubuntu
sudo apt install mysql-server -y
sudo systemctl start mysql
sudo systemctl enable mysql

# 初始化
sudo mysql_secure_installation

# 登录
mysql -u root -p

# 创建数据库和用户
CREATE DATABASE study_db CHARACTER SET utf8mb4;
CREATE USER 'appuser'@'%' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL ON study_db.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
```

## 三、手动部署 jar

### 3.1 本地打包

```bash
mvn clean package -DskipTests
```

生成的 jar 在 `target/` 目录下。

### 3.2 上传到服务器

```bash
# 使用 scp 上传
scp target/myapp-1.0.0.jar user@your-server:/home/user/app/

# 或者用 sftp 工具（如 WinSCP、FileZilla）
```

### 3.3 启动

```bash
# 创建应用目录
mkdir -p /home/user/app
cd /home/user/app

# 前台启动（测试用，关终端就停）
java -jar myapp-1.0.0.jar

# 后台启动（nohup + 日志输出）
nohup java -jar myapp-1.0.0.jar > app.log 2>&1 &

# 查看进程
ps aux | grep myapp

# 查看日志
tail -f app.log

# 停止
kill $(pgrep -f myapp)
```

### 3.4 指定环境配置

```bash
# 使用生产环境配置
java -jar myapp-1.0.0.jar --spring.profiles.active=prod

# 指定端口
java -jar myapp-1.0.0.jar --server.port=9090

# JVM 参数
java -Xms256m -Xmx512m -jar myapp-1.0.0.jar
```

## 四、systemd 服务管理

手动启动的问题：
- 服务器重启后应用不会自动启动
- 应用崩溃了不会自动重启
- 管理不方便（找进程号、手动 kill）

用 systemd 注册为系统服务，解决以上所有问题。

### 4.1 创建服务文件

```bash
sudo vim /etc/systemd/system/myapp.service
```

```ini
[Unit]
Description=My Spring Boot Application
After=network.target mysql.service

[Service]
Type=simple
User=user
WorkingDirectory=/home/user/app
ExecStart=/usr/bin/java -Xms256m -Xmx512m -jar myapp-1.0.0.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

关键配置说明：

| 配置 | 说明 |
|------|------|
| `After` | 在网络和 MySQL 启动后再启动 |
| `User` | 运行用户（不要用 root） |
| `ExecStart` | 启动命令 |
| `Restart=always` | 崩溃后自动重启 |
| `RestartSec=10` | 重启间隔 10 秒 |
| `WantedBy` | 开机自启 |

### 4.2 管理服务

```bash
# 重新加载服务文件
sudo systemctl daemon-reload

# 启动
sudo systemctl start myapp

# 停止
sudo systemctl stop myapp

# 重启
sudo systemctl restart myapp

# 查看状态
sudo systemctl status myapp

# 设置开机自启
sudo systemctl enable myapp

# 取消开机自启
sudo systemctl disable myapp

# 查看日志
sudo journalctl -u myapp -f
sudo journalctl -u myapp --since "2024-01-01" --until "2024-01-02"
```

## 五、部署检查清单

| 检查项 | 命令 |
|--------|------|
| 应用是否在运行 | `systemctl status myapp` |
| 端口是否监听 | `ss -tlnp \| grep 8080` |
| 能否访问 | `curl http://localhost:8080` |
| 防火墙是否放通 | `sudo ufw status` 或 `sudo firewall-cmd --list-all` |
| 日志是否有报错 | `journalctl -u myapp -n 50` |
| 磁盘空间 | `df -h` |
| 内存使用 | `free -h` |

## 六、常见问题

| 问题 | 排查 |
|------|------|
| 启动失败 | `journalctl -u myapp` 查看错误日志 |
| 端口被占用 | `ss -tlnp \| grep 端口号`，kill 占用进程 |
| 连不上数据库 | 检查 MySQL 是否运行、用户权限、防火墙 |
| 外网访问不了 | 检查安全组规则（云服务器）和防火墙 |
| 内存不够 | 调小 JVM 参数 `-Xmx`，或升级服务器 |

## 练习

### 练习 1：手动部署

1. 把之前的 redis-demo 项目打包成 jar
2. 在本机（或虚拟机/云服务器）用 `java -jar` 手动启动
3. 用浏览器访问验证功能正常

### 练习 2：systemd 服务

1. 为应用创建 systemd 服务文件
2. 用 `systemctl start/stop/restart/status` 管理
3. 设置开机自启
4. 用 `kill` 模拟应用崩溃，观察是否自动重启

### 练习 3：思考题

1. `nohup java -jar` 和 `systemd` 两种方式各有什么优缺点？
2. 为什么不建议用 root 用户运行应用？
3. 部署前应该检查哪些事项？
