package br.com.flaviohblima.rinhabackend3.queue_service;

import jakarta.annotation.PreDestroy;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisQueueService implements QueueService {

    private static final Logger log = LoggerFactory.getLogger(RedisQueueService.class);

    private final RBlockingQueue<String> paymentQueue;
    private final RDelayedQueue<String> delayedQueue;

    @Autowired
    public RedisQueueService(RedissonClient redissonClient) {
        this.paymentQueue = redissonClient.getBlockingQueue("payments");
        this.delayedQueue = redissonClient.getDelayedQueue(paymentQueue);
    }

    @Override
    public void enqueue(String task) {
        paymentQueue.add(task);
    }

    @Override
    public void enqueueForRetry(String task) {
        delayedQueue.offer(task, 5, TimeUnit.SECONDS);
    }

    @Override
    public String dequeue() {
        try {
            return paymentQueue.take();
        } catch (InterruptedException e) {
            log.error("Queue awaiting for the next payment was interrupted!");
            return null;
        }
    }

    @PreDestroy
    public void cleanUp() {
        delayedQueue.destroy();
    }
}
