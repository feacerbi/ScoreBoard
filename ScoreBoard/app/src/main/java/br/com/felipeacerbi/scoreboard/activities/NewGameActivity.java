package br.com.felipeacerbi.scoreboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.utils.NewGameHelper;

public class NewGameActivity extends ActionBarActivity {

    private GameDAO gameDAO;
    private NewGameHelper ngh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        ngh = new NewGameHelper(this);

        gameDAO = new GameDAO(this);
        Log.i("Test", gameDAO.toString());
    }

    public void createNew() {

        long id = gameDAO.insertGame(ngh.getGame());

        if(id != -1) {
            getIntent().putExtra("game", ngh.getGame());
            setResult(Activity.RESULT_OK);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }

        gameDAO.close();

        finish();

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New Game");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_game, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_save:
                createNew();
                return true;
        }
        return false;
    }
}
