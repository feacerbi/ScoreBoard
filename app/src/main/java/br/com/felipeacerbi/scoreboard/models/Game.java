package br.com.felipeacerbi.scoreboard.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe.acerbi on 03/07/2014.
 */
public class Game implements Serializable {

    public static final int GAME_MODE_1X1 = 2;
    public static final int GAME_MODE_2X2 = 4;

    private long id;
    private int winScore;
    private int gameMode;
    private boolean finished;
    private List<Score> totalScores;
    private List<Player> players;
    private List<Round> rounds;

    public Game(int gameMode) {

        winScore = 3000;
        finished = false;
        this.gameMode = gameMode;

        rounds = new ArrayList<Round>();
        players = new ArrayList<Player>();
        totalScores = new ArrayList<Score>();

        startPlayers();
        startScores();

    }

    public void refreshTotal() {

        for(int i = 0; i < getTotalScores(); i++) {
            getTotalScore(i).setValue(0);
        }

        for(Round round : rounds) {
            for(int i = 0; i < round.getScores(); i++) {
                getTotalScore(i).setValue(getTotalScore(i).getValue() + round.getScore(i).getValue());
            }
        }

    }

    public void startScores() {

        switch(gameMode) {
            case Game.GAME_MODE_1X1:
                for(int i = 0; i < 2; i++) {
                    totalScores.add(new Score(Score.SCORE_TOTAL));
                }
                break;
            case Game.GAME_MODE_2X2:
                for(int i = 0; i < 2; i++) {
                    totalScores.add(new Score(Score.SCORE_TOTAL));
                }
                break;
        }

    }

    public void startPlayers() {

        for(int i = 0; i < gameMode; i++) {
            Player player = new Player("Player " + (i + 1));
            players.add(i, player);
        }

    }

    public void addRound(Round round) {

        rounds.add(round);
        refreshTotal();

    }

    public void removeRound(Round round) {

        rounds.remove(round);
        refreshTotal();

    }

    public String getFormattedDuration() {
        long time = 0;

        if(getRounds() > 0) {
            time = getRoundsList().get(getRounds() - 1).getTime() - getRoundsList().get(0).getTime();
            time = time/1000;
        }

        long years = time / 31556926;
        time = time % 31556926;
        long weeks = time / 604800;
        time = time % 604800;
        long days = time / 86400;
        time = time % 86400;
        long hours = time / 3600;
        time = time % 3600;
        long minutes = time / 60;
        time = time % 60;
        long seconds = time;

        String formatted = years > 0 ? years + "y " : "" +
                (weeks > 0 ? weeks + "w " : "") +
                (days > 0 ? days + "d " : "") +
                (hours > 0 ? hours + "h " : "") +
                (minutes > 0 ? minutes + "m " : "") +
                (seconds > 0 ? seconds + "s" : "");

        return formatted;
    }

    public long getFinishTime() {
        if(finished && rounds.size() > 0) {
            return rounds.get(rounds.size() - 1).getTime();
        }

        return 0;
    }

    public int getRounds() {
        return rounds.size();
    }

    public int getPlayers() { return players.size(); }

    public int getTotalScores() { return totalScores.size(); }

    public Player getPlayer(int position) {
        return players.get(position);
    }

    public Score getTotalScore(int position) {
        return totalScores.get(position);
    }

    public List<Round> getRoundsList() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<Player> getPlayersList() { return players; }

    public void setPlayers(List<Player> players) { this.players = players; }

    public List<Score> getTotalScoresList() { return totalScores; }

    public void setTotalScores(List<Score> totalScores) { this.totalScores = totalScores; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWinScore() {
        return winScore;
    }

    public void setWinScore(int winScore) {
        this.winScore = winScore;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
