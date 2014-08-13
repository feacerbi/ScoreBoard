package br.com.felipeacerbi.scoreboard.utils;

import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.activities.NewGameActivity;
import br.com.felipeacerbi.scoreboard.adapters.NewGamePlayersAdapter;
import br.com.felipeacerbi.scoreboard.models.Game;
import br.com.felipeacerbi.scoreboard.models.Player;

public class NewGameHelper {

    private NewGameActivity nga;
    private Spinner gameModes;
    private ListView newPlayersList;
    private EditText winScoreView;
    private List<Player> players;
    private int gameMode;
    private Spinner gameSkins;
    private NewGamePlayersAdapter adapter;
    private View headerView;
    private Game modifiedGame;
    private boolean isNew;

    public NewGameHelper(NewGameActivity nga) {

        this.nga = nga;

        players = new ArrayList<Player>();

        getInfo();

        getModify();

    }

    public void getInfo() {

        headerView = nga.getLayoutInflater().inflate(R.layout.activity_new_game_header, null);
        newPlayersList = (ListView) nga.findViewById(R.id.new_players_list);
        newPlayersList.addHeaderView(headerView);
        adapter = new NewGamePlayersAdapter(nga, players, true);
        newPlayersList.setAdapter(adapter);

        gameSkins = (Spinner) newPlayersList.findViewById(R.id.game_skins);
        gameModes = (Spinner) newPlayersList.findViewById(R.id.game_modes);
        winScoreView = (EditText) newPlayersList.findViewById(R.id.win_score);

        ArrayAdapter<CharSequence> numberAdapter = ArrayAdapter.createFromResource(nga,
                R.array.game_skins, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gameSkins.setAdapter(numberAdapter);

        ArrayAdapter<CharSequence> modesAdapter = ArrayAdapter.createFromResource(nga,
                R.array.game_modes, android.R.layout.simple_spinner_item);
        modesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gameModes.setAdapter(modesAdapter);

        gameModes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        gameMode = Game.GAME_MODE_1X1;
                        if(isNew) {
                            fillPlayers(gameMode);
                        }
                        break;
                    case 1:
                        if(isNew) {
                            gameMode = Game.GAME_MODE_2X2;
                            fillPlayers(gameMode);
                        }
                        break;
                    default:

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void getModify() {

        Game game = (Game) nga.getIntent().getSerializableExtra("game");

        isNew = true;

        if(game != null) {
            setGame(game, false);
        }
    }

    public void fillPlayers(int numberOfPlayers) {

        players = new ArrayList<Player>();

        for (int i = 0; i < numberOfPlayers; i++) {
            Player newPlayer = new Player();
            newPlayer.setName("Player " + (i + 1));
            players.add(newPlayer);
        }
        modifyPlayers(players, true);

    }

    public void modifyPlayers(List<Player> players, boolean isNew) {

        this.players = players;
        adapter = new NewGamePlayersAdapter(nga, players, isNew);
        newPlayersList.setAdapter(adapter);
        newPlayersList.setSelection(newPlayersList.getCount());

    }

    public Game getGame() {

        if(modifiedGame == null) {
            modifiedGame = new Game(gameMode);
        }
        for(int n = 0; n < gameMode; n++) {
            modifiedGame.getPlayersList().set(n, players.get(n));
        }
        modifiedGame.setWinScore(Integer.parseInt(winScoreView.getText().toString()));

        return modifiedGame;
    }

    public Game reload() {
        Game temp = new Game(gameMode);
        for(int n = 0; n < gameMode; n++) {
            temp.getPlayersList().set(n, players.get(n));
        }
        temp.setWinScore(Integer.parseInt(winScoreView.getText().toString()));

        return temp;
    }

    public void setGame(Game game, boolean isNew) {

        this.isNew = isNew;
        modifiedGame = game;
        switch(game.getGameMode()) {
            case Game.GAME_MODE_1X1:
                gameModes.setSelection(0);
                break;
            case Game.GAME_MODE_2X2:
                gameModes.setSelection(1);
                break;
        }
        gameModes.setEnabled(isNew);
        winScoreView.setText(String.valueOf(game.getWinScore()));
        modifyPlayers(game.getPlayersList(), isNew);

        if(!isNew) {
            ((TextView) nga.getSupportActionBar().getCustomView().findViewById(R.id.start_button)).setText("MODIFY");
        }
    }

    public NewGamePlayersAdapter getAdapter() {
        return adapter;
    }

    public boolean isGameNew() { return adapter.isNew(); }
}
