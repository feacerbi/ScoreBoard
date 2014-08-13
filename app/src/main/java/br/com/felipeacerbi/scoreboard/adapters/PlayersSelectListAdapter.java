package br.com.felipeacerbi.scoreboard.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by Felipe on 05/07/2014.
 */
public class PlayersSelectListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Player> players;

    public PlayersSelectListAdapter(Activity activity, List<Player> players) {

        this.activity = activity;
        this.players = players;
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int id) {
        return players.get(id);
    }

    @Override
    public long getItemId(int pos) {
        return players.indexOf(pos);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        View playerItem;

        if(view == null) {
            playerItem = activity.getLayoutInflater().inflate(R.layout.player_item, null);
        } else {
            playerItem = view;
        }
            final ImageView pic = (ImageView) playerItem.findViewById(R.id.pic_listview);
            TextView name = (TextView) playerItem.findViewById(R.id.name_listview);
            TextView score = (TextView) playerItem.findViewById(R.id.score_listview);

        Player player = players.get(pos);

        Ion.with(pic)
                .resize(120, 120)
                .centerCrop()
                .placeholder(R.drawable.ic_contact_picture)
                .error(R.drawable.ic_contact_picture)
                .load(player.getPhotoPath());

        name.setText(player.getName());
        score.setText("Score: " + player.getScore());
        playerItem.setBackgroundResource(R.drawable.list_style);

        return playerItem;
    }

    public List<Player> getPlayers() {
        return players;
    }

}
