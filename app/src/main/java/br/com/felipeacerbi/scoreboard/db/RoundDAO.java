package br.com.felipeacerbi.scoreboard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;
import br.com.felipeacerbi.scoreboard.models.Round;
import br.com.felipeacerbi.scoreboard.models.Score;

/**
 * Created by Felipe on 05/07/2014.
 */
public class RoundDAO extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String TABLE_ROUNDS = "Rounds";
    private static final String DATABASE = "ScoreBoardDB";
    private ScoreDAO scoreDAO;
    Context context;

    public RoundDAO(Context context) {

        super(context, DATABASE, null, VERSION);

        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sldb) {

       String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUNDS
                + "(id INTEGER PRIMARY KEY, "
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

    public long insertRound(Round round) {

        ContentValues cv = new ContentValues();

        cv.put("scoreTitle", round.getScoreTitle());
        cv.put("gameId", round.getGame().getId());

        long id = getWritableDatabase().insert(TABLE_ROUNDS, null, cv);

        scoreDAO = new ScoreDAO(context);

        for(Score score : round.getScoresList()) {
            score.setRoundId(id);
            scoreDAO.insertScore(score);
        }

        for(Score subScore : round.getSubScoresList()) {
            subScore.setRoundId(id);
            scoreDAO.insertScore(subScore);
        }

        scoreDAO.close();

        return id;

    }

    public void deleteRound(Round round) {

        scoreDAO = new ScoreDAO(context);
        scoreDAO.deleteRoundScores(round.getId());
        scoreDAO.close();

        String[] args = { String.valueOf(round.getId()) };
        getWritableDatabase().delete(TABLE_ROUNDS, "id=?", args);

    }

    public List<Round> listRounds(Game game) {

        scoreDAO = new ScoreDAO(context);

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_ROUNDS + " WHERE gameId=" + game.getId() + ";", null);

        List<Round> rounds = new ArrayList<Round>();

        if (c.moveToFirst()) {

            do {

                Round round = new Round(game.getGameMode());

                round.setId(c.getLong(c.getColumnIndex("id")));
                round.setScoreTitle(c.getString(c.getColumnIndex("scoreTitle")));
                round.setScores(scoreDAO.listRoundScores(round.getId(), Score.SCORE_NORMAL));
                round.setSubScores(scoreDAO.listRoundScores(round.getId(), Score.SCORE_SUB));
                rounds.add(round);

            } while (c.moveToNext());

        }

        scoreDAO.close();

        return rounds;

    }
}
