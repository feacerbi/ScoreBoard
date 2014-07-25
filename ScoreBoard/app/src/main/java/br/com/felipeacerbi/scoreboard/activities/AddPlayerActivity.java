package br.com.felipeacerbi.scoreboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import br.com.felipeacerbi.scoreboard.utils.AddPlayerHelper;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.R;

public class AddPlayerActivity extends Activity {

    private AddPlayerHelper aph;
    private PlayerDAO playerDAO;
    private boolean isNew;
    public static final int TAKE_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        aph = new AddPlayerHelper(this);

        playerDAO = new PlayerDAO(this);

        isNew = aph.getModify();
    }

    public void createNew() {

        if (isNew) {
            playerDAO.insertPlayer(aph.getPlayer());
        } else {
            playerDAO.updatePlayer(aph.getPlayer());
        }

        playerDAO.close();

        finish();

    }

    public void restoreActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Player");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(aph.getPath());

            aph.getPhoto().setImageBitmap(Bitmap.createScaledBitmap(bmp,
                    bmp.getWidth(), bmp.getHeight(),
                    true));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_player, menu);
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
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
