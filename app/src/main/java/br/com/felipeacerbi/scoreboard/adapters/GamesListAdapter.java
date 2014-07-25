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

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by Felipe on 05/07/2014.
 */
public class GamesListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Game> games;
    private SparseBooleanArray selectedIds;

    public GamesListAdapter(Activity activity, List<Game> games) {

        this.activity = activity;
        this.games = games;
        selectedIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int id) {
        return games.get(id);
    }

    @Override
    public long getItemId(int pos) {
        return games.indexOf(pos);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        View gameItem;

        if(view == null) {
            gameItem = activity.getLayoutInflater().inflate(R.layout.game_item, null);
        } else {
            gameItem = view;
        }

        TextView name1 = (TextView) gameItem.findViewById(R.id.name_1);
        TextView name2 = (TextView) gameItem.findViewById(R.id.name_2);
        TextView round = (TextView) gameItem.findViewById(R.id.round_number);
        TextView score1 = (TextView) gameItem.findViewById(R.id.score_1);
        TextView score2 = (TextView) gameItem.findViewById(R.id.score_2);
        TextView winscore = (TextView) gameItem.findViewById(R.id.winscore);

        Game game = games.get(pos);

        if(game.getPlayer1() != null && game.getPlayer2() != null) {
            name1.setText(game.getPlayer1().getName());
            name2.setText(game.getPlayer2().getName());
            round.setText("Round " + (game.getRounds() + 1));
            score1.setText(String.valueOf(game.getTotalScore1()));
            score2.setText(String.valueOf(game.getTotalScore2()));
            winscore.setText(String.valueOf(game.getWinScore()));
        }
        if(selectedIds.get(pos)) {
            gameItem.setBackgroundColor(activity.getResources().getColor(R.color.pressed_color));
        } else {
            gameItem.setBackgroundResource(R.drawable.list_style);
        }

        gameItem.setTag(game);

        return gameItem;
    }

    public List<Game> getGames() {
        return games;
    }

    public void select(int position) {
        selectView(position, !selectedIds.get(position));
    }

    public void removeSelection() {
        selectedIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedIds.put(position, value);
        else
            selectedIds.delete(position);

        notifyDataSetChanged();
    }

    public void selectAllViews() {
        for(int i = 0; i < getCount(); i++) {
            selectedIds.put(i, true);
        }

        notifyDataSetChanged();
    }

    public void clearViews() {
        for(int i = 0; i < getCount(); i++) {
            selectedIds.put(i, false);
        }

        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedIds;
    }
}
