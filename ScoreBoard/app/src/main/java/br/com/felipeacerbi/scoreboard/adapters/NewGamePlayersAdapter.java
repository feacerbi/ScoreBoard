package br.com.felipeacerbi.scoreboard.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by felipe.acerbi on 08/07/2014.
 */
public class NewGamePlayersAdapter extends BaseAdapter {

    private Activity activity;
    private List<Player> players;
    private PlayerDAO playerDAO;

    public NewGamePlayersAdapter(Activity activity, List<Player> players) {
        this.players = players;
        this.activity = activity;
        playerDAO = new PlayerDAO(activity);
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

        ViewHolder vh;
        Player player;

        if(view == null) {

            player = players.get(pos);

            view = activity.getLayoutInflater().inflate(R.layout.activity_new_game_player, viewGroup, false);

            vh = new ViewHolder();

            vh.playerTitle = (TextView) view.findViewById(R.id.select_players_text);
            vh.playerType = (Spinner) view.findViewById(R.id.add_player_type);
            vh.playerName = (TextView) view.findViewById(R.id.add_player_name);
            vh.viewPosition = pos;
            vh.playerTitle.setText("Player " + (vh.viewPosition + 1));
            vh.player = player;

            view.setTag(vh);
            vh.playerType.setTag(vh);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.player_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        vh.playerType.setAdapter(adapter);

        vh.playerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                ViewHolder vh = (ViewHolder) adapterView.getTag();
                switch (pos) {
                    case 0:
                        vh.playerName.setText(vh.playerTitle.getText());
                        addNewPlayer(vh);
                        break;
                    case 1:
                        customPlayerNameDialog(vh);
                        break;
                    case 2:
                        getPlayerNameDialog(vh);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ViewHolder vh = (ViewHolder) adapterView.getTag();
                vh.playerType.setSelection(0);
                addNewPlayer(vh);
            }
        });

        } else {
            vh = (ViewHolder) view.getTag();
            vh.playerTitle.setText("Player " + (vh.viewPosition + 1));
        }

        return view;
    }

    public void getPlayerNameDialog(final ViewHolder vh) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.players_select_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Player");

        ListView lv = (ListView) convertView.findViewById(R.id.players_select_listview);
        lv.setEmptyView((TextView) convertView.findViewById(R.id.empty_select_text));
        final PlayersSelectListAdapter adapter = new PlayersSelectListAdapter(activity, playerDAO.listPlayers());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                vh.player = adapter.getPlayers().get(pos);
                view.setSelected(true);
            }
        });

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                vh.playerName.setText(vh.player.getName());

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                vh.playerType.setSelection(0);
                addNewPlayer(vh);
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                vh.playerType.setSelection(0);
                addNewPlayer(vh);
            }
        });
        alertDialog.show();

    }

    public void customPlayerNameDialog(final ViewHolder vh) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        final EditText newName = new EditText(activity);
        newName.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME);
        alertDialog.setView(newName);
        alertDialog.setTitle("Custom Name");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                vh.playerName.setText(newName.getText().toString());
                vh.player.setName(vh.playerName.getText().toString());
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                vh.playerType.setSelection(0);
                addNewPlayer(vh);
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                vh.playerType.setSelection(0);
                addNewPlayer(vh);
            }
        });
        alertDialog.show();

    }

    public void addNewPlayer(ViewHolder vh) {

        Player player = new Player();
        player.setName(vh.playerTitle.getText().toString());
        vh.player = player;

    }

    private class ViewHolder {
        TextView playerTitle;
        Spinner playerType;
        TextView playerName;
        int viewPosition;
        Player player;
    }
}
