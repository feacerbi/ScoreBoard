package br.com.felipeacerbi.scoreboard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.NewGameActivity;
import br.com.felipeacerbi.scoreboard.adapters.ScoreListAdapter;
import br.com.felipeacerbi.scoreboard.activities.MainScoreActivity;
import br.com.felipeacerbi.scoreboard.db.GameDAO;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;
import br.com.felipeacerbi.scoreboard.models.Round;
import br.com.felipeacerbi.scoreboard.models.Score;
import br.com.felipeacerbi.scoreboard.tasks.AddPointsTask;
import br.com.felipeacerbi.scoreboard.tasks.DeleteGamesTask;
import br.com.felipeacerbi.scoreboard.tasks.SaveGameTask;

/**
 * Created by felipe.acerbi on 07/07/2014.
 */
public class CurrentMatchFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int NEW_GAME = 101;

    private TextView nameBox1;
    private TextView nameBox2;
    private TextView nameBox3;
    private TextView nameBox4;
    private TextView score2;
    private TextView score1;
    private ListView scoreList;
    private EditText inputScore2;
    private EditText inputScore1;
    private LinearLayout addButton;
    private TextView roundNumber;
    private ScoreListAdapter sla;
    private TextView emptyList;
    private GameDAO gameDAO;
    private TextView winscore;
    private View addBar;

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

        round.setScoreTitle("Round " + String.valueOf(getGame().getRounds() + 1) + " Score");
        round.setScore(0, new Score(Integer.parseInt(inputScore1.getText().toString())));
        round.setScore(1, new Score(Integer.parseInt(inputScore2.getText().toString())));
        round.setSubScore(0, new Score(getGame().getTotalScore(0).getValue() + round.getScore(0).getValue()));
        round.setSubScore(1, new Score(getGame().getTotalScore(1).getValue() + round.getScore(1).getValue()));
        round.setGame(getGame());

        getGame().addRound(round);

        sla.notifyDataSetChanged();

        updateScore();

        new SaveGameTask((MainScoreActivity) getActivity()).execute(getGame());

    }

    public void removeRound() {

        if(getGame() != null && getGame().getRounds() > 0) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Undo Round")
                        .setMessage("Do you really want to undo last Round?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                Round last = getGame().getRoundsList().get(getGame().getRounds() - 1);
                                getGame().removeRound(last);

                                sla.notifyDataSetChanged();

                                updateScore();

                                new SaveGameTask((MainScoreActivity) getActivity()).execute(getGame());

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

        } else {
            Toast.makeText(getActivity(), "No Rounds to undo", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateScore() {

        switch(getGame().getGameMode()) {
            case Game.GAME_MODE_1X1:
                break;
            case Game.GAME_MODE_2X2:
                nameBox3.setText(getGame().getPlayer(3).getName());
                nameBox4.setText(getGame().getPlayer(4).getName());
                break;
        }

        nameBox1.setText(getGame().getPlayer(0).getName());
        nameBox2.setText(getGame().getPlayer(1).getName());
        score1.setText(String.valueOf(getGame().getTotalScore(0)));
        score2.setText(String.valueOf(getGame().getTotalScore(1)));
        roundNumber.setText("Round " + (getGame().getRounds() + 1));
        winscore.setText(String.valueOf(getGame().getWinScore()));
        scoreList.smoothScrollToPosition(getGame().getRounds());

        checkWinner();

    }

    public boolean checkWinScore(List<Score> totalScores) {

        int winScore = getGame().getWinScore();

        for(Score totalScore : totalScores) {
            if(totalScore.getValue() >= winScore) return true;
        }

        return false;

    }

    public void checkWinner() {

        List<Player> tempPlayers = getGame().getPlayersList();
        List<Score> totalScores = getGame().getTotalScoresList();
        List<Player> winnerList = new ArrayList<Player>();

        switch (getGame().getGameMode()) {
            case Game.GAME_MODE_1X1:
                if(checkWinScore(totalScores)) {
                    if(totalScores.get(0).getValue() > totalScores.get(1).getValue()) {
                        winnerList.add(tempPlayers.get(0));
                        setWinner(winnerList, 100, false);
                    } else if(totalScores.get(1).getValue() > totalScores.get(0).getValue()) {
                        winnerList.add(tempPlayers.get(1));
                        setWinner(winnerList, 100, false);
                    } else {
                        setWinner(tempPlayers, 50, true);
                    }

                }
                break;
            case Game.GAME_MODE_2X2:
                if(checkWinScore(totalScores)) {
                    if(totalScores.get(0).getValue() > totalScores.get(1).getValue()) {
                        winnerList.add(tempPlayers.get(0));
                        winnerList.add(tempPlayers.get(1));
                        setWinner(winnerList, 100, false);
                    } else if(totalScores.get(1).getValue() > totalScores.get(0).getValue()) {
                        winnerList.add(tempPlayers.get(3));
                        winnerList.add(tempPlayers.get(4));
                        setWinner(winnerList, 100, false);
                    } else {
                        setWinner(tempPlayers, 50, true);
                    }

                }
                break;
        }
    }

    public void setWinner(List<Player> players, int points, boolean isDraw) {

        String names = "";

        scoreList.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.winner_footer, scoreList, false));
        sla = new ScoreListAdapter(getActivity(), R.layout.round_item, getGame().getRoundsList());
        scoreList.setAdapter(sla);

        for(Player player : players) {
            new AddPointsTask((MainScoreActivity) getActivity(), points).execute(player);
            names += player.getName() + "\n";
        }

        TextView pointsText = (TextView) scoreList.findViewById(R.id.points);
        pointsText.setText(String.valueOf(points));

        TextView winnerName = (TextView) scoreList.findViewById(R.id.winner_name);
        winnerName.setText(isDraw ? "Draw" : names);

        List<Game> games = new ArrayList<Game>();
        games.add(getGame());

        new DeleteGamesTask((MainScoreActivity) getActivity(), null).execute(games);

        disable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = setLayout(Game.GAME_MODE_1X1);

        disable();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getGame() != null) {
            sla = new ScoreListAdapter(getActivity(), R.layout.round_item, getGame().getRoundsList());
            scoreList.setAdapter(sla);
            updateScore();
            enable();
        }
    }

    public void disable() {
        addBar.setVisibility(View.INVISIBLE);
        emptyList.setText("Start a new game on the above menu.");
        inputScore1.setEnabled(false);
        inputScore1.setHintTextColor(Color.LTGRAY);
        inputScore1.setTextColor(Color.LTGRAY);
        inputScore2.setEnabled(false);
        inputScore2.setHintTextColor(Color.LTGRAY);
        inputScore2.setTextColor(Color.LTGRAY);
        addButton.setEnabled(false);
    }

    public void enable() {
        addBar.setVisibility(View.VISIBLE);
        emptyList.setText("Add a round score on the bar below.");
        inputScore1.setEnabled(true);
        inputScore1.setHintTextColor(Color.WHITE);
        inputScore1.setTextColor(Color.WHITE);
        inputScore2.setEnabled(true);
        inputScore2.setHintTextColor(Color.WHITE);
        inputScore2.setTextColor(Color.WHITE);
        addButton.setEnabled(true);
    }

    public void createGame() {

        sla = new ScoreListAdapter(getActivity(), R.layout.round_item, getGame().getRoundsList());
        scoreList.setAdapter(sla);

        new SaveGameTask((MainScoreActivity) getActivity()).execute(getGame());

        updateScore();
        enable();

    }

    public Game getGame() {
        return ((MainScoreActivity) getActivity()).getGame();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_GAME && resultCode == Activity.RESULT_OK) {
            ((MainScoreActivity) getActivity()).setGame((Game) data.getExtras().get("game"));
            setLayout(getGame().getGameMode());
            createGame();

        }
    }

    private View setLayout(int gameMode) {

        View rootView = null;

        switch (gameMode) {
            case Game.GAME_MODE_1X1:
                rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_current_game_1x1, null);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(inputScore1.getText() != null && inputScore2.getText() != null) {
                                addRound();
                        } else {
                            Toast.makeText(getActivity(), "Input all scores", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Game.GAME_MODE_2X2:
                rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_current_game_2x2, null);

                nameBox3 = (TextView) rootView.findViewById(R.id.name_3);
                nameBox4 = (TextView) rootView.findViewById(R.id.name_4);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(inputScore1.getText() != null && inputScore2.getText() != null) {
                            addRound();
                        } else {
                            Toast.makeText(getActivity(), "Input all scores", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

        nameBox1 = (TextView) rootView.findViewById(R.id.name_1);
        nameBox2 = (TextView) rootView.findViewById(R.id.name_2);
        score1 = (TextView) rootView.findViewById(R.id.score_1);
        score2 = (TextView) rootView.findViewById(R.id.score_2);
        scoreList = (ListView) rootView.findViewById(R.id.score_list);
        inputScore1 = (EditText) rootView.findViewById(R.id.new_score1);
        inputScore2 = (EditText) rootView.findViewById(R.id.new_score2);
        addButton = (LinearLayout) rootView.findViewById(R.id.add_score);
        roundNumber = (TextView) rootView.findViewById(R.id.round_number);
        winscore = (TextView) rootView.findViewById(R.id.winscore);
        emptyList = (TextView) rootView.findViewById(R.id.empty_text);
        addBar = rootView.findViewById(R.id.add_bar);

        scoreList.setEmptyView(emptyList);

        return rootView;

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
            case R.id.action_undo:
                removeRound();
                return true;
        }

        return false;
    }
}
