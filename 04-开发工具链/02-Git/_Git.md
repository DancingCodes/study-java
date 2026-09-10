# Git

## 一、什么是 Git

Git 是**分布式版本控制系统**，帮你做三件事：

1. **记录每次修改** — 代码写坏了可以随时回退到之前的版本
2. **多人协作** — 多个人同时开发，互不干扰，最后合并
3. **分支管理** — 开发新功能在单独的分支上，不影响主分支

类比：Git 就像游戏的存档系统，每次 commit 就是存一个档，随时可以读档。

## 二、Git 基本概念

```
工作区（Working Directory）  →  暂存区（Stage/Index）  →  本地仓库（Repository）  →  远程仓库（Remote）
        git add                    git commit                  git push
```

- **工作区** — 你写代码的目录，就是你能看到的文件
- **暂存区** — 临时存放修改，等待提交（`git add` 放进来）
- **本地仓库** — 提交历史都存在这里（`git commit` 存进来）
- **远程仓库** — GitHub/Gitee 上的仓库（`git push` 推上去）

## 三、Git 安装与配置

### 3.1 安装

去 [git-scm.com](https://git-scm.com/) 下载安装，一路 Next 即可。

安装完在终端输入 `git --version` 看到版本号就成功了。

### 3.2 初始配置

```bash
# 设置用户名和邮箱（提交记录会显示这些信息）
git config --global user.name "你的名字"
git config --global user.email "你的邮箱"

# 查看配置
git config --list
```

## 四、Git 常用命令

### 4.1 仓库初始化

```bash
# 方式 1：在当前目录初始化
git init

# 方式 2：从远程克隆
git clone https://github.com/xxx/项目名.git
```

### 4.2 日常三连：add → commit → push

```bash
# 1. 查看当前状态（哪些文件修改了、哪些未跟踪）
git status

# 2. 添加文件到暂存区
git add 文件名          # 添加单个文件
git add .              # 添加所有修改

# 3. 提交到本地仓库
git commit -m "提交说明"

# 4. 推送到远程仓库
git push
```

这是最常用的流程，每天用无数次。

### 4.3 查看历史

```bash
# 查看提交历史
git log                    # 详细
git log --oneline          # 一行一条，简洁
git log --oneline -5       # 只看最近 5 条

# 查看某次修改的内容
git diff                   # 工作区 vs 暂存区
git diff --staged          # 暂存区 vs 上次提交
```

### 4.4 撤销操作

```bash
# 撤销工作区修改（还没 add）
git checkout -- 文件名
# 或
git restore 文件名

# 撤销暂存区（已经 add，但还没 commit）
git reset HEAD 文件名
# 或
git restore --staged 文件名

# 回退到某次提交（慎用！）
git reset --hard commitID
```

### 4.5 命令速查表

| 命令 | 作用 |
|------|------|
| `git init` | 初始化仓库 |
| `git clone url` | 克隆远程仓库 |
| `git status` | 查看状态 |
| `git add .` | 暂存所有修改 |
| `git commit -m "说明"` | 提交 |
| `git push` | 推送到远程 |
| `git pull` | 拉取远程更新 |
| `git log --oneline` | 查看简洁历史 |
| `git diff` | 查看修改内容 |
| `git restore 文件名` | 撤销工作区修改 |

## 五、分支管理

分支是 Git 最强大的功能。开发新功能、修 bug 都在分支上做，不影响主分支。

### 5.1 分支基本操作

```bash
# 查看分支
git branch                 # 本地分支
git branch -a              # 所有分支（含远程）

# 创建分支
git branch 分支名

# 切换分支
git checkout 分支名
# 或
git switch 分支名

# 创建并切换（最常用）
git checkout -b 分支名
# 或
git switch -c 分支名

# 删除分支
git branch -d 分支名       # 安全删除（已合并才能删）
git branch -D 分支名       # 强制删除
```

### 5.2 合并分支

```bash
# 先切回主分支
git checkout master

# 合并 feature 分支到 master
git merge feature
```

### 5.3 解决冲突

当两个分支修改了**同一个文件的同一处**，合并时会冲突：

```
<<<<<<< HEAD
这是 master 分支的内容
=======
这是 feature 分支的内容
>>>>>>> feature
```

解决步骤：
1. 打开冲突文件，手动选择保留哪部分（删掉 `<<<<`、`====`、`>>>>` 标记）
2. `git add .`
3. `git commit -m "解决冲突"`

## 六、远程仓库

### 6.1 关联远程仓库

```bash
# 添加远程仓库
git remote add origin https://github.com/你的用户名/项目名.git

# 查看远程仓库
git remote -v

# 第一次推送（-u 设置上游分支，以后直接 git push 就行）
git push -u origin master
```

### 6.2 拉取与推送

```bash
# 拉取远程更新并合并
git pull

# 推送
git push
```

**好习惯**：每次开始写代码前先 `git pull`，避免冲突。

## 七、团队协作流程

企业中最常见的 Git 工作流：

```
master（主分支）— 只放稳定代码，不直接改
  ├── develop（开发分支）— 日常开发在这
  │     ├── feature/login（功能分支）— 做登录功能
  │     ├── feature/order（功能分支）— 做订单功能
  │     └── ...
  └── hotfix/xxx（紧急修复分支）— 生产环境 bug
```

流程：
1. 从 `develop` 拉一个 `feature/xxx` 分支
2. 在 feature 分支上开发
3. 开发完提交 PR（Pull Request），代码审查通过后合并到 `develop`
4. `develop` 测试通过后合并到 `master` 发布

## 八、.gitignore 文件

告诉 Git 哪些文件不需要跟踪：

```gitignore
# 编译输出
target/
build/

# IDE 配置
.idea/
*.iml
.vscode/

# 系统文件
.DS_Store
Thumbs.db

# 环境变量
.env

# 日志
*.log
```

**注意**：已经被 Git 跟踪的文件，加到 `.gitignore` 不会生效。需要先 `git rm --cached 文件名` 取消跟踪。

## 九、IDEA 中使用 Git

- **提交**：Ctrl + K（弹出提交窗口）
- **推送**：Ctrl + Shift + K
- **拉取**：右上角蓝色箭头，或 VCS → Git → Pull
- **查看历史**：Git 面板（左下角 Git 标签）
- **解决冲突**：IDEA 自带可视化冲突解决工具，比命令行方便很多

## 练习

### 练习 1：Git 基本操作

在你的 `study-java` 项目中（已经是 Git 仓库了），练习以下操作：

1. `git status` — 看看当前有哪些修改
2. `git add .` — 暂存所有修改
3. `git commit -m "完成 Maven 学习"` — 提交
4. `git log --oneline -5` — 查看最近 5 条提交记录

### 练习 2：分支操作

1. 创建一个分支 `feature/test` 并切换过去
2. 在 `maven-demo` 的 `App.java` 里加一行打印 `"来自 feature/test 分支"`
3. 提交这个修改
4. 切回 `master` 分支，看看 `App.java` 有没有刚才的修改（应该没有）
5. 合并 `feature/test` 到 `master`
6. 删除 `feature/test` 分支

### 练习 3：思考题

1. `git add` 和 `git commit` 的区别是什么？
2. 为什么要用分支开发，直接在 master 上写不行吗？
3. 两个人同时改了同一个文件会怎样？怎么解决？
