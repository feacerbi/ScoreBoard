package br.com.felipeacerbi.scoreboard.utils;

import java.io.File;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.AddPlayerActivity;
import br.com.felipeacerbi.scoreboard.models.Player;

public class AddPlayerHelper {
	
	private AddPlayerActivity apa;
	private EditText name;
	private ImageView photo;
	private String path;
	private long id;
	private Button but;
    private RadioButton maleGender;
    private RadioButton femaleGender;

    public AddPlayerHelper(AddPlayerActivity apa) {
		 
		this.apa = apa;
		 
		getInfo();
        getModify();
		
	}
	 
	public void getInfo() {
		
		name = (EditText) apa.findViewById(R.id.name_bar);
		photo = (ImageView) apa.findViewById(R.id.pic_view);
		but = (Button) apa.findViewById(R.id.pic_button);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullImage(photo.getDrawable());
            }
        });
		
		but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				path = apa.getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
				
				cam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
				
				apa.startActivityForResult(cam, AddPlayerActivity.TAKE_PICTURE );
				
			}
		});
		
	}

	public void getModify() {

		Player player = (Player) apa.getIntent().getSerializableExtra("player");

		if(player != null) {

			id = player.getId();
			name.setText(player.getName());
			if(player.getPhotoPath() != null) {
                Bitmap bmp = BitmapFactory.decodeFile(player.getPhotoPath());
				photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true));
				path = player.getPhotoPath();
			}

		}
	}
	
	public Player getPlayer() {

        Player player = new Player();
        player.setId(id);
        player.setName(name.getText().toString());
        player.setPhotoPath(path);
		
		return player;
		
	}
	
	public String getPath() {

		return path;

	}



    public void fullImage(Drawable image) {

        Dialog mSplashDialog = new Dialog(apa);
        mSplashDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        mSplashDialog.setContentView(R.layout.image_fullscreen);
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSplashDialog.setCancelable(true);
        ((ImageView) mSplashDialog.findViewById(R.id.imageview_fullscreen)).setImageDrawable(image);
        mSplashDialog.show();

    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }
}
