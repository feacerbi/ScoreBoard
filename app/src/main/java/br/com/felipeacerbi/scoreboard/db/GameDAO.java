package br.com.felipeacerbi.scoreboard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.models.Competitor;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;
import br.com.felipeacerbi.scoreboard.models.Round;
import br.com.felipeacerbi.scoreboard.models.Score;

/**
 * Created by Felipe on 05/07/2014.
 */
public class GameDAO extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String TABLE_GAMES = "Games";
    private static final String DATABASE = "ScoreBoardDB";
    private Context context;

    public GameDAO(Context context) {

        super(context, DATABASE, null, VERSION);

        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sldb) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_GAMES
                + "(id INTEGER PRIMARY KEY, "
                + "winScore INTEGER, "
                + "gameMode INTEGER);";

        sldb.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sldb, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_GAMES;

        sldb.execSQL(sql);

        onCreate(sldb);

    }

    public long insertGame(Game game) {

        PlayerDAO playerDAO = new PlayerDAO(context);
        CompetitorDAO competitorDAO = new CompetitorDAO(context);

        for(Player player : game.getPlayersList()) {
            if(!playerDAO.idExists(player)) {
                player.setId(playerDAO.insertPlayer(player));
            }
            Competitor competitor = new Competitor(game.getId(), player.getId());
            competitorDAO.insertCompetitor(competitor);
        }

        ScoreDAO scoreDAO = new ScoreDAO(context);

        for(Score totalScore : game.getTotalScoresList()) {
            scoreDAO.insertScore(totalScore);
        }

        RoundDAO roundDAO = new RoundDAO(context);

        for(Round round : game.getRoundsList()) {
            roundDAO.insertRound(round);
        }

        ContentValues cv = new ContentValues();

        cv.put("winScore", game.getWinScore());
        cv.put("gameMode", game.getGameMode());

        long id = getWritableDatabase().insert(TABLE_GAMES, null, cv);

        playerDAO.close();
        competitorDAO.close();
        scoreDAO.close();
        roundDAO.close();

        return id;

    }

    public void deleteGame(Game game) {

        CompetitorDAO competitorDAO = new CompetitorDAO(context);
        competitorDAO.deleteCompetitors(game.getId());

        ScoreDAO scoreDAO = new ScoreDAO(context);
        scoreDAO.deleteGameScores(game.getId());

        RoundDAO roundDAO = new RoundDAO(context);

        List<Round> rounds = roundDAO.listRounds(game);

        for(Round round : rounds) {
            roundDAO.deleteRound(round);
        }

        String[] args = { String.valueOf(game.getId()) };

        getWritableDatabase().delete(TABLE_GAMES, "id=?", args);

        competitorDAO.close();
        scoreDAO.close();
        roundDAO.close();
    }

    public void updateGame(Game game) {

        CompetitorDAO competitorDAO = new CompetitorDAO(context);
        competitorDAO.deleteCompetitors(game.getId());

        PlayerDAO playerDAO = new PlayerDAO(context);

        for(Player player : game.getPlayersList()) {
            if(!playerDAO.idExists(player)) {
                player.setId(playerDAO.insertPlayer(player));
            }
            Competitor competitor = new Competitor(game.getId(), player.getId());
            competitorDAO.insertCompetitor(competitor);
        }

        ScoreDAO scoreDAO = new ScoreDAO(context);
        scoreDAO.deleteGameScores(game.getId());

        for(Score totalScore : game.getTotalScoresList()) {
            scoreDAO.insertScore(totalScore);
        }

        RoundDAO roundDAO = new RoundDAO(context);

        List<Round> rounds = roundDAO.listRounds(game);

        for(Round round : rounds) {
            roundDAO.deleteRound(round);
        }

        for(Round round : game.getRoundsList()) {
            round.setId(roundDAO.insertRound(round));
        }

        ContentValues cv = new ContentValues();

        cv.put("winScore", game.getWinScore());
        cv.put("gameMode", game.getGameMode());

        String[] args = { String.valueOf(game.getId()) };

        getWritableDatabase().update(TABLE_GAMES, cv, "id=?", args);

        competitorDAO.close();
        scoreDAO.close();
        roundDAO.close();

    }

    public boolean idExists(Game game) {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_GAMES + " WHERE id=" + game.getId() + ";", null);

        if(c.moveToFirst()) return true;

        return false;

    }

    public List<Game> listGames() {

        ScoreDAO scoreDAO = new ScoreDAO(context);
        RoundDAO roundDAO = new RoundDAO(context);
        PlayerDAO playerDAO = new PlayerDAO(context);
        CompetitorDAO competitorDAO = new CompetitorDAO(context);

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_GAMES + ";", null);

        List<Game> games = new ArrayList<Game>();

        if (c.moveToFirst()) {

            do {

                Game game = new Game(c.getInt(c.getColumnIndex("gameMode")));

                game.setId(c.getLong(c.getColumnIndex("id")));
                game.setWinScore(c.getInt(c.getColumnIndex("winScore")));
                game.setTotalScores(scoreDAO.listGameScores(game.getId(), Score.SCORE_TOTAL));
                game.setRounds(roundDAO.listRounds(game));
                for(Competitor competitor : competitorDAO.listCompetitors(game.getId())) {
                    game.getPlayersList().add(playerDAO.getPlayer(competitor.getPlayerId()));
                }

                games.add(game);

            } while (c.moveToNext());

        }

        scoreDAO.close();
        roundDAO.close();
        playerDAO.close();
        competitorDAO.close();

        return games;

    }
}
