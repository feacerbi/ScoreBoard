package br.com.felipeacerbi.scoreboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import br.com.felipeacerbi.scoreboard.app.ScoreBoardApplication;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.tasks.AddPlayerTask;
import br.com.felipeacerbi.scoreboard.utils.AddPlayerHelper;
import br.com.felipeacerbi.scoreboard.R;

public class AddPlayerActivity extends ActionBarActivity {

    private AddPlayerHelper aph;
    public static final int TAKE_PICTURE = 100;
    private String photoPath;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_add_player);
    }

    public void createNew() {

        new AddPlayerTask(this).execute(aph.getPlayer());

        Intent returnIntent = new Intent();
        returnIntent.putExtra("player", aph.getPlayer());
        setResult(Activity.RESULT_OK, returnIntent);

        finish();

    }

    public void cancel() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);

        finish();

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        View view = getLayoutInflater().inflate(R.layout.actionbar_custom_player, null);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            aph.setPhoto();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_player, menu);

        restoreActionBar();
        aph = new AddPlayerHelper(this);
        if(name != null) {
            aph.getNameField().setText(name);
        }
        if(photoPath != null) {
            aph.setPath(photoPath);
            aph.setPhoto();
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

        String name = aph.getNameField().getText().toString();
        if(name != null) {
            outState.putString("name", name);
        }
        if(aph.getPath() != null) {
            outState.putString("path", aph.getPath());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        restoreActionBar();
        aph = new AddPlayerHelper(this);

        name = savedInstanceState.getString("name");
        photoPath = savedInstanceState.getString("path");
    }
}
