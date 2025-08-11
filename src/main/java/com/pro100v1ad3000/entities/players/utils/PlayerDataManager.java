package main.java.com.pro100v1ad3000.entities.players.utils;

public class PlayerDataManager {

    private String nickname;

    private String headColor;
    private String bodyColor;
    private String handColor;

    public PlayerDataManager() {
        nickname = "unknown";

        headColor = "beige";
        bodyColor = "beige";
        handColor = "beige";
    }

    public PlayerDataManager(String nickname) {
        this.nickname = nickname;

        headColor = "beige";
        bodyColor = "beige";
        handColor = "beige";
    }

    public PlayerDataManager(String nickname, String headColor, String bodyColor, String handColor) {
        this.nickname = nickname;

        this.headColor = headColor;
        this.bodyColor = bodyColor;
        this.handColor = handColor;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHeadColor(String headColor) {
        this.headColor = headColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public void setHandColor(String handColor) {
        this.handColor = handColor;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadColor() {
        return headColor;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public String getHandColor() {
        return handColor;
    }
}
