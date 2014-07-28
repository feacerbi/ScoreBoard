package br.com.felipeacerbi.scoreboard.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe.acerbi on 03/07/2014.
 */
public class Round implements Serializable {

    private long id;
    private List<Score> scores;
    private List<Score> subScores;
    private String scoreTitle;
    private Game game;

    public Round(int gameMode) {
        scores = new ArrayList<Score>();
        subScores = new ArrayList<Score>();

        switch(gameMode) {
            case Game.GAME_MODE_1X1:
                for(int i = 0; i < 2; i++) {
                    scores.add(new Score(Score.SCORE_NORMAL));
                    subScores.add(new Score(Score.SCORE_SUB));
                }
                break;
            case Game.GAME_MODE_2X2:
                for(int i = 0; i < 2; i++) {
                    scores.add(new Score(Score.SCORE_NORMAL));
                    subScores.add(new Score(Score.SCORE_SUB));
                }
                break;
        }
    }

    public Score getScore(int position) {
        return scores.get(position);
    }

    public void setScore(int position, Score score) {
        scores.set(position, score);
    }

    public Score getSubScore(int position) {
        return subScores.get(position);
    }

    public void setSubScore(int position, Score subScore) {
        subScores.set(position, subScore);
    }

    public String getScoreTitle() {
        return scoreTitle;
    }

    public void setScoreTitle(String scoreTitle) {
        this.scoreTitle = scoreTitle;
    }

    public int getScores() { return scores.size(); }

    public int getSubScores() { return subScores.size(); }

    public List<Score> getScoresList() { return scores; }

    public List<Score> getSubScoresList() { return subScores; }

    public void setScores(List<Score> scores) { this.scores = scores; }

    public void setSubScores(List<Score> subScores) { this.subScores = subScores; }

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
