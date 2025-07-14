package main.java.com.pro100v1ad3000.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Logger {

    private static final ConcurrentLinkedQueue<LogEntry> logQueue = new ConcurrentLinkedQueue<>();
    private static  final AtomicBoolean running = new AtomicBoolean(false);
    private static Thread loggerThread;
    private static PrintWriter fileWriter;
    private static LogLevel minLogLevel = LogLevel.INFO;

    // Уровни логирования
    public enum LogLevel {
        ERROR, WARN, INFO, DEBUG
    }

    // Внутренний класс для хранения информации о логах
    private static class LogEntry {
        final String message;
        final LogLevel level;
        final long timestamp;
        final String threadName;

        LogEntry(String message, LogLevel level) {
            this.message = message;
            this.level = level;
            this.timestamp = System.currentTimeMillis();
            this.threadName = Thread.currentThread().getName();
        }
    }

    // Инициализация логгера с заданным уровнем логирования и путем к файлу лога
    public static synchronized void initialize(LogLevel level, String logFilePath) {
        minLogLevel = level;

        try {
            if(logFilePath != null) {
                fileWriter = new PrintWriter(new FileWriter(logFilePath, false), true);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize file logger: " + e.getMessage());
        }

        if(running.compareAndSet(false, true)) {
            loggerThread = new Thread(Logger::processLogs, "Logger-Thread");
            loggerThread.setDaemon(true);
            loggerThread.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                running.set(false);
                loggerThread.interrupt();
                flush();
            }));
        }

    }

    // Остановка логгера и освобождение ресурсов
    public static synchronized void shutdown() {
        running.set(false);
        if(loggerThread != null) {
            loggerThread.interrupt();
        }
        flush();
    }

    // Основной цикл обработки логов, который обрабатывает записи из очереди
    private static void processLogs() {
        while (running.get()) {
            try {
                LogEntry entry;
                while ((entry = logQueue.poll()) != null) {
                    writeLog(entry);
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Запись оставшихся сообщений перед завершением работы
        flush();

    }

    // Запись лога в консоль и файл
    private static void writeLog(LogEntry entry) {
        String formatted = formatLogEntry(entry);

        // Вывод в консоль
        if(entry.level == LogLevel.ERROR) {
            System.err.println(formatted);
        } else {
            System.out.println(formatted);
        }

        // Запись в файл
        if (fileWriter != null) {
            fileWriter.println(formatted);
        }

    }

    //Форматирование записи лога в строку
    private static String formatLogEntry(LogEntry entry) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return String.format("[%s] [%s] [%s] %s: %s",
                sdf.format(new Date(entry.timestamp)),
                entry.threadName,
                entry.level,
                entry.level.name(),
                entry.message);
    }

    // Сброс буфера: запись всех оставшихся сообщений в лог
    public static void flush() {
        LogEntry entry;
        while((entry = logQueue.poll()) != null) {
            writeLog(entry);
        }

        if(fileWriter != null) {
            fileWriter.flush();
        }
    }

    // Методы для логирования сообщений с различными уровнями важности
    public static void error(String message) {
        log(message, LogLevel.ERROR);
    }

    public static void error(String message, Throwable throwable) {
        log(message + "\n" + getStackTrace(throwable), LogLevel.ERROR);
    }

    public static void info(String message) {
        log(message, LogLevel.INFO);
    }

    public static void warn(String message) {
        log(message, LogLevel.WARN);
    }

    public static void debug(String message) {
        log(message, LogLevel.DEBUG);
    }

    // Добавление записи в очередь логов, если уровень логирования достаточен
    private static void log(String message, LogLevel level) {
        if(level.ordinal() <= minLogLevel.ordinal()) {
            logQueue.offer(new LogEntry(message, level));
        }
    }

    // Получение трассировки стека исключения в виде строки
    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
