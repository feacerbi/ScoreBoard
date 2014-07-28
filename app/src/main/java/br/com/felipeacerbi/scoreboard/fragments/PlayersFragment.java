package br.com.felipeacerbi.scoreboard.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.felipeacerbi.scoreboard.tasks.ListPlayersTask;
import br.com.felipeacerbi.scoreboard.utils.PlayersContextActionBar;
import br.com.felipeacerbi.scoreboard.db.PlayerDAO;
import br.com.felipeacerbi.scoreboard.adapters.PlayersListAdapter;
import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.AddPlayerActivity;
import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.models.Player;

/**
 * Created by Felipe on 05/07/2014.
 */
public class PlayersFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView listView;
    private PlayerDAO pdao;
    private PlayersListAdapter padapter;
    private AdapterView.OnItemClickListener modifyPlayersListener;

    public static PlayersFragment newInstance(int sectionNumber) {
        PlayersFragment fragment = new PlayersFragment();
        fragment.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View playersList = inflater.inflate(R.layout.fragment_players, container, false);

        listView = (ListView) playersList.findViewById(R.id.players_listview);
        listView.setEmptyView(playersList.findViewById(R.id.empty_text));
        modifyPlayersListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Player player = (Player) listView.getItemAtPosition(pos);

                Intent intent = new Intent(getActivity(), AddPlayerActivity.class);
                intent.putExtra("player", player);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(modifyPlayersListener);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                PlayersContextActionBar playersContextActionBar = new PlayersContextActionBar((MainScoreActivity) getActivity(), PlayersFragment.this, listView, pos);
                ((MainScoreActivity) getActivity()).startSupportActionMode(playersContextActionBar);

                return true;
            }
        });

        return playersList;
    }

    public AdapterView.OnItemClickListener getModifyPlayersListener() {
        return modifyPlayersListener;
    }

    @Override
    public void onResume() {
        super.onResume();

        new ListPlayersTask((MainScoreActivity) getActivity(), listView).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainScoreActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_player:
                Intent addPlayer = new Intent(getActivity(), AddPlayerActivity.class);
                startActivity(addPlayer);
                return true;
            case R.id.action_save:

        }
        return false;
    }
}
