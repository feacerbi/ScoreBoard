package br.com.felipeacerbi.scoreboard.tasks;

import android.os.AsyncTask;

import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class AddPointsTask extends AsyncTask<Player, Void, Void> {

    MainScoreActivity msa;
    private int points;

    public AddPointsTask(MainScoreActivity msa, int points) {

        this.msa = msa;
        this.points = points;

        msa.getApp().register(this);
    }

    @Override
    protected Void doInBackground(Player... players) {

        players[0].setScore(players[0].getScore() + points);

        PlayerDAO playerDAO = new PlayerDAO(msa);

        playerDAO.updatePlayer(players[0]);

        playerDAO.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        msa.getApp().unregister(this);
    }
}
