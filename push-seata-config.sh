#!/bin/bash

echo "正在向 Nacos 推送 Seata 配置..."

curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs" \
  -d "dataId=seataServer.properties" \
  -d "group=SEATA_GROUP" \
  -d 'content=service.vgroupMapping.order-service-group=default
service.vgroupMapping.product-service-group=default
service.vgroupMapping.user-service-group=default
store.mode=file
store.file.dir=file_store/data
store.file.max-branch-session-size=16384
store.file.max-global-session-size=512
store.file.file-write-buffer-cache-size=16384
store.file.flush-disk-mode=async
store.file.session-reload-read-size=100
server.recovery.committing-retry-period=1000
server.recovery.asyn-committing-retry-period=1000
server.recovery.rollbacking-retry-period=1000
server.recovery.timeout-retry-period=1000
server.max-commit-retry-timeout=-1
server.max-rollback-retry-timeout=-1
server.rollback-retry-timeout-enable=false
server.distributed-lock-expire-time=10000
server.undo.log-save-days=7
server.undo.log-delete-period=86400000
client.undo.log-table=undo_log
client.undo.log-serialization=jackson
client.undo-only-care-update-columns=true
client.rm.lock.retry-interval=10
client.rm.lock.retry-times=30
client.rm.lock.retry-policy-branch-rollback-on-conflict=true
client.rm.report-retry-count=5
client.rm.table-meta-check-enable=false
client.rm.table-meta-checker-interval=60000
client.rm.sql-parser-type=druid
client.rm.report-success-enable=true
client.rm.saga-branch-register-enable=false
client.tm.commit-retry-count=5
client.tm.rollback-retry-count=5
client.tm.degrade-check=false
client.tm.degrade-check-period=2000
client.tm.degrade-check-allow-times=10
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enable-client-batch-send-request=true
transport.shutdown.wait=3
transport.thread-factory.boss-thread-prefix=NettyBoss
transport.thread-factory.worker-thread-prefix=NettyServerNIOWorker
transport.thread-factory.server-executor-thread-prefix=NettyServerBizHandler
transport.thread-factory.share-boss-worker=false
transport.thread-factory.client-selector-thread-prefix=NettyClientSelector
transport.thread-factory.client-selector-thread-size=1
transport.thread-factory.client-worker-thread-prefix=NettyClientWorkerThread
transport.thread-factory.boss-thread-size=1
transport.thread-factory.worker-thread-size=default
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registry-type=compact
metrics.exporter-list=prometheus
metrics.exporter-prometheus-port=9898'

echo ""
echo "Seata 配置已推送到 Nacos"
