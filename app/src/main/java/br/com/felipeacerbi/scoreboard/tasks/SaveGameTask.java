package br.com.felipeacerbi.scoreboard.tasks;

import android.os.AsyncTask;
import android.util.Log;

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

    public SaveGameTask(MainScoreActivity msa) {

        this.msa = msa;

        msa.getApp().register(this);
    }

    @Override
    protected Void doInBackground(Game... games) {

        GameDAO gameDAO = new GameDAO(msa);
        CompetitorDAO competitorDAO = new CompetitorDAO(msa);

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
        msa.getApp().unregister(this);
    }
}
