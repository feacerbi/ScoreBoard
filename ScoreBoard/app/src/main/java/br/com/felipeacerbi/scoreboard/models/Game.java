package br.com.felipeacerbi.scoreboard.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe.acerbi on 03/07/2014.
 */
public class Game implements Serializable {

    private long id;
    private Player player1;
    private Player player2;
    private int totalScore1;
    private int totalScore2;
    private int winScore;
    private List<Round> rounds;

    public Game() {

        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        totalScore1 = 0;
        totalScore2 = 0;
        winScore = 3000;
        rounds = new ArrayList<Round>();

    }

    public Game(Player player1, Player player2) {

        this.player1 = player1;
        this.player2 = player2;
        totalScore1 = 0;
        totalScore2 = 0;
        winScore = 3000;
        rounds = new ArrayList<Round>();

    }

    public void refreshTotal() {

        int total1 = 0;
        int total2 = 0;

        for(Round round : rounds) {
            total1 += round.getScore1();
            total2 += round.getScore2();
        }

        setTotalScore1(total1);
        setTotalScore2(total2);

        //TODO Check if there is a winner.

    }

    public void addRound(Round round) {

        rounds.add(round);
        refreshTotal();

    }

    public int getRounds() {
        return rounds.size();
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getTotalScore1() {
        return totalScore1;
    }

    public void setTotalScore1(int totalScore1) {
        this.totalScore1 = totalScore1;
    }

    public int getTotalScore2() {
        return totalScore2;
    }

    public void setTotalScore2(int totalScore2) {
        this.totalScore2 = totalScore2;
    }

    public List<Round> getRoundsList() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

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
}
