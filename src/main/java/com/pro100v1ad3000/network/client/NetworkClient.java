package main.java.com.pro100v1ad3000.network.client;

import main.java.com.pro100v1ad3000.network.NetworkUtils;
import main.java.com.pro100v1ad3000.network.packets.ServerShutdownPacket;
import main.java.com.pro100v1ad3000.network.server.NetworkServer;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NetworkClient {

    private final String host;
    private final int port;
    private final int maxReconnectAttempts;
    private final long reconnectDelayMs;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Thread listenerThread;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private final Consumer<Object> packetHandler;
    private int reconnectAttempts;
    private long lastPing = -1;

    public NetworkClient(String host, int port, int maxReconnectAttempts,
                         long reconnectDelayMs, Consumer<Object> packetHandler) {
        this.host = host;
        this.port = port;
        this.maxReconnectAttempts = maxReconnectAttempts;
        this.reconnectDelayMs = reconnectDelayMs;
        this.packetHandler = packetHandler;
    }

    public boolean connect() {
        // Устанавливает соединение с сервером, используя заданные параметры хоста и порта.
        // Возвращает true, если соединение успешно установлено, иначе false.
        return connectInternal(false);
    }

    public boolean connectInternal(boolean isReconnect) { // Подключаем клиент к серверу
        // Внутренний метод для установки соединения с сервером.
        // Если isReconnect установлен в true, метод пытается переподключиться с задержкой.
        // Возвращает true, если соединение успешно установлено, иначе false.
        try {
            if(isReconnect) {
                Logger.info("Attempting to reconnect (" + (reconnectAttempts + 1) + "/" + maxReconnectAttempts + ")...");
                Thread.sleep(reconnectDelayMs);
            }

            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            isConnected.set(true);
            reconnectAttempts = 0;

            startListening();
            Logger.info("Connected to server at " + host + ":" + port);

            return true;
        } catch (Exception e) {
            if(isReconnect) {
                reconnectAttempts++;
                if(reconnectAttempts < maxReconnectAttempts) {
                    return connectInternal(true);
                }
                Logger.error("Max reconnect attempts reached");
            } else {
                Logger.error("Initial connection failed: " + e.getMessage());
            }

            return false;
        }
    }

    private void startListening() { // Запускаем поток для обработки входящих данных
        // Создает и запускает новый поток для прослушивания входящих пакетов от сервера.
        // При получении пакета передает его обработчику пакетов.
        listenerThread = new Thread(() -> {
            try {
                while (isConnected.get()) {
                    Object packet = in.readObject();



                    packetHandler.accept(packet);
                }
            } catch (Exception e) {
                if(isConnected.get()) {
                    Logger.error("Connection error: " + e.getMessage());
                }
            }
        });
        listenerThread.start();
    }


    public void sendPacket(Object packet) { // Отправка объекта через сеть
        // Отправляет объект (пакет) через сетевое соединение, если оно активно.
        // В случае ошибки вызывает метод handleDisconnection для обработки разрыва соединения.
        if(!isConnected.get()) return;

        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            Logger.error("Failed to send packet: " + e.getMessage());
        }
    }

    public void disconnect() { // Отключение от сервера
        // Иницирует отключение от сервера, устанавливая флаг isConnected в false и выполняя очистку ресурсов.
        isConnected.set(false);
        cleanup();
    }

    private void cleanup() { // Очистка всех открытых потоков и подключений
        // Выполняет очистку ресурсов: прерывает поток прослушивания и закрывает сокет, а также потоки ввода и вывода.
        try {
            if(listenerThread != null) {
                listenerThread.interrupt();
                listenerThread.join(100);
            }
            if(out != null) out.close();
            if(in != null) in.close();
            if(socket != null) socket.close();
        } catch (IOException e) {
            Logger.error("Error during cleanup: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isConnected() {
        // Возвращает текущее состояние соединения.
        return isConnected.get();
    }

    public long getLastPing() {
        return lastPing;
    }
}
