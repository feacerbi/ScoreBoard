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

import br.com.felipeacerbi.scoreboard.utils.ContextActionBar;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Player player = (Player) listView.getItemAtPosition(pos);

                Intent intent = new Intent(getActivity(), AddPlayerActivity.class);

                intent.putExtra("player", player);

                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                ContextActionBar contextActionBar = new ContextActionBar((MainScoreActivity) getActivity(), listView, pos);
                ((MainScoreActivity) getActivity()).startSupportActionMode(contextActionBar);

                return true;
            }
        });

        pdao = new PlayerDAO(getActivity());
        padapter = new PlayersListAdapter(getActivity(), pdao.listPlayers());

        listView.setAdapter(padapter);

        return playersList;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateList();
    }

    public void updateList() {

        pdao = new PlayerDAO(getActivity());

        PlayersListAdapter pla = new PlayersListAdapter(getActivity(), pdao.listPlayers());

        pdao.close();

        listView.setAdapter(pla);

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
