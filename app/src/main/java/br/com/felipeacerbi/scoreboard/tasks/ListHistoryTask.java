package br.com.felipeacerbi.scoreboard.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.adapters.GamesListAdapter;
import br.com.felipeacerbi.scoreboard.adapters.HistoryListAdapter;
import br.com.felipeacerbi.scoreboard.db.GameDAO;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class ListHistoryTask extends AsyncTask<Void, Void, HistoryListAdapter> {

    private MainScoreActivity msb;
    private ListView list;
    private ProgressDialog pdia;

    public ListHistoryTask(MainScoreActivity msb, ListView list) {
        this.msb = msb;
        this.list = list;

        msb.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdia = new ProgressDialog(msb);
        pdia.setMessage("Loading History...");
        pdia.show();
    }

    @Override
    protected HistoryListAdapter doInBackground(Void... voids) {

        GameDAO gdao = new GameDAO(msb);

        HistoryListAdapter hla = new HistoryListAdapter(msb, gdao.listHistoryGames());

        gdao.close();

        return hla;
    }

    @Override
    protected void onPostExecute(HistoryListAdapter historyListAdapter) {

        pdia.dismiss();

        list.setAdapter(historyListAdapter);

        msb.getApp().unregister(this);
    }
}
