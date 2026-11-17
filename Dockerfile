# ============================================================
# XCTO 后端镜像（docker-compose.yml 中 xcto-backend 服务使用）
# ============================================================
# 使用前请先在本地执行构建：
#   Windows: build.bat  (产出 target/XCTO.jar)
#   然后:   docker build -t xcto-backend:latest .
# 前端镜像见 xcto-ui/Dockerfile，两个镜像独立构建、独立推送。
# ============================================================

FROM eclipse-temurin:17-jre
WORKDIR /app

# 直接复制本地已构建好的 fat jar（内含前端静态文件，但正式入口走 xcto-frontend 的 nginx）
COPY target/XCTO.jar app.jar

EXPOSE 65535

ENTRYPOINT ["java", "-jar", "app.jar"]
