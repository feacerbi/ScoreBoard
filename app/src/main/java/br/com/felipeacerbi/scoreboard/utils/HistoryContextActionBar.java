package br.com.felipeacerbi.scoreboard.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.adapters.GamesListAdapter;
import br.com.felipeacerbi.scoreboard.fragments.GamesFragment;
import br.com.felipeacerbi.scoreboard.fragments.HistoryFragment;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.tasks.DeleteGamesTask;

/**
 * Created by Felipe on 06/07/2014.
 */
public class HistoryContextActionBar implements android.support.v7.view.ActionMode.Callback {

    private MainScoreActivity activity;
    private ListView list;
    private GamesListAdapter adapter;
    private HistoryFragment frag;

    public HistoryContextActionBar(MainScoreActivity activity, HistoryFragment frag, ListView list, int pos) {

        this.activity = activity;
        this.frag = frag;
        this.list = list;
        adapter = (GamesListAdapter) list.getAdapter();
        adapter.select(pos);
    }

    @Override
    public boolean onCreateActionMode(final android.support.v7.view.ActionMode actionMode, Menu menu) {

        actionMode.setTitle("Games");
        actionMode.setSubtitle(adapter.getSelectedIds().size() + " selected");
        

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                adapter.select(pos);
                actionMode.setSubtitle(adapter.getSelectedIds().size() + " selected");
            }
        });

        MenuItem clear = menu.add("Clear All");
        clear.setIcon(R.drawable.ic_menu_clear_holo_light);
        clear.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                adapter.clearViews();
                actionMode.setSubtitle(0 + " selected");
                return false;
            }
        });

        MenuItem selAll = menu.add("Select All");
        selAll.setIcon(R.drawable.ic_menu_selectall_holo_light);
        selAll.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        selAll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                adapter.selectAllViews();
                actionMode.setSubtitle(adapter.getSelectedIds().size() + " selected");
                return false;
            }
        });

        MenuItem remove = menu.add("Remove");
        remove.setIcon(R.drawable.ic_action_discard);
        remove.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        remove.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Remove Games")
                        .setMessage("Do you really want to remove the games?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                List<Game> games = new ArrayList<Game>();

                                SparseBooleanArray sba = adapter.getSelectedIds();
                                for(int i = 0; i < sba.size(); i++) {
                                    if(sba.valueAt(i)) {
                                        games.add((Game) list.getAdapter().getItem(sba.keyAt(i)));
                                    }
                                }

                                new DeleteGamesTask(activity, list).execute(games);

                                actionMode.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(android.support.v7.view.ActionMode actionMode) {
        list.setOnItemClickListener(frag.getLoadGamesListener());
        adapter.removeSelection();
    }
}
