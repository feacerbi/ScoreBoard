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
public class RoundDAO extends SQLiteOpenHelper{

    private static final int VERSION = 2;
    private static final String TABLE_ROUNDS = "Rounds";
    private static final String DATABASE = "ScoreBoardDB";

    public RoundDAO(Context context) {

        super(context, DATABASE, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sldb) {

       String sql = "CREATE TABLE " + TABLE_ROUNDS
                + "(id INTEGER PRIMARY KEY, "
                + "score1 INTEGER, "
                + "score2 INTEGER, "
                + "subscore1 INTEGER, "
                + "subscore2 INTEGER, "
                + "scoreTitle TEXT, "
                + "gameId INTEGER);";

        sldb.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sldb, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_ROUNDS;

        sldb.execSQL(sql);

        onCreate(sldb);

    }

    public void insertRound(Round round) {

        ContentValues cv = new ContentValues();

        cv.put("score1", round.getScore1());
        cv.put("score2", round.getScore2());
        cv.put("subscore1", round.getSubScore1());
        cv.put("subscore2", round.getSubScore2());
        cv.put("scoreTitle", round.getScoreTitle());
        cv.put("gameId", round.getGame().getId());

        getWritableDatabase().insert(TABLE_ROUNDS, null, cv);

    }

    public void deleteRound(Round round) {

        String[] args = { String.valueOf(round.getId()) };

        getWritableDatabase().delete(TABLE_ROUNDS, "id=?", args);

    }

    public void updateRound(Round round) {

        ContentValues cv = new ContentValues();

        cv.put("score1", round.getScore1());
        cv.put("score2", round.getScore2());
        cv.put("subscore1", round.getSubScore1());
        cv.put("subscore2", round.getSubScore2());
        cv.put("scoreTitle", round.getScoreTitle());
        cv.put("gameId", round.getGame().getId());

        String[] args = { String.valueOf(round.getId()) };

        getWritableDatabase().update(TABLE_ROUNDS, cv, "id=?", args);

    }

    public List<Round> listRounds(long gameId) {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_ROUNDS + " WHERE gameId=" + gameId + ";", null);

        List<Round> rounds = new ArrayList<Round>();

        if (c.moveToFirst()) {

            do {

                Round round = new Round();

                round.setId(c.getLong(c.getColumnIndex("id")));
                round.setScore1(c.getInt(c.getColumnIndex("score1")));
                round.setScore2(c.getInt(c.getColumnIndex("score2")));
                round.setSubScore1(c.getInt(c.getColumnIndex("subscore1")));
                round.setSubScore2(c.getInt(c.getColumnIndex("subscore2")));
                round.setScoreTitle(c.getString(c.getColumnIndex("scoreTitle")));

                rounds.add(round);

            } while (c.moveToNext());

        }

        return rounds;

    }
}
