package br.com.felipeacerbi.scoreboard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.AddPlayerActivity;
import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.tasks.ListGamesTask;
import br.com.felipeacerbi.scoreboard.tasks.ListHistoryTask;
import br.com.felipeacerbi.scoreboard.utils.GamesContextActionBar;
import br.com.felipeacerbi.scoreboard.utils.HistoryContextActionBar;

/**
 * Created by Felipe on 05/07/2014.
 */
public class HistoryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView listView;
    private AdapterView.OnItemClickListener loadGamesListener;

    public static HistoryFragment newInstance(int sectionNumber) {
        HistoryFragment fragment = new HistoryFragment();
        fragment.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View gamesList = inflater.inflate(R.layout.fragment_games, container, false);

        listView = (ListView) gamesList.findViewById(R.id.games_listview);
        TextView emptyView = (TextView )gamesList.findViewById(R.id.empty_text);
        emptyView.setText("No games finished on the Current Game section.");
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(loadGamesListener);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                HistoryContextActionBar historyContextActionBar = new HistoryContextActionBar((MainScoreActivity) getActivity(), HistoryFragment.this, listView, pos);
                ((MainScoreActivity) getActivity()).startSupportActionMode(historyContextActionBar);

                return true;
            }
        });

        return gamesList;
    }

    public AdapterView.OnItemClickListener getLoadGamesListener() {
        return loadGamesListener;
    }

    @Override
    public void onResume() {
        super.onResume();

        new ListHistoryTask((MainScoreActivity) getActivity(), listView).execute();
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
                return false;
            case R.id.action_save:

        }
        return false;
    }


}
