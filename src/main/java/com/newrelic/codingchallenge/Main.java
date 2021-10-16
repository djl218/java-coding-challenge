package com.newrelic.codingchallenge;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int QUEUE_LENGTH = 2_000_000;
    private static final int PORT = 4000;

    public static void main(String[] args) {
        System.out.println("Starting up server ....");
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(QUEUE_LENGTH);
        Server server = new Server(blockingQueue);
        server.start(PORT);
    }
}