package br.com.felipeacerbi.scoreboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.app.ScoreBoardApplication;
import br.com.felipeacerbi.scoreboard.fragments.CurrentMatchFragment;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;
import br.com.felipeacerbi.scoreboard.utils.NewGameHelper;

public class NewGameActivity extends ActionBarActivity {

    private NewGameHelper ngh;
    public static final int NEW_PLAYER = 102;
    Bundle saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

    }

    public void createNew() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("game", ngh.getGame());
        setResult(Activity.RESULT_OK, returnIntent);

        finish();

    }

    public void cancel() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);

        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_PLAYER && resultCode == Activity.RESULT_OK) {
            Player player = (Player) data.getExtras().getSerializable("player");
            ngh.getAdapter().setNewPlayer(player);
        } else {
            if(saveState != null) {
                onRestoreInstanceState(saveState);
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        View view = getLayoutInflater().inflate(R.layout.actionbar_custom_game, null);

        View cancel = view.findViewById(R.id.choice_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        View start = view.findViewById(R.id.choice_ok);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNew();
            }
        });

        actionBar.setCustomView(view, lp);
    }

    public ScoreBoardApplication getApp() {
        return (ScoreBoardApplication) getApplication();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState = new Bundle();
        onSaveInstanceState(saveState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_game, menu);

        if(ngh == null) {
            restoreActionBar();
            ngh = new NewGameHelper(this);
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Game game = ngh.reload();
        boolean isNew = ngh.isGameNew();

        if(game != null) {
            outState.putSerializable("game", game);
            outState.putBoolean("isNew", isNew);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Game game = (Game) savedInstanceState.getSerializable("game");
        boolean isNew = savedInstanceState.getBoolean("isNew");

        restoreActionBar();
        if(ngh == null) {
            if(game != null) {
                ngh = new NewGameHelper(this, game, isNew);
            } else {
                ngh = new NewGameHelper(this);
            }

        }

    }
}
