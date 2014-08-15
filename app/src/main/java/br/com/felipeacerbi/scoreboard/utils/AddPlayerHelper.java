package br.com.felipeacerbi.scoreboard.utils;

import java.io.File;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.AddPlayerActivity;
import br.com.felipeacerbi.scoreboard.models.Player;

public class AddPlayerHelper {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory() + "/ScoreBoard/";
	
	private AddPlayerActivity apa;
	private EditText nameField;
	private ImageView photo;
	private String path;
	private long id;
    private LinearLayout createButton;
    private LinearLayout cancelButton;

    public AddPlayerHelper(AddPlayerActivity apa) {
		 
		this.apa = apa;
		 
		getInfo();

        if(getModify()) {
            ((TextView) createButton.findViewById(R.id.choice_ok_text)).setText("MODIFY");
        }

        nameField.clearFocus();
	}
	 
	public void getInfo() {
		
		nameField = (EditText) apa.findViewById(R.id.name_bar);
		photo = (ImageView) apa.findViewById(R.id.pic_view);
        createButton = (LinearLayout) apa.findViewById(R.id.choice_ok);
        cancelButton = (LinearLayout) apa.findViewById(R.id.choice_cancel);

        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                apa.createNew();
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                apa.cancel();
            }
        });

		apa.registerForContextMenu(photo);

        photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                apa.openContextMenu(view);
            }
        });
		
	}

    public boolean checkFolder() {

        File defaultFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), "ScoreBoard");

        if(!defaultFolder.exists()) {
            return defaultFolder.mkdir();
        }

        return false;
    }

	public boolean getModify() {

		Player player = (Player) apa.getIntent().getSerializableExtra("player");

		if(player != null) {

			id = player.getId();
			nameField.setText(player.getName());
			if(player.getPhotoPath() != null) {
				setPath(player.getPhotoPath());
                setPhoto();
			}

            return true;
		}

        return false;
	}
	
	public Player getPlayer() {

        Player player = new Player();
        player.setId(id);
        player.setName(nameField.getText().toString());
        player.setPhotoPath(path);
		
		return player;
		
	}

	public String getPath() {
		return path;
	}

    public void setPath(String path) {
        this.path = path;
    }

    public void setPhoto() {
        Ion.with(photo)
                .placeholder(R.drawable.ic_contact_picture_big)
                .error(R.drawable.ic_contact_picture_big)
                .load(getPath());
    }

    public EditText getNameField() {
        return nameField;
    }

}
