package com.newrelic.codingchallenge;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int MAX_CLIENTS = 5;
    private final ExecutorService executorServiceServer = Executors.newFixedThreadPool(MAX_CLIENTS);
    private final BlockingQueue<Integer> blockingQueueServer;
    private ServerSocket socketServer;

    public Server(BlockingQueue<Integer> blockingQueueServer) {
        this.blockingQueueServer = blockingQueueServer;
    }

    public void start(int port) {
        try {
            socketServer = new ServerSocket(port);
            InputQueue inputQueue = new InputQueue(blockingQueueServer);
            new Thread(inputQueue).start();
            while (true) {
                executorServiceServer.submit(
                    new ClientHandler(socketServer.accept(), blockingQueueServer, executorServiceServer)
                );
            }
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            try {
                socketServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final BlockingQueue<Integer> blockingQueueClient;
        private final Socket socketClient;
        private final ExecutorService executorServiceClient;
        private BufferedReader in;

        public ClientHandler(Socket socketClient, BlockingQueue<Integer> blockingQueueClient, ExecutorService executorServiceClient) {
            this.socketClient = socketClient;
            this.blockingQueueClient = blockingQueueClient;
            this.executorServiceClient = executorServiceClient;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                String currLine = "";
                while (currLine != null) {
                    try {
                        currLine = in.readLine();
                    } catch (IOException error) {
                        error.printStackTrace();
                        break;
                    }
                    if (currLine.equals("terminate")) {
                        executorServiceClient.shutdown();
                        System.exit(0);
                    }
                    if (isValidInput(currLine)) {
                        blockingQueueClient.add(Integer.valueOf(currLine));
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    socketClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean isValidInput(String input)
                throws NumberFormatException {
            if (input.length() != 9) {
                throw new NumberFormatException("Input " + input + " is not exactly nine digits long");
            }
            for (int i = 0; i < 9; i++) {
                if (i == 0 && input.charAt(i) == '-') {
                    throw new NumberFormatException("Input " + input + " contains negative sign as first character");
                }
                if (!Character.isDigit(input.charAt(i))) {
                    throw new NumberFormatException("Input " + input + " does not contain only decimal digits");
                }
            }
            return true;
        }
    }
}