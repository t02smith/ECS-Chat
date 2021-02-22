package uk.ac.soton.comp1206;

import java.util.ArrayList;
import java.util.Iterator;

public class User implements Comparable<User> {
    final private String name;
    private ArrayList<String[]> messages;
    private long lastActive;

    public User(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public int compareTo(User user) {
        if (this.getLastActive() >= user.getLastActive()) return -1;
        else return 1;
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
}
