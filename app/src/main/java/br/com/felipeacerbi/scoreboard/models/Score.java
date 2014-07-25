package br.com.felipeacerbi.scoreboard.models;

import java.io.Serializable;

/**
 * Created by felipe.acerbi on 18/07/2014.
 */
public class Score implements Serializable {

    public static final int SCORE_NORMAL = 0;
    public static final int SCORE_TOTAL = 1;
    public static final int SCORE_SUB = 2;

    private long gameId;
    private long roundId;
    private int type;
    private int value;

    public Score(int type) {
        gameId = 0;
        roundId = 0;
        value = 0;
        this.type = type;
    }

    public Score(int type, int value) {
        gameId = 0;
        roundId = 0;
        this.value = value;
        this.type = type;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    public int getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Score setValue(int value) {
        this.value = value;
        return this;
    }
}
