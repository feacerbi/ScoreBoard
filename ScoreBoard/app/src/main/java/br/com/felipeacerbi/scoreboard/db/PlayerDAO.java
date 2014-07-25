package br.com.felipeacerbi.scoreboard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by Felipe on 05/07/2014.
 */
public class PlayerDAO extends SQLiteOpenHelper{

    private static final int VERSION = 2;
    private static final String TABLE_PLAYERS = "Players";
    private static final String DATABASE = "ScoreBoardDB";

    public PlayerDAO(Context context) {

        super(context, DATABASE, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sldb) {

       String sql = "CREATE TABLE " + TABLE_PLAYERS
                + "(id INTEGER PRIMARY KEY, "
                + "name TEXT UNIQUE NOT NULL, "
                + "score INTEGER, "
                + "photoPath TEXT);";

        sldb.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sldb, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_PLAYERS;

        sldb.execSQL(sql);

        onCreate(sldb);

    }

    public void insertPlayer(Player player) {

        ContentValues cv = new ContentValues();

        cv.put("name", player.getName());
        cv.put("score", player.getScore());
        cv.put("photoPath", player.getPhotoPath());

        getWritableDatabase().insert(TABLE_PLAYERS, null, cv);

    }

    public void deletePlayer(Player player) {

        String[] args = { String.valueOf(player.getId()) };

        getWritableDatabase().delete(TABLE_PLAYERS, "id=?", args);

    }

    public void updatePlayer(Player player) {

        ContentValues cv = new ContentValues();

        cv.put("name", player.getName());
        cv.put("score", player.getScore());
        cv.put("photoPath", player.getPhotoPath());

        String[] args = { String.valueOf(player.getId()) };

        getWritableDatabase().update(TABLE_PLAYERS, cv, "id=?", args);

    }

    public Player getPlayer(long id) {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_PLAYERS + " WHERE id=" + id + ";", null);

        if(c.moveToFirst()) {

            Player player = new Player();

            player.setId(id);
            player.setName(c.getString(c.getColumnIndex("name")));
            player.setScore(c.getInt(c.getColumnIndex("score")));
            player.setPhotoPath(c.getString(c.getColumnIndex("photoPath")));

            return player;

        }

        return null;

    }

    public List<Player> listPlayers() {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_PLAYERS + ";", null);

        List<Player> players = new ArrayList<Player>();

        if (c.moveToFirst()) {

            do {

                Player player = new Player();

                player.setId(c.getLong(c.getColumnIndex("id")));
                player.setName(c.getString(c.getColumnIndex("name")));
                player.setScore(c.getInt(c.getColumnIndex("score")));
                player.setPhotoPath(c.getString(c.getColumnIndex("photoPath")));

                players.add(player);

            } while (c.moveToNext());

        }

        return players;

    }
}
