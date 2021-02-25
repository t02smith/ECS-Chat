package uk.ac.soton.comp1206;

import java.util.ArrayList;
import java.util.Iterator;

public class User {
    final private String name;

    private ArrayList<String[]> messages;
    private long lastActive;

    private int tileGameScore = 0;

    public User(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message, String time) {
        String[] msg = {message, time};
        this.messages.add(msg);
        this.lastActive = System.currentTimeMillis();
    }

    public String getName() {
        return this.name;
    }

    public Iterator<String[]> getMessages() {
        return this.messages.iterator();
    }

    public long getLastActive() {
        return this.lastActive;
    }

    public int getTileScore() {
        return this.tileGameScore;
    }

    public void setTileScore(int newScore) {
        this.tileGameScore = newScore;
    }
}
