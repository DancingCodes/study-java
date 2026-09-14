# CI/CD 基础

## 一、什么是 CI/CD

### 1.1 没有 CI/CD 的痛苦

每次发版：
```
1. 本地改代码
2. 手动跑测试（经常忘）
3. mvn package 打包
4. scp 上传到服务器
5. ssh 登录服务器
6. kill 旧进程
7. 启动新 jar
8. 祈祷不出问题...
```

### 1.2 有了 CI/CD

```
git push → 自动构建 → 自动测试 → 自动部署
```

| 概念 | 全称 | 说明 |
|------|------|------|
| **CI** | Continuous Integration | 持续集成：代码推送后自动构建、自动测试 |
| **CD** | Continuous Delivery/Deployment | 持续交付/部署：测试通过后自动部署到服务器 |

## 二、GitHub Actions

GitHub 自带的 CI/CD 工具，免费额度够用，不需要额外安装。

### 2.1 基本概念

| 概念 | 说明 |
|------|------|
| **Workflow** | 一个自动化流程（`.yml` 文件） |
| **Event** | 触发 Workflow 的事件（push、PR、定时等） |
| **Job** | 一个任务（构建、测试、部署等） |
| **Step** | 一个步骤（一条命令或一个 Action） |
| **Runner** | 执行 Job 的服务器（GitHub 提供免费的） |

### 2.2 文件位置

```
项目根目录/
└── .github/
    └── workflows/
        └── ci.yml    ← Workflow 配置文件
```

## 三、Java 项目 CI 配置

### 3.1 基础 CI：构建 + 测试

```yaml
name: Java CI

on:
  push:
    branches: [main, master]
  pull_request:
    branches: [main, master]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      # 1. 拉取代码
      - uses: actions/checkout@v4

      # 2. 安装 JDK
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven

      # 3. 构建 + 测试
      - name: Build and Test
        run: mvn clean verify

      # 4. 上传构建产物（可选）
      - name: Upload artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: target/*.jar
```

推送代码后，在 GitHub 仓库的 `Actions` 标签页可以看到构建过程。

### 3.2 CI + Docker 构建

```yaml
name: Docker CI

on:
  push:
    branches: [main]

jobs:
  build-and-push:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and push Docker image
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: ${{ secrets.DOCKER_USERNAME }}/myapp:${{ github.sha }}
```

`secrets.DOCKER_USERNAME` 等需要在 GitHub 仓库的 Settings → Secrets 中配置。

## 四、CD：自动部署

### 4.1 部署到自己的服务器

在 CI 成功后，SSH 到服务器执行部署：

```yaml
  deploy:
    needs: build-and-push   # 依赖构建 Job 成功
    runs-on: ubuntu-latest

    steps:
      - name: Deploy to server
        uses: appleboy/ssh-action@v1
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SERVER_SSH_KEY }}
          script: |
            docker pull yourusername/myapp:${{ github.sha }}
            docker stop myapp || true
            docker rm myapp || true
            docker run -d --name myapp -p 8080:8080 yourusername/myapp:${{ github.sha }}
```

### 4.2 完整流程

```
开发者 git push
    ↓
GitHub Actions 触发
    ↓
CI：编译 → 测试 → 构建 Docker 镜像 → 推送到 Docker Hub
    ↓
CD：SSH 到服务器 → 拉取镜像 → 重启容器
    ↓
部署完成！
```

## 五、Jenkins（了解）

Jenkins 是另一个主流 CI/CD 工具：

| 对比 | GitHub Actions | Jenkins |
|------|---------------|--------|
| 部署方式 | GitHub 云端 | 需要自己安装 |
| 配置方式 | YAML 文件 | Web 界面 + Jenkinsfile |
| 费用 | 免费额度够用 | 免费开源，但需要服务器 |
| 生态 | GitHub Marketplace | 海量插件 |
| 适用 | 中小项目，GitHub 仓库 | 大公司，任意代码仓库 |

中小项目推荐 GitHub Actions，大公司常用 Jenkins。

## 六、CI/CD 最佳实践

1. **每次提交都跑 CI**：尽早发现问题
2. **测试必须通过才能合并**：设置 Branch Protection
3. **构建产物有版本号**：用 git SHA 或 tag 标记镜像
4. **敏感信息用 Secrets**：密码、密钥不要写在代码里
5. **分环境部署**：dev → staging → prod，逐步验证

## 练习

### 练习 1：GitHub Actions CI

1. 把项目推送到 GitHub
2. 创建 `.github/workflows/ci.yml`
3. 配置 push 时自动构建和测试
4. 在 Actions 标签页查看构建结果

### 练习 2：Docker 镜像构建

1. 在 CI 中增加 Docker 镜像构建步骤
2. 推送镜像到 Docker Hub
3. 在服务器上拉取镜像并运行

### 练习 3：思考题

1. CI 和 CD 分别解决了什么问题？
2. 为什么敏感信息要用 Secrets 而不是直接写在 yml 文件里？
3. 生产环境为什么不建议用 `latest` 标签部署？
