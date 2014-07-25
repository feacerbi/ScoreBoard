package br.com.felipeacerbi.scoreboard.models;

/**
 * Created by Felipe on 19/07/2014.
 */
public class Competitor {

    private long gameId;
    private long playerId;

    public Competitor() {
    }

    public Competitor(long gameId, long playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
