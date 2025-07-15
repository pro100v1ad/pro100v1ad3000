package main.java.com.pro100v1ad3000.network.server;

import main.java.com.pro100v1ad3000.network.packets.ServerShutdownPacket;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class NetworkServer {

    private final int port;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private Thread serverThread;
    private boolean isRunning = false;
    private final BiConsumer<ClientHandler, Object> packetHandler;

    public NetworkServer(int port, BiConsumer<ClientHandler, Object> packetHandler) {

        this.port = port;
        this.packetHandler = packetHandler;

    }

    public void start() { // Запускает сервер
        // Запускает сервер на указанном порту в отдельном потоке.
        // Если сервер уже запущен, метод завершает выполнение.
        if(isRunning) return;

        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                isRunning = true;
                Logger.info("Server started on port " + port);

                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    clientHandler.start();
                }

            } catch (IOException e) {
                if(isRunning) {
                    Logger.error("Server error: " + e.getMessage());
                }
            }
        });
        serverThread.start();
    }

    public void stop() { // Останавливает сервер
        // Останавливает сервер, отключая всех клиентов и закрывая серверный сокет.
        isRunning = false;

        broadcast(new ServerShutdownPacket(), null);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        clients.forEach(ClientHandler::disconnect);
        clients.clear();

        try {
            if(serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            Logger.error("Error stopping server: " + e.getMessage());
        }
    }

    public void broadcast(Object packet, ClientHandler exclude) { // рассылает всем клиентам пакеты
        // Рассылает пакет всем подключенным клиентам, кроме указанного в параметре exclude.
        for(ClientHandler client : clients) {
            if(client != exclude) {
                client.sendPacket(packet);
            }
        }

    }

    public class ClientHandler extends Thread { // Сами клиенты

        private final Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private boolean isConnected = false;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() { // для каждого клиента свой поток
            // Обрабатывает соединение с клиентом в отдельном потоке.
            // Читает входящие пакеты и передает их обработчику пакетов.
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                isConnected = true;

                while (isConnected && !Thread.currentThread().isInterrupted()) {
                    try {
                        Object packet = in.readObject();
                        packetHandler.accept(this, packet);
                    } catch (EOFException e) {
                        // Нормальное завершение соединения
                        break;
                    }
                }

            } catch (Exception e) {
                if(isConnected && !Thread.currentThread().isInterrupted()) {
                    Logger.error("Client error: " + e.getMessage());
                }
            } finally {
                disconnect();
            }
        }

        public void sendPacket(Object packet) { // Отправка пакета клиенту
            // Отправляет пакет клиенту, если соединение активно.
            if(!isConnected) return;

            try {
                out.writeObject(packet);
                out.flush();
            } catch (IOException e) {
                Logger.error("Failed to send packet to client: " + e.getMessage());
                disconnect();
            }
        }

        public void disconnect() { // отключение от сервера
            // Отключает клиента, закрывая сокет и удаляя его из списка клиентов
            isConnected = false;
            clients.remove(this);
            try {
                if(socket != null) socket.close();
            } catch (IOException e) {
                Logger.error("Error closing client socket: " + e.getMessage());
            }
        }

    }


}
