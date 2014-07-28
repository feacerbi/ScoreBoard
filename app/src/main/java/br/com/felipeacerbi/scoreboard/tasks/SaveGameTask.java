package br.com.felipeacerbi.scoreboard.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.app.ScoreBoardApplication;
import br.com.felipeacerbi.scoreboard.db.CompetitorDAO;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.models.Competitor;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class SaveGameTask extends AsyncTask<Game, Void, Void> {

    MainScoreActivity msa;
    private ProgressDialog pdia;

    public SaveGameTask(MainScoreActivity msa) {

        this.msa = msa;

        msa.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdia = new ProgressDialog(msa);
        pdia.setMessage("Saving Game...");
        pdia.show();
    }

    @Override
    protected Void doInBackground(Game... games) {

        GameDAO gameDAO = new GameDAO(msa);

        if(gameDAO.idExists(games[0])) {
            gameDAO.updateGame(games[0]);
        } else {
            msa.getGame().setId(gameDAO.insertGame(games[0]));
        }

        gameDAO.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        pdia.dismiss();

        msa.getApp().unregister(this);
    }
}
