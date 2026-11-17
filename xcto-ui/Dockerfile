# ============================================================
# XCTO 前端镜像（docker-compose.yml 中 xcto-frontend 服务使用）
# ============================================================
# 独立构建：docker build -t xcto-frontend:latest ./xcto-ui
# 独立推送：docker tag/push xcto-frontend:latest <registry>/xcto-frontend:latest
# 内含 nginx：托管 SPA 静态资源，并把 API 反向代理到 xcto-backend。
# ============================================================

# 阶段一：构建 SPA
FROM node:20-alpine AS build
WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm install

COPY . .
RUN npm run build-only

# 阶段二：nginx 托管
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
