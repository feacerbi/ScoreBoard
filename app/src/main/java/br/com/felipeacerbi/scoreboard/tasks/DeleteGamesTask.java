package br.com.felipeacerbi.scoreboard.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.List;

import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.adapters.GamesListAdapter;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.models.Game;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class DeleteGamesTask extends AsyncTask<List<Game>, Void, Void> {

    MainScoreActivity msa;
    ListView list;
    private ProgressDialog pdia;

    public DeleteGamesTask(MainScoreActivity msa, ListView list) {

        this.msa = msa;
        this.list = list;

        msa.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(list != null) {
            pdia = new ProgressDialog(msa);
            pdia.setMessage("Deleting Games...");
            pdia.show();
        }
    }

    @Override
    protected Void doInBackground(List<Game>... lists) {

        GameDAO gameDAO = new GameDAO(msa);

        List<Game> games = lists[0];

        for(Game game : games) {
            gameDAO.deleteGame(game);
        }

        gameDAO.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if(list != null) {
            GameDAO gameDAO = new GameDAO(msa);

            GamesListAdapter gla = new GamesListAdapter(msa, gameDAO.listGames());

            list.setAdapter(gla);

            gameDAO.close();

            pdia.dismiss();
        }

        msa.getApp().unregister(this);
    }
}
