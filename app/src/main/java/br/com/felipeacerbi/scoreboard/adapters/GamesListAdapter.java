package br.com.felipeacerbi.scoreboard.adapters;

import android.app.Activity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.models.Game;

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

        View gameItem = view;
        Game game = games.get(pos);

        switch(game.getGameMode()) {
            case Game.GAME_MODE_1X1:
                if(view == null) {
                    gameItem = activity.getLayoutInflater().inflate(R.layout.game_item_1x1, null);
                }
                break;
            case Game.GAME_MODE_2X2:
                if(view == null) {
                    gameItem = activity.getLayoutInflater().inflate(R.layout.game_item_2x2, null);
                }
                TextView name3 = (TextView) gameItem.findViewById(R.id.name_3);
                TextView name4 = (TextView) gameItem.findViewById(R.id.name_4);
                name3.setText(game.getPlayer(2).getName());
                name4.setText(game.getPlayer(3).getName());
                break;
        }

        TextView name1 = (TextView) gameItem.findViewById(R.id.name_1);
        TextView name2 = (TextView) gameItem.findViewById(R.id.name_2);
        TextView round = (TextView) gameItem.findViewById(R.id.round_number);
        TextView score1 = (TextView) gameItem.findViewById(R.id.score_1);
        TextView score2 = (TextView) gameItem.findViewById(R.id.score_2);
        TextView winscore = (TextView) gameItem.findViewById(R.id.winscore);

        Log.i("GLA", name1.getText().toString());
        Log.i("GLA", game.toString());
        Log.i("GLA", game.getPlayer(0).getName());
        name1.setText(game.getPlayer(0).getName());
        name2.setText(game.getPlayer(1).getName());
        round.setText("Round " + (game.getRounds() + 1));
        score1.setText(String.valueOf(game.getTotalScore(0).getValue()));
        score2.setText(String.valueOf(game.getTotalScore(1).getValue()));
        winscore.setText(String.valueOf(game.getWinScore()));

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
            selectedIds.delete(i);
        }

        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedIds;
    }
}
