package br.com.felipeacerbi.scoreboard.models;

import java.io.Serializable;

/**
 * Created by Felipe on 05/07/2014.
 */
public class Player implements Serializable {

    private long id;
    private String name;
    private String photoPath;
    private int score;

    public Player() {
        setScore(0);
    }

    public Player(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return getName();
    }
}
