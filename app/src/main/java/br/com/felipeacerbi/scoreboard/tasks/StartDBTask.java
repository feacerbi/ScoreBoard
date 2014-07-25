package br.com.felipeacerbi.scoreboard.tasks;

import android.os.AsyncTask;

import br.com.felipeacerbi.scoreboard.app.ScoreBoardApplication;
import br.com.felipeacerbi.scoreboard.db.CompetitorDAO;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.db.RoundDAO;
import br.com.felipeacerbi.scoreboard.db.ScoreDAO;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class StartDBTask extends AsyncTask<Object, Void, Void> {

    private ScoreBoardApplication sba;

    public StartDBTask(ScoreBoardApplication sba) {

        this.sba = sba;

        sba.register(this);
    }

    @Override
    protected Void doInBackground(Object[] objects) {

        PlayerDAO pdao = new PlayerDAO(sba);
        RoundDAO rdao = new RoundDAO(sba);
        GameDAO gdao = new GameDAO(sba);
        ScoreDAO sdao = new ScoreDAO(sba);
        CompetitorDAO cdao = new CompetitorDAO(sba);

        pdao.onCreate(pdao.getWritableDatabase());
        rdao.onCreate(rdao.getWritableDatabase());
        gdao.onCreate(gdao.getWritableDatabase());
        sdao.onCreate(sdao.getWritableDatabase());
        cdao.onCreate(sdao.getWritableDatabase());

        pdao.close();
        rdao.close();
        gdao.close();
        sdao.close();
        cdao.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        sba.unregister(this);
    }
}
