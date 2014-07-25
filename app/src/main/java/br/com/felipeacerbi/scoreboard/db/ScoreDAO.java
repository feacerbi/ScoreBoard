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
import br.com.felipeacerbi.scoreboard.models.Score;

/**
 * Created by Felipe on 05/07/2014.
 */
public class ScoreDAO extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String TABLE_SCORES = "Scores";
    private static final String DATABASE = "ScoreBoardDB";
    private Context context;

    public ScoreDAO(Context context) {

        super(context, DATABASE, null, VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sldb) {

       String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORES
                + "(value INTEGER, "
                + "type INTEGER, "
                + "gameId INTEGER, "
                + "roundId INTEGER);";

        sldb.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sldb, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_SCORES;

        sldb.execSQL(sql);

        onCreate(sldb);

    }

    public long insertScore(Score score) {

        ContentValues cv = new ContentValues();

        cv.put("value", score.getValue());
        cv.put("type", score.getType());
        cv.put("gameId", score.getGameId());
        cv.put("roundId", score.getRoundId());

        long id = getWritableDatabase().insert(TABLE_SCORES, null, cv);

        return id;
    }

    public void deleteRoundScores(long roundId) {

        String[] args = { String.valueOf(roundId) };

        getWritableDatabase().delete(TABLE_SCORES, "roundId=?", args);

    }

    public void deleteGameScores(long gameId) {

        String[] args = { String.valueOf(gameId) };

        getWritableDatabase().delete(TABLE_SCORES, "gameId=?", args);

    }

    public List<Score> listRoundScores(long roundId, int type) {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_SCORES + " WHERE roundId=" + roundId + " AND type=" + type + ";", null);

        List<Score> scores = new ArrayList<Score>();

        if (c.moveToFirst()) {

            do {

                Score score = new Score(type);

                score.setValue(c.getInt(c.getColumnIndex("value")));
                score.setGameId(c.getLong(c.getColumnIndex("gameId")));
                score.setRoundId(c.getLong(c.getColumnIndex("roundId")));

                scores.add(score);

            } while (c.moveToNext());

        }

        return scores;

    }

    public List<Score> listGameScores(long gameId, int type) {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_SCORES + " WHERE gameId=" + gameId + " AND type=" + type + ";", null);

        List<Score> scores = new ArrayList<Score>();

        if (c.moveToFirst()) {

            do {

                Score score = new Score(type);

                score.setValue(c.getInt(c.getColumnIndex("value")));
                score.setGameId(c.getLong(c.getColumnIndex("gameId")));
                score.setRoundId(c.getLong(c.getColumnIndex("roundId")));

                scores.add(score);

            } while (c.moveToNext());

        }

        return scores;

    }
}
