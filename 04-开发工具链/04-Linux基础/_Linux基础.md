# Linux 基础

## 一、为什么要学 Linux

Java 项目几乎都部署在 **Linux 服务器**上，不是 Windows。所以你必须会：

1. **连接服务器** — 通过 SSH 远程登录
2. **基本操作** — 查看文件、编辑配置、管理进程
3. **部署项目** — 上传 jar 包、启动/停止服务

工作中不需要精通 Linux，但基本命令必须熟练。

## 二、Linux 目录结构

Linux 没有 C盘 D盘，一切从根目录 `/` 开始：

```
/
├── home/          ← 用户主目录（你的文件放这里）
│   └── zhang/     ← 你的用户目录（~）
├── root/          ← root 用户的主目录
├── etc/           ← 配置文件（nginx.conf、my.cnf 等）
├── var/           ← 日志、数据（/var/log）
├── usr/           ← 安装的软件（/usr/local）
├── opt/           ← 可选软件安装目录
├── tmp/           ← 临时文件
└── bin/           ← 系统命令
```

**常用的**：`/home`（你的文件）、`/etc`（配置）、`/var/log`（日志）、`/usr/local`（软件安装）。

## 三、文件和目录操作

### 3.1 目录操作

```bash
# 查看当前目录
pwd

# 切换目录
cd /home/zhang         # 去指定目录
cd ~                   # 回到用户主目录
cd ..                  # 回到上一级
cd -                   # 回到上次的目录

# 查看目录内容
ls                     # 简单列出
ls -l                  # 详细信息（权限、大小、时间）
ls -la                 # 包含隐藏文件（.开头的）
ls -lh                 # 文件大小用 KB/MB 显示

# 创建目录
mkdir mydir            # 创建单个目录
mkdir -p a/b/c         # 递归创建多级目录

# 删除目录
rmdir mydir            # 删除空目录
rm -rf mydir           # 删除目录及所有内容（慎用！）
```

### 3.2 文件操作

```bash
# 创建文件
touch hello.txt        # 创建空文件

# 查看文件
cat hello.txt          # 查看全部内容（短文件）
head -20 hello.txt     # 看前 20 行
tail -20 hello.txt     # 看后 20 行
tail -f app.log        # 实时跟踪日志（最常用！）
less hello.txt         # 分页查看（按 q 退出）

# 复制
cp file1 file2         # 复制文件
cp -r dir1 dir2        # 复制目录

# 移动/重命名
mv old.txt new.txt     # 重命名
mv file.txt /home/     # 移动到另一个目录

# 删除
rm file.txt            # 删除文件
rm -rf dir/            # 删除目录（不可恢复，慎用！）
```

### 3.3 命令速查

| 命令 | 作用 | 记忆 |
|------|------|------|
| `pwd` | 当前目录 | Print Working Directory |
| `ls -la` | 列出所有文件 | List |
| `cd` | 切换目录 | Change Directory |
| `mkdir -p` | 创建目录 | Make Directory |
| `cp -r` | 复制 | Copy |
| `mv` | 移动/重命名 | Move |
| `rm -rf` | 删除 | Remove（慎用！） |
| `cat` | 查看文件 | Concatenate |
| `tail -f` | 实时看日志 | 最常用 |

## 四、文件权限

`ls -l` 输出示例：

```
-rw-r--r-- 1 zhang zhang 1024 Sep 10 10:00 hello.txt
│ │  │  │
│ │  │  └── 其他人：r--（只读）
│ │  └───── 所属组：r--（只读）
│ └──────── 所有者：rw-（读写）
└────────── 文件类型：- 普通文件，d 目录
```

权限含义：
- `r`（read）= 4 — 可读
- `w`（write）= 2 — 可写
- `x`（execute）= 1 — 可执行

```bash
# 修改权限
chmod 755 script.sh    # 所有者 rwx，组和其他人 r-x
chmod 644 config.yml   # 所有者 rw-，组和其他人 r--
chmod +x script.sh     # 给文件加执行权限
```

**常用权限**：
- `755` — 脚本文件（所有者可读写执行，其他人可读可执行）
- `644` — 配置文件（所有者可读写，其他人只读）

## 五、查找和搜索

