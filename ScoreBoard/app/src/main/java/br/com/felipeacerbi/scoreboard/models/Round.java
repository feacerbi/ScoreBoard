package br.com.felipeacerbi.scoreboard.models;

/**
 * Created by felipe.acerbi on 03/07/2014.
 */
public class Round {

    private long id;
    private int score1;
    private int score2;
    private int subScore1;
    private int subScore2;
    private String scoreTitle;
    private Game game;

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getSubScore1() {
        return subScore1;
    }

    public void setSubScore1(int subScore1) {
        this.subScore1 = subScore1;
    }

    public int getSubScore2() {
        return subScore2;
    }

    public void setSubScore2(int subScore2) {
        this.subScore2 = subScore2;
    }

    public String getScoreTitle() {
        return scoreTitle;
    }

    public void setScoreTitle(String scoreTitle) {
        this.scoreTitle = scoreTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
