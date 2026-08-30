FROM redis:8-alpine

#ENV REDIS_HOST=${REDIS_HOST} \
#    REDIS_PORT=${REDIS_PORT} \
#    REDIS_PASSWORD=${REDIS_PASSWORD} \
#    REDIS_USER=${REDIS_USER}

EXPOSE 6379

VOLUME ["/data"]

CMD ["sh", "-c", "redis-server --appendonly yes --requirepass \"$REDIS_PASSWORD\" --user \"$REDIS_USER\" on \">${REDIS_PASSWORD}\" ~* +@all"]
