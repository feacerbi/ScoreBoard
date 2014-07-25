package br.com.felipeacerbi.scoreboard.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import br.com.felipeacerbi.scoreboard.activities.NewGameActivity;
import br.com.felipeacerbi.scoreboard.adapters.NewGamePlayersAdapter;
import br.com.felipeacerbi.scoreboard.adapters.PlayersSelectListAdapter;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class SelectPlayersTask extends AsyncTask<Void, Void, PlayersSelectListAdapter> {

    private NewGameActivity nga;
    private ProgressDialog pdia;
    private NewGamePlayersAdapter parentAdapter;

    public SelectPlayersTask(NewGameActivity nga, NewGamePlayersAdapter parentAdapter) {
        this.nga = nga;
        this.parentAdapter = parentAdapter;
        this.nga.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdia = new ProgressDialog(nga);
        pdia.setMessage("Loading Players...");
        pdia.show();
    }

    @Override
    protected PlayersSelectListAdapter doInBackground(Void... voids) {

        PlayerDAO pdao = new PlayerDAO(nga);

        final PlayersSelectListAdapter adapter = new PlayersSelectListAdapter(nga, pdao.listPlayers());

        pdao.close();

        return adapter;
    }

    @Override
    protected void onPostExecute(PlayersSelectListAdapter playersListAdapter) {

        pdia.dismiss();

        parentAdapter.getPlayerNameDialog(parentAdapter.getTemp(), playersListAdapter);

        nga.getApp().unregister(this);
    }
}
