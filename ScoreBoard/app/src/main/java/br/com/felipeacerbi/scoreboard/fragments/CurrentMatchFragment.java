package br.com.felipeacerbi.scoreboard.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.AddPlayerActivity;
import br.com.felipeacerbi.scoreboard.activities.NewGameActivity;
import br.com.felipeacerbi.scoreboard.adapters.ScoreListAdapter;
import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Round;

/**
 * Created by felipe.acerbi on 07/07/2014.
 */
public class CurrentMatchFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int NEW_GAME = 101;

    private TextView nameBox1;
    private TextView nameBox2;
    private TextView score2;
    private TextView score1;
    private ListView scoreList;
    private EditText inputScore2;
    private EditText inputScore1;
    private Button addButton;
    private TextView roundNumber;
    private ScoreListAdapter sla;
    private Game game;
    private TextView emptyList;

    public CurrentMatchFragment() {}

    public static CurrentMatchFragment newInstance(int sectionNumber) {
        CurrentMatchFragment fragment = new CurrentMatchFragment();
        fragment.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void addRound() {

        Round round = new Round();

        round.setScoreTitle("Round " + String.valueOf(game.getRounds() + 1) + " Score");
        round.setScore1(Integer.parseInt(inputScore1.getText().toString()));
        round.setScore2(Integer.parseInt(inputScore2.getText().toString()));
        round.setSubScore1(game.getTotalScore1() + round.getScore1());
        round.setSubScore2(game.getTotalScore2() + round.getScore2());

        game.addRound(round);

        sla.notifyDataSetChanged();

        updateScore();

    }

    public void updateScore() {

        nameBox1.setText(game.getPlayer1().getName());
        nameBox2.setText(game.getPlayer2().getName());
        score1.setText(String.valueOf(game.getTotalScore1()));
        score2.setText(String.valueOf(game.getTotalScore2()));
        roundNumber.setText("Round " + (game.getRounds() + 1));
        scoreList.smoothScrollToPosition(game.getRounds());

    }

    public void demo() {

        for(int i = 0; i < 20; i++) {
            Round round = new Round();
            round.setScore1(1000);
            round.setScore2(2000);
            round.setScore1(1500);
            round.setSubScore2(3000);
            game.addRound(round);
        }

        sla.notifyDataSetChanged();
        updateScore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_match, container, false);

        nameBox1 = (TextView) rootView.findViewById(R.id.name_1);
        nameBox2 = (TextView) rootView.findViewById(R.id.name_2);
        score1 = (TextView) rootView.findViewById(R.id.score_1);
        score2 = (TextView) rootView.findViewById(R.id.score_2);
        scoreList = (ListView) rootView.findViewById(R.id.score_list);
        inputScore1 = (EditText) rootView.findViewById(R.id.new_score1);
        inputScore2 = (EditText) rootView.findViewById(R.id.new_score2);
        addButton = (Button) rootView.findViewById(R.id.add_score);
        roundNumber = (TextView) rootView.findViewById(R.id.round_number);
        emptyList = (TextView) rootView.findViewById(R.id.empty_text);

        game = new Game();
        sla = new ScoreListAdapter(getActivity(), R.layout.round_item, game.getRoundsList());
        scoreList.setEmptyView(emptyList);
        scoreList.setAdapter(sla);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRound();
            }
        });

        disable();

        return rootView;
    }

    public void disable() {
        emptyList.setText("Start a new game on the above menu.");
        inputScore1.setEnabled(false);
        inputScore2.setEnabled(false);
        addButton.setEnabled(false);
    }

    public void enable() {
        emptyList.setText("Add a round score on the bar below.");
        inputScore1.setEnabled(true);
        inputScore2.setEnabled(true);
        addButton.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(""+requestCode + " " + NEW_GAME, ""+resultCode + " " + Activity.RESULT_OK);
        if(requestCode == NEW_GAME && resultCode == Activity.RESULT_OK) {
            game = (Game) data.getExtras().get("game");
            enable();
        }
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
                return false;
            case R.id.action_new:
                Intent newGame = new Intent(getActivity(), NewGameActivity.class);
                startActivityForResult(newGame, NEW_GAME);
                return  true;
        }

        return false;
    }
}
