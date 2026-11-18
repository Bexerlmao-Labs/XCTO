# ============================================================
# XCTO 后端镜像（docker-compose.yml 中 xcto-backend 服务使用）
# ============================================================
# 使用分层 jar：先在构建阶段按层解包（dependencies / spring-boot-loader /
# snapshot-dependencies / application），再分四个 COPY 写入运行镜像。
# 依赖层几乎不变，配合镜像仓库按层去重，CI 每次只需跨境推送很小的
# application 层，解决推送阶段长时间卡顿的问题。
#
# 前端镜像见 xcto-ui/Dockerfile，两个镜像独立构建、独立推送。
# ============================================================

# 基础镜像使用自己 TCR 里的副本（由 ECS 上 docker pull+push 同步，见下）。
# 这样基础层永远在自家 registry 中去重命中，避免 Docker Hub tag 更新后
# 每次 CI 都要跨境重推 ~110MB 基础层，导致推送阶段长时间卡顿。
# 升级 JDK 时：ECS 上重新 pull+push 一次即可。
FROM ccr.ccs.tencentyun.com/xcto/eclipse-temurin:17-jre AS extractor
WORKDIR /app
COPY target/XCTO.jar app.jar
# Spring Boot 3.3+/4.x 的 tools 模式，--layers 按层输出到子目录
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# 基础镜像同理，使用 TCR 内的副本
FROM ccr.ccs.tencentyun.com/xcto/eclipse-temurin:17-jre
WORKDIR /app
# COPY 顺序即层叠加顺序：最稳定的层放最前面
COPY --from=extractor /app/extracted/dependencies/ ./
COPY --from=extractor /app/extracted/spring-boot-loader/ ./
COPY --from=extractor /app/extracted/snapshot-dependencies/ ./
COPY --from=extractor /app/extracted/application/ ./

EXPOSE 65535

ENTRYPOINT ["java", "-jar", "app.jar"]
