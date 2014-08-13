package br.com.felipeacerbi.scoreboard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

import br.com.felipeacerbi.scoreboard.app.ScoreBoardApplication;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.tasks.AddPlayerTask;
import br.com.felipeacerbi.scoreboard.utils.AddPlayerHelper;
import br.com.felipeacerbi.scoreboard.R;

public class AddPlayerActivity extends ActionBarActivity {

    private AddPlayerHelper aph;
    public static final int TAKE_PICTURE = 100;
    public static final int PICK_PICTURE = 101;
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
                View focusView = AddPlayerActivity.this.getCurrentFocus();
                if(focusView != null) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
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
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                aph.setPhoto();
            } else if(requestCode == PICK_PICTURE) {
                aph.setPath(getBitmapPath(data));
                aph.setPhoto();
            }
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        aph.checkFolder();

        MenuItem selfie = menu.add("Take a Selfie");
        selfie.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                aph.setPath(AddPlayerHelper.DEFAULT_PATH + System.currentTimeMillis() + ".jpg");

                cam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(aph.getPath())));

                if(cam.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cam, TAKE_PICTURE);
                } else {
                    Toast.makeText(AddPlayerActivity.this, "No camera app found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        MenuItem pick = menu.add("Pick from Gallery");
        pick.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent gal = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(gal.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(gal, PICK_PICTURE);
                } else {
                    Toast.makeText(AddPlayerActivity.this, "No gallery app found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public String getBitmapPath(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
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
