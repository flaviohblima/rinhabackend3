package br.com.flaviohblima.rinhabackend3.queue_service;

public interface QueueService {

    void enqueue(String task);

    void enqueueForRetry(String task);

    String dequeue();

}
