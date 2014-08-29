package br.com.felipeacerbi.scoreboard.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by Felipe on 05/07/2014.
 */
public class DrawerListAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private String[] sections;
    private int selected = 0;

    public DrawerListAdapter(Activity activity, int resource, int textViewResourceId, String[] sections) {
        super(activity, resource, textViewResourceId, sections);

        this.activity = activity;
        this.sections = sections;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        View drawerItem;

        if(view == null) {
            drawerItem = activity.getLayoutInflater().inflate(R.layout.drawer_item, viewGroup, false);
        } else {
            drawerItem = view;
        }

        final ImageView pic = (ImageView) drawerItem.findViewById(R.id.drawer_icon);
        TextView name = (TextView) drawerItem.findViewById(R.id.drawer_text);

        switch(pos) {
            case 0:
                pic.setImageResource(R.drawable.ic_current_game);
                break;
            case 1:
                pic.setImageResource(R.drawable.ic_saved_games);
                break;
            case 2:
                pic.setImageResource(R.drawable.ic_action_group);
                break;
            case 3:
                pic.setImageResource(R.drawable.ic_history);
                break;
        }

        name.setText(sections[pos]);

        if(selected == pos) {
            name.setTypeface(null, Typeface.BOLD);
        } else {
            name.setTypeface(null, Typeface.NORMAL);
        }

        return drawerItem;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
