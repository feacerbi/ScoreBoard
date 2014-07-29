package br.com.felipeacerbi.scoreboard.tasks;

import android.os.AsyncTask;

import br.com.felipeacerbi.scoreboard.activities.AddPlayerActivity;
import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class AddPlayerTask extends AsyncTask<Player, Void, Void> {

    AddPlayerActivity apa;

    public AddPlayerTask(AddPlayerActivity apa) {

        this.apa = apa;

        apa.getApp().register(this);
    }

    @Override
    protected Void doInBackground(Player... players) {

        PlayerDAO playerDAO = new PlayerDAO(apa);

        players[0].setId(playerDAO.insertPlayer(players[0]));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        apa.getApp().unregister(this);
    }
}
