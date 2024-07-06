# 鱿鱼杀后端
## 快速部署
1. 修改配置文件 `sk_backend-app/src/main/resources/application-prod.yml`
2. mvn打包
```shell
mvn clean install # 程序根目录下运行
```
2. 安装环境
```shell
cd docs/dev-ops
chmod +x ./docker-compose-environment.yml
docker-compose -f docker-compose-environment.yml up -d
```
3. 构建镜像
```shell
cd sk_backernd-app
chmod +x ./build.sh
./build.sh
```
4. 运行程序
```shell
cd docs/dev-ops
chmod +x ./docker-compose-app.yml
docker-compose -f docker-compose-app.yml up -d
```
