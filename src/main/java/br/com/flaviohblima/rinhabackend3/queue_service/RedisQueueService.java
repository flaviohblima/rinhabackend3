package br.com.flaviohblima.rinhabackend3.queue_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisQueueService {

    private static final Logger log = LoggerFactory.getLogger(RedisQueueService.class);

    private final StringRedisTemplate redisTemplate;
    private static final String PAYMENT_QUEUE = "payments";
    private static final String PAYMENT_RETRY_QUEUE = "payments-retry";

    @Autowired
    public RedisQueueService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void enqueue(String correlationId) {
        log.debug("Enqueue: {}", correlationId);
        redisTemplate.opsForList().rightPush(PAYMENT_QUEUE, correlationId);
    }

    public String dequeue() {
        return redisTemplate.opsForList().leftPop(PAYMENT_QUEUE);
    }

    public void enqueueForRetry(String correlationId) {
        long retryTime = System.currentTimeMillis() + 5 * 1000;
        redisTemplate.opsForZSet().add(PAYMENT_RETRY_QUEUE, correlationId, retryTime);
    }

    @Scheduled(fixedRate = 1000)
    public void processRetryQueue() {
        long now = System.currentTimeMillis();

        Set<String> correlationIds = redisTemplate.opsForZSet()
                .rangeByScore(PAYMENT_RETRY_QUEUE, 0, now);

        if (correlationIds != null && !correlationIds.isEmpty()) {
            redisTemplate.opsForZSet().remove(PAYMENT_RETRY_QUEUE, correlationIds.toArray());
            correlationIds.forEach(this::enqueue);
        }
    }

}
