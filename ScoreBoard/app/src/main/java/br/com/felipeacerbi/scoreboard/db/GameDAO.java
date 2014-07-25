package br.com.felipeacerbi.scoreboard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;
import br.com.felipeacerbi.scoreboard.models.Round;

/**
 * Created by Felipe on 05/07/2014.
 */
public class GameDAO extends SQLiteOpenHelper{

    private static final int VERSION = 2;
    private static final String TABLE_GAMES = "Games";
    private static final String DATABASE = "ScoreBoardDB";
    private PlayerDAO playerDAO;
    private RoundDAO roundDAO;
    private Context context;

    public GameDAO(Context context) {

        super(context, DATABASE, null, VERSION);

        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase sldb) {

        String sql = "CREATE TABLE " + TABLE_GAMES
                + "(id INTEGER PRIMARY KEY, "
                + "player1Id INTEGER, "
                + "player2Id INTEGER, "
                + "total1 INTEGER, "
                + "total2 INTEGER, "
                + "winScore INTEGER);";

        sldb.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sldb, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_GAMES;

        sldb.execSQL(sql);

        onCreate(sldb);

    }

    public long insertGame(Game game) {

        playerDAO = new PlayerDAO(context);

        if(game.getPlayer1().getId() == 0) {
            playerDAO.insertPlayer(game.getPlayer1());
        } else if(game.getPlayer2().getId() == 0) {
            playerDAO.insertPlayer(game.getPlayer2());
        }

        roundDAO = new RoundDAO(context);

        ContentValues cv = new ContentValues();

        cv.put("player1Id", game.getPlayer1().getId());
        cv.put("player2Id", game.getPlayer2().getId());
        cv.put("total1", game.getTotalScore1());
        cv.put("total2", game.getTotalScore2());
        cv.put("winScore", game.getWinScore());

        long id = getWritableDatabase().insert(TABLE_GAMES, null, cv);

        for(Round round : game.getRoundsList()) {
            round.setGame(game);
            roundDAO.insertRound(round);
        }

        roundDAO.close();
        playerDAO.close();

        return id;

    }

    public void deleteGame(Game game) {

        roundDAO = new RoundDAO(context);

        String[] args = { String.valueOf(game.getId()) };

        getWritableDatabase().delete(TABLE_GAMES, "id=?", args);

        List<Round> rounds = roundDAO.listRounds(game.getId());

        for(Round round : rounds) {
            roundDAO.deleteRound(round);
        }

        roundDAO.close();
    }

    public void updateGame(Game game) {

        roundDAO = new RoundDAO(context);

        ContentValues cv = new ContentValues();

        cv.put("player1Id", game.getPlayer1().getId());
        cv.put("player2Id", game.getPlayer2().getId());
        cv.put("total1", game.getTotalScore1());
        cv.put("total2", game.getTotalScore2());
        cv.put("winScore", game.getWinScore());

        String[] args = { String.valueOf(game.getId()) };

        getWritableDatabase().update(TABLE_GAMES, cv, "id=?", args);

        List<Round> rounds = roundDAO.listRounds(game.getId());

        for(Round round : rounds) {
            roundDAO.deleteRound(round);
        }

        for(Round round : game.getRoundsList()) {
            round.setGame(game);
            roundDAO.insertRound(round);
        }

        roundDAO.close();

    }

    public List<Game> listGames() {

        roundDAO = new RoundDAO(context);
        playerDAO = new PlayerDAO(context);

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_GAMES + ";", null);

        List<Game> games = new ArrayList<Game>();

        if (c.moveToFirst()) {

            do {

                Game game = new Game();

                game.setId(c.getLong(c.getColumnIndex("id")));
                game.setPlayer1(playerDAO.getPlayer(c.getLong(c.getColumnIndex("player1Id"))));
                game.setPlayer2(playerDAO.getPlayer(c.getLong(c.getColumnIndex("player2Id"))));
                game.setTotalScore1(c.getInt(c.getColumnIndex("total1")));
                game.setTotalScore2(c.getInt(c.getColumnIndex("total2")));
                game.setWinScore(c.getInt(c.getColumnIndex("winScore")));
                game.setRounds(roundDAO.listRounds(game.getId()));

                games.add(game);

            } while (c.moveToNext());

        }

        roundDAO.close();
        playerDAO.close();

        return games;

    }
}
