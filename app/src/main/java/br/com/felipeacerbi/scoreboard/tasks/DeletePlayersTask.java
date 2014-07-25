package br.com.felipeacerbi.scoreboard.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.List;

import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.adapters.GamesListAdapter;
import br.com.felipeacerbi.scoreboard.adapters.PlayersListAdapter;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class DeletePlayersTask extends AsyncTask<List<Player>, Void, Void> {

    MainScoreActivity msa;
    ListView list;
    private ProgressDialog pdia;

    public DeletePlayersTask(MainScoreActivity msa, ListView list) {

        this.msa = msa;
        this.list = list;

        msa.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(list != null) {
            pdia = new ProgressDialog(msa);
            pdia.setMessage("Deleting Players...");
            pdia.show();
        }
    }

    @Override
    protected Void doInBackground(List<Player>... lists) {

        PlayerDAO playerDAO = new PlayerDAO(msa);

        List<Player> players = lists[0];

        for(Player player : players) {
            playerDAO.deletePlayer(player);
        }

        playerDAO.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if(list != null) {
            PlayerDAO playerDAO = new PlayerDAO(msa);

            PlayersListAdapter pla = new PlayersListAdapter(msa, playerDAO.listPlayers());

            list.setAdapter(pla);

            playerDAO.close();

            pdia.dismiss();

            msa.getApp().unregister(this);
        }
    }
}
