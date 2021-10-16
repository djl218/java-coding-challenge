package com.newrelic.codingchallenge;

import java.io.InputStreamReader;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

public class Client {
    private Socket socketClient;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String ip, int port) {
        try {
            socketClient = new Socket(ip, port);
            out = new PrintWriter(socketClient.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader((socketClient.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInput(String input) {
        try {
            out.println(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            out.close();
            in.close();
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
