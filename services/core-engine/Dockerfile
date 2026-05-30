FROM gradle:9.5.1-jdk21-ubi9 AS build

WORKDIR /workspace

COPY . .

RUN gradle :services:core-engine:installDist --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/services/core-engine/build/install/core-engine ./
COPY services/core-engine/healthcheck.sh /app/healthcheck.sh

RUN chmod +x /app/healthcheck.sh

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["/app/bin/core-engine"]
