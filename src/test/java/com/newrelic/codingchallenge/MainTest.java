package com.newrelic.codingchallenge;

import org.junit.Test;

public class MainTest {
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final int PORT = 4000;

    @Test
    public void testTermination() {
        Client client = new Client();
        client.connect(LOCAL_HOST, PORT);
        client.sendInput("000000000");
        client.sendInput("terminate");
    }

    @Test
    public void testNegativeNumber() {
        Client client = new Client();
        client.connect(LOCAL_HOST, PORT);
        client.sendInput("000000000");
        client.sendInput("-11111111");
    }

    @Test
    public void testNumberWithLetters() {
        Client client = new Client();
        client.connect(LOCAL_HOST, PORT);
        client.sendInput("000000000");
        client.sendInput("1111a1111");
    }

    @Test
    public void testDuplicates() {
        Client client = new Client();
        client.connect(LOCAL_HOST, PORT);
        client.sendInput("000000000");
        client.sendInput("000000000");
    }

    @Test
    public void handleFiveClients() {
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        Client client4 = new Client();
        Client client5 = new Client();

        client1.connect(LOCAL_HOST, PORT);
        client2.connect(LOCAL_HOST, PORT);
        client3.connect(LOCAL_HOST, PORT);
        client4.connect(LOCAL_HOST, PORT);
        client5.connect(LOCAL_HOST, PORT);

        int min = 0, max = 999_999_999;
        for (int i = 0; i <= 400_000; i++) {
            int rand1 = (int) (Math.random() * (max - min) + min);
            int rand2 = (int) (Math.random() * (max - min) + min);
            int rand3 = (int) (Math.random() * (max - min) + min);
            int rand4 = (int) (Math.random() * (max - min) + min);
            int rand5 = (int) (Math.random() * (max - min) + min);

            client1.sendInput(String.format("%09d", rand1));
            client2.sendInput(String.format("%09d", rand2));
            client3.sendInput(String.format("%09d", rand3));
            client4.sendInput(String.format("%09d", rand4));
            client5.sendInput(String.format("%09d", rand5));
        }
    }
}
