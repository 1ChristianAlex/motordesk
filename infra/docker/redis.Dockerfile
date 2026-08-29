FROM redis:8-alpine

#ENV REDIS_HOST=redis
#ENV REDIS_PORT=6379
#ENV REDIS_PASSWORD=motordesk_pass
#ENV REDIS_USER=motordesk_pass

EXPOSE 6379

VOLUME ["/data"]

CMD ["sh", "-c", "redis-server --appendonly yes --requirepass \"$REDIS_PASSWORD\" --user \"$REDIS_USER\" on \">${REDIS_PASSWORD}\" ~* +@all"]
