#!/bin/bash

# SkyWalking Product Service 启动脚本
# 使用方式: ./startup-product-service.sh

# SkyWalking Agent 路径(请根据实际安装路径修改)
SKYWALKING_AGENT_PATH="/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar"

# SkyWalking OAP Server 地址
SKYWALKING_BACKEND="127.0.0.1:11800"

# 服务名称
SERVICE_NAME="product-service"

# JVM 参数
JVM_OPTS="-Xms512m -Xmx512m"

# SkyWalking Agent 参数
SKYWALKING_OPTS="-javaagent:${SKYWALKING_AGENT_PATH} \
  -Dskywalking.agent.service_name=${SERVICE_NAME} \
  -Dskywalking.collector.backend_service=${SKYWALKING_BACKEND}"

# 启动服务
java ${JVM_OPTS} ${SKYWALKING_OPTS} -jar target/product-service.jar
