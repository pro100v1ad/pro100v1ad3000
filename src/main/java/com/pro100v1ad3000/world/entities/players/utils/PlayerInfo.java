package main.java.com.pro100v1ad3000.world.entities.players.utils;

import java.util.Random;

public class PlayerInfo {
    private String nickname;
    private int id;

    public PlayerInfo() {
        this.nickname = "unknown";
        this.id = generateRandomId();
    }

    public PlayerInfo(String nickname) {
        this.nickname = nickname;
        this.id = generateRandomId();
    }

    public PlayerInfo(String nickname, int id) {
        this.nickname = nickname;
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    // Генерация случайного ID из 10 цифр
    public static int generateRandomId() {
        Random random = new Random();
        return 1000000000 + random.nextInt(900000000); // 10-значное число
    }
}
