apiVersion: v1
kind: ConfigMap
metadata:
  name: motordesk-config
  namespace: motordesk
data:
  HTTP_HOST: "0.0.0.0"
  HTTP_PORT: "8080"
  HTTP_PROTOCOL: "http"
  DATABASE_DRIVER: "org.postgresql.Driver"
  MONGO_OPTIONS: "${MONGO_OPTIONS}"
  MONGO_URL: "mongo:27017"
  REDIS_PORT: "6379"
  REDIS_TLS: "true"
