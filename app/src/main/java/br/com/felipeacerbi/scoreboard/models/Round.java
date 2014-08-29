package br.com.felipeacerbi.scoreboard.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by felipe.acerbi on 03/07/2014.
 */
public class Round implements Serializable {

    private long id;
    private List<Score> scores;
    private List<Score> subScores;
    private String scoreTitle;
    private Game game;
    private long time;

    public Round(int gameMode) {

        time = Calendar.getInstance().getTimeInMillis();

        startScores(gameMode);

    }

    public void startScores(int gameMode) {

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

    public String getFormattedTime() {

        String formatted = "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTime());

        Calendar today = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int ampm = calendar.get(Calendar.AM_PM);
        if(day == today.get(Calendar.DAY_OF_MONTH) && month == today.get(Calendar.MONTH)) {
            formatted = (hour < 10 ? "0" + hour : "" + hour) +
                    ":" + (min < 10 ? "0" + min : "" + min) +
                    " " + (ampm == 0 ? "AM" : "PM");
        } else {
            formatted = (hour < 10 ? "0" + hour : "" + hour) +
                    ":" + (min < 10 ? "0" + min : "" + min) +
                    " " + (ampm == 0 ? "AM" : "PM") +
                    " - " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                    " " + day +
                    ", " + year;
        }

        return formatted;
    }

    public static String getFormattedTime(long time) {

        String formatted = "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        Calendar today = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int ampm = calendar.get(Calendar.AM_PM);
        if(day == today.get(Calendar.DAY_OF_MONTH) && month == today.get(Calendar.MONTH)) {
            formatted = (hour < 10 ? "0" + hour : "" + hour) +
                    ":" + (min < 10 ? "0" + min : "" + min) +
                    " " + (ampm == 0 ? "AM" : "PM");
        } else {
            formatted = (hour < 10 ? "0" + hour : "" + hour) +
                    ":" + (min < 10 ? "0" + min : "" + min) +
                    " " + (ampm == 0 ? "AM" : "PM") +
                    " - " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                    " " + day +
                    ", " + year;
        }

        return formatted;
    }

    public Score getScore(int position) {
        return scores.get(position);
    }

    public Score getSubScore(int position) {
        return subScores.get(position);
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
