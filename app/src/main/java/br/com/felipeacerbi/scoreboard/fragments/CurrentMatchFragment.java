package br.com.felipeacerbi.scoreboard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    private View rootView;
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
    private TextView winscore;
    private View addBar;
    private ImageView shadow;
    private View scoreCard;

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

            Round round = new Round(getGame().getGameMode());

            round.setScoreTitle("Round " + String.valueOf(getGame().getRounds() + 1) + " Score");
            round.getScore(0).setValue(Integer.parseInt(inputScore1.getText().toString()));
            round.getScore(1).setValue(Integer.parseInt(inputScore2.getText().toString()));
            round.getSubScore(0).setValue(getGame().getTotalScore(0).getValue() + round.getScore(0).getValue());
            round.getSubScore(1).setValue(getGame().getTotalScore(1).getValue() + round.getScore(1).getValue());
            round.setGame(getGame());

            getGame().addRound(round);

            sla.notifyDataSetChanged();

            if(updateScore()) {
                new SaveGameTask((MainScoreActivity) getActivity()).execute(getGame());
            }
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

    public boolean updateScore() {

        switch(getGame().getGameMode()) {
            case Game.GAME_MODE_1X1:
                break;
            case Game.GAME_MODE_2X2:
                nameBox3.setText(getGame().getPlayer(2).getName());
                nameBox4.setText(getGame().getPlayer(3).getName());
                break;
        }

        nameBox1.setText(getGame().getPlayer(0).getName());
        nameBox2.setText(getGame().getPlayer(1).getName());
        score1.setText(String.valueOf(getGame().getTotalScore(0).getValue()));
        score2.setText(String.valueOf(getGame().getTotalScore(1).getValue()));
        roundNumber.setText("Round " + (getGame().getRounds() + 1));
        winscore.setText(String.valueOf(getGame().getWinScore()));
        scoreList.setSelection(scoreList.getCount());

        return checkWinner();

    }

    public boolean checkWinScore(List<Score> totalScores) {

        int winScore = getGame().getWinScore();

        for(Score totalScore : totalScores) {
            if(totalScore.getValue() >= winScore) return true;
        }

        return false;

    }

    public boolean checkWinner() {

        boolean up = true;

        List<Player> tempPlayers = getGame().getPlayersList();
        List<Score> totalScores = getGame().getTotalScoresList();
        List<Player> winnerList = new ArrayList<Player>();

        switch (getGame().getGameMode()) {
            case Game.GAME_MODE_1X1:
                if(checkWinScore(totalScores)) {
                    up = false;
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
                    up = false;
                    if(totalScores.get(0).getValue() > totalScores.get(1).getValue()) {
                        winnerList.add(tempPlayers.get(0));
                        winnerList.add(tempPlayers.get(1));
                        setWinner(winnerList, 100, false);
                    } else if(totalScores.get(1).getValue() > totalScores.get(0).getValue()) {
                        winnerList.add(tempPlayers.get(2));
                        winnerList.add(tempPlayers.get(3));
                        setWinner(winnerList, 100, false);
                    } else {
                        setWinner(tempPlayers, 50, true);
                    }

                }
                break;
        }

        return up;
    }

    public void setWinner(List<Player> players, int points, boolean isDraw) {

        String names = "";

        scoreList.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.winner_footer, scoreList, false));
        sla = new ScoreListAdapter(getActivity(), R.layout.round_item, getGame().getRoundsList());
        scoreList.setAdapter(sla);

        for(Player player : players) {
            new AddPointsTask((MainScoreActivity) getActivity(), points).execute(player);
            if(names.isEmpty())
                names = player.getName();
            else {
                names += "\n" + player.getName();
            }
        }

        TextView pointsText = (TextView) scoreList.findViewById(R.id.points);
        pointsText.setText(String.valueOf(points));

        TextView winnerName = (TextView) scoreList.findViewById(R.id.winner_name);
        winnerName.setText(isDraw ? "Draw" : names);

        scoreList.setSelection(scoreList.getCount());

        List<Game> games = new ArrayList<Game>();
        games.add(getGame());

        // TODO History Games
        new DeleteGamesTask((MainScoreActivity) getActivity(), null).execute(games);

        disable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = setLayout(Game.GAME_MODE_1X1);
        disable();

        if(getGame() != null) {
            rootView = setLayout(getGame().getGameMode());
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getGame() != null) {
            sla = new ScoreListAdapter(getActivity(), R.layout.round_item, getGame().getRoundsList());
            scoreList.setAdapter(sla);
            enable();
            updateScore();
        }
    }

    public void disable() {
        addBar.setVisibility(View.INVISIBLE);
        ((RelativeLayout) addBar.getParent()).removeView(addBar);

        scoreCard.setEnabled(false);
        shadow.setVisibility(View.INVISIBLE);

        emptyList.setText("Start a new game by clicking \nhere or on the above menu.");
        ((RelativeLayout.LayoutParams)emptyList.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        emptyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGame = new Intent(getActivity(), NewGameActivity.class);
                startActivityForResult(newGame, NEW_GAME);
            }
        });

        scoreCard.setOnClickListener(null);
    }

    public void enable() {
        addBar.setVisibility(View.VISIBLE);
        ((RelativeLayout) rootView).removeView(addBar);
        ((RelativeLayout) rootView).addView(addBar);

        scoreCard.setEnabled(true);
        shadow.setVisibility(View.VISIBLE);

        emptyList.setText("Add a round score on the bar below.");
        ((RelativeLayout.LayoutParams)emptyList.getLayoutParams()).addRule(RelativeLayout.ABOVE, R.id.add_bar);
        emptyList.setOnClickListener(null);

        scoreCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGame = new Intent(getActivity(), NewGameActivity.class);
                newGame.putExtra("game", getGame());
                startActivityForResult(newGame, NEW_GAME);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CurrentMatchFragment.NEW_GAME && resultCode == Activity.RESULT_OK) {
            ((MainScoreActivity) getActivity()).setGame((Game) data.getExtras().get("game"));
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CurrentMatchFragment.newInstance(1))
                    .commit();
            new SaveGameTask((MainScoreActivity) getActivity()).execute(getGame());
        }
    }

    public Game getGame() {
        return ((MainScoreActivity) getActivity()).getGame();
    }

    private View setLayout(int gameMode) {

        switch (gameMode) {
            case Game.GAME_MODE_1X1:
                rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_current_game_1x1, null);

                getViews();

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!inputScore1.getText().toString().isEmpty() && !inputScore2.getText().toString().isEmpty()) {
                            addRound();
                        } else {
                            Toast.makeText(getActivity(), "Input all scores", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Game.GAME_MODE_2X2:
                rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_current_game_2x2, null);

                getViews();

                nameBox3 = (TextView) rootView.findViewById(R.id.name_3);
                nameBox4 = (TextView) rootView.findViewById(R.id.name_4);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!inputScore1.getText().toString().isEmpty() && !inputScore2.getText().toString().isEmpty()) {
                            addRound();
                        } else {
                            Toast.makeText(getActivity(), "Input all scores", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

        return rootView;

    }

    public void getViews() {

        addButton = (LinearLayout) rootView.findViewById(R.id.add_score);
        inputScore1 = (EditText) rootView.findViewById(R.id.new_score1);
        inputScore2 = (EditText) rootView.findViewById(R.id.new_score2);
        nameBox1 = (TextView) rootView.findViewById(R.id.name_1);
        nameBox2 = (TextView) rootView.findViewById(R.id.name_2);
        score1 = (TextView) rootView.findViewById(R.id.score_1);
        score2 = (TextView) rootView.findViewById(R.id.score_2);
        scoreList = (ListView) rootView.findViewById(R.id.score_list);
        roundNumber = (TextView) rootView.findViewById(R.id.round_number);
        winscore = (TextView) rootView.findViewById(R.id.winscore);
        emptyList = (TextView) rootView.findViewById(R.id.empty_text);
        shadow = (ImageView) rootView.findViewById(R.id.add_bar_shadow);
        addBar = rootView.findViewById(R.id.add_bar);
        scoreCard = rootView.findViewById(R.id.score_card);

        scoreList.setEmptyView(emptyList);

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
                if(addBar.getVisibility() != View.INVISIBLE) {
                    removeRound();
                } else {
                    Toast.makeText(getActivity(), "Start a new game", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return false;
    }
}
