package main.java.com.pro100v1ad3000.network;

import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkUtils {

    private static final int PING_ATTEMPTS = 3;
    private static final int PING_TIMEOUT_MS = 1000;

    public static long measurePing(String host, int port) {
        long totalTime = 0;
        int successfulAttempts = 0;

        for(int i = 0; i < PING_ATTEMPTS; i++) {
            try (Socket socket = new Socket()) {
                long startTme = System.currentTimeMillis();
                socket.connect(new InetSocketAddress(host, port), PING_TIMEOUT_MS);
                long endTime = System.currentTimeMillis();
                totalTime += (endTime - startTme);
                successfulAttempts++;
            } catch (IOException e) {
                Logger.warn("Ping attempt failed: " + e.getMessage());
            }
        }

        return successfulAttempts > 0 ? totalTime / successfulAttempts : -1;
    }



}