```bash
# 查找文件
find /home -name "*.log"           # 在 /home 下找所有 .log 文件
find / -name "nginx.conf"          # 全盘找 nginx 配置文件

# 搜索文件内容
grep "error" app.log               # 在日志中搜索 error
grep -i "error" app.log            # 忽略大小写
grep -n "error" app.log            # 显示行号
grep -r "TODO" /home/project/      # 递归搜索目录

# 管道（把前一个命令的输出传给后一个）
cat app.log | grep "error"         # 查看日志中包含 error 的行
ps -ef | grep java                 # 找 Java 进程
```

## 六、进程管理

```bash
# 查看进程
ps -ef                             # 所有进程
ps -ef | grep java                 # 找 Java 进程

# 杀死进程
kill PID                           # 正常终止
kill -9 PID                        # 强制终止（杀不掉就用这个）

# 查看端口占用
netstat -nltp                      # 查看所有监听端口
netstat -nltp | grep 8080          # 看 8080 端口被谁占了
# 或
lsof -i :8080                      # 查看 8080 端口
```

**工作中最常用的场景**：部署的服务挂了或端口冲突，用 `ps` 找进程、`kill` 杀进程、`netstat` 看端口。

## 七、网络相关

```bash
# 测试网络连通
ping baidu.com                     # 测试能不能访问

# 下载文件
wget https://xxx.com/file.zip      # 下载文件
curl https://api.example.com/users # 请求接口（测试 API）

# 查看 IP
ip addr                            # 查看本机 IP
# 或
ifconfig                           # 旧版命令
```

## 八、Vim 编辑器

服务器上没有 IDEA，临时改配置文件就用 Vim。

### 8.1 三种模式

```
命令模式 ←→ 插入模式 ←→ 底行模式
   ↑          按 i 进入
   按 Esc 回来
                          按 : 进入
```

### 8.2 最小化使用（记住这些就够了）

```bash
vim config.yml             # 打开文件
```

1. 按 `i` — 进入编辑模式，可以打字了
2. 编辑完按 `Esc` — 退出编辑模式
3. 输入 `:wq` 回车 — 保存并退出
4. 输入 `:q!` 回车 — 不保存退出（改错了用这个）

### 8.3 常用操作

| 按键 | 作用 | 模式 |
|------|------|------|
| `i` | 进入插入模式 | 命令模式 |
| `Esc` | 回到命令模式 | 任何模式 |
| `:wq` | 保存退出 | 底行模式 |
| `:q!` | 不保存退出 | 底行模式 |
| `dd` | 删除当前行 | 命令模式 |
| `yy` | 复制当前行 | 命令模式 |
| `p` | 粘贴 | 命令模式 |
| `/关键词` | 搜索 | 命令模式 |
| `u` | 撤销 | 命令模式 |
| `G` | 跳到文件末尾 | 命令模式 |
| `gg` | 跳到文件开头 | 命令模式 |

## 九、实用组合命令

```bash
# 部署 Java 项目常用
nohup java -jar app.jar > app.log 2>&1 &    # 后台运行 jar
tail -f app.log                              # 实时看日志
ps -ef | grep java                           # 找 Java 进程
kill -9 PID                                  # 停止服务

# 查看系统资源
free -h                # 内存使用
df -h                  # 磁盘使用
top                    # CPU 和内存实时监控（按 q 退出）
```

`nohup ... &` 这个命令后面部署项目时天天用，现在记个印象就行。

## 十、SSH 远程连接

```bash
# 连接远程服务器
ssh 用户名@服务器IP
# 例如
ssh root@192.168.1.100

# 上传文件到服务器
scp app.jar root@192.168.1.100:/home/app/

# 从服务器下载文件
scp root@192.168.1.100:/home/app/app.log ./
```

实际工作中一般用 **FinalShell** 或 **Termius** 等可视化 SSH 工具连接，比命令行方便。

## 练习

### 练习 1：基本命令实操

在 Git Bash（Windows 上可以模拟 Linux 命令）中练习：

1. `pwd` — 查看当前目录
2. `ls -la` — 查看当前目录所有文件
3. `mkdir -p test/a/b` — 创建多级目录
4. `touch test/hello.txt` — 创建文件
5. `echo "hello linux" > test/hello.txt` — 写入内容
6. `cat test/hello.txt` — 查看内容
7. `cp test/hello.txt test/hello2.txt` — 复制文件
8. `rm -rf test` — 删除整个 test 目录

### 练习 2：思考题

1. `tail -f app.log` 用在什么场景？
2. 服务部署后发现端口被占用了，怎么排查？
3. `chmod 755` 和 `chmod 644` 分别适合什么文件？
4. 在 Vim 里编辑完文件，怎么保存退出？怎么不保存退出？
