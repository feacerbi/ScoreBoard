package br.com.felipeacerbi.scoreboard.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.adapters.PlayersListAdapter;
import br.com.felipeacerbi.scoreboard.app.ScoreBoardApplication;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class ListPlayersTask extends AsyncTask<Void, Void, PlayersListAdapter> {

    private MainScoreActivity msb;
    private ListView list;
    private ProgressDialog pdia;

    public ListPlayersTask(MainScoreActivity msb, ListView list) {
        this.msb = msb;
        this.list = list;

        msb.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdia = new ProgressDialog(msb);
        pdia.setMessage("Loading Players...");
        pdia.show();
    }

    @Override
    protected PlayersListAdapter doInBackground(Void... voids) {

        PlayerDAO pdao = new PlayerDAO(msb);

        PlayersListAdapter pla = new PlayersListAdapter(msb, pdao.listPlayers());

        pdao.close();

        return pla;
    }

    @Override
    protected void onPostExecute(PlayersListAdapter playersListAdapter) {

        pdia.dismiss();

        list.setAdapter(playersListAdapter);

        msb.getApp().unregister(this);
    }
}
