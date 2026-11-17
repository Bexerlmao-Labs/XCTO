# ============================================================
# XCTO 前端镜像（docker-compose.yml 中 xcto-frontend 服务使用）
# ============================================================
# 独立构建：docker build -t xcto-frontend:latest ./xcto-ui
# 独立推送：docker tag/push xcto-frontend:latest <registry>/xcto-frontend:latest
# 仅托管 SPA 静态资源（nginx 默认配置），反向代理由外部手动配置：
#   上游地址：http://<前端容器IP>:80 或映射后的 127.0.0.1:8080
#   API 反代到：http://xcto-backend:65535（或映射后的后端端口）
#   注意：需自行处理 history 路由回退（try_files ... /index.html）
# ============================================================

# 阶段一：构建 SPA
FROM node:20-alpine AS build
WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm install

COPY . .
RUN npm run build-only

# 阶段二：nginx 托管静态文件（使用镜像自带默认配置）
FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
