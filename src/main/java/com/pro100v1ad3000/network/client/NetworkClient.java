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
    private final Runnable onDisconnectCallBack;
    private int reconnectAttempts;
    private long lastPing = -1;

    public NetworkClient(String host, int port, int maxReconnectAttempts,
                         long reconnectDelayMs, Consumer<Object> packetHandler, Runnable onDisconnectCallBack) {
        this.host = host;
        this.port = port;
        this.maxReconnectAttempts = maxReconnectAttempts;
        this.reconnectDelayMs = reconnectDelayMs;
        this.packetHandler = packetHandler;
        this.onDisconnectCallBack = onDisconnectCallBack;
    }

    public boolean connect() { // Подключаем клиент к серверу
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
            updatePing();
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

                    if(packet instanceof ServerShutdownPacket) {

                        Logger.info("Server is shutting down. Disconnection...");
                        handleDisconnection(true);
                        return;

                    }

                    packetHandler.accept(packet);
                }
            } catch (Exception e) {
                if(isConnected.get()) {
                    Logger.error("Connection error: " + e.getMessage());
                    handleDisconnection(false);
                }
            }
        });
        listenerThread.start();
    }

    private void handleDisconnection(boolean isServerShutdown) { // Потеря соединения, с попыткой переподключения
        // Обрабатывает разрыв соединения: устанавливает флаг isConnected в false,
        // выполняет очистку ресурсов и вызывает callback-функцию отключения.
        // Запускает попытку переподключения в фоновом потоке.
        isConnected.set(false);
        cleanup();
        onDisconnectCallBack.run();

        // Попытка переподключения только если сервер не выключился намеренно
        if(!isServerShutdown) {
            new Thread(() -> {
                if (connectInternal(true)) {
                    Logger.info("Reconnected successfully");
                } else {
                    Logger.error("Failed to reconnect");
                }
            }).start();
        }
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
            handleDisconnection(false);
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

    private void updatePing() {
        new Thread(() -> {
           while (isConnected.get()) {
               lastPing = NetworkUtils.measurePing(host, port);
               try {
                   Thread.sleep(5000);
               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
               }
           }
        }).start();
    }

    public boolean isConnected() {
        // Возвращает текущее состояние соединения.
        return isConnected.get();
    }

    public long getLastPing() {
        return lastPing;
    }
}
