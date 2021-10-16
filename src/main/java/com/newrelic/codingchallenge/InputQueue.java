package com.newrelic.codingchallenge;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileWriter;
import java.io.BufferedWriter;

class InputQueue implements Runnable {
    private final BlockingQueue<Integer> blockingQueue;
    private Timer timer;
    private static final long WAIT_TIME = 10_000;
    private static final String NUMBER_LOG = "numbers.log";
    private static final int MAX_INPUT_VALUE = 999_999_999;
    private Object lock;
    private final boolean[] receivedInput = new boolean[MAX_INPUT_VALUE];
    private int uniqueDiffSinceLastReport = 0;
    private int duplicateDiffSinceLastReport = 0;
    private int uniqueTotal = 0;

    public InputQueue(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new TenSecondReport(), 0, WAIT_TIME);
    }

    @Override
    public void run() {
        try {
            FileWriter fileWriter = new FileWriter(NUMBER_LOG, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            while (true) {
                int currNum = blockingQueue.take();
                synchronized (lock) {
                    if (receivedInput[currNum]) {
                        duplicateDiffSinceLastReport++;
                        continue;
                    }
                    receivedInput[currNum] = true;
                    uniqueDiffSinceLastReport++;
                    uniqueTotal++;
                    try {
                        bufferedWriter.write(currNum + System.lineSeparator());
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class TenSecondReport extends TimerTask {
        @Override
        public void run() {
            lock = new Object();
            synchronized (lock) {
                StringBuilder sb = new StringBuilder();
                sb.append("Received ");
                sb.append(uniqueDiffSinceLastReport);
                sb.append(" unique numbers, ");
                sb.append(duplicateDiffSinceLastReport);
                sb.append(" duplicates. Unique total: ");
                sb.append(uniqueTotal);
                System.out.println(sb.toString());
                uniqueDiffSinceLastReport = 0;
                duplicateDiffSinceLastReport = 0;
            }
        }
    }
}

