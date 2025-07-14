package main.java.com.pro100v1ad3000.entities.players;

public abstract class Player {

    protected int id;
    protected float x, y;

    public Player(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public abstract void update(float deltaTime);

    public int getId() { return id; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
