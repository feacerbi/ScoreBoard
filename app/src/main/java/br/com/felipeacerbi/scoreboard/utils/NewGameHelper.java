package br.com.felipeacerbi.scoreboard.utils;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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

    public NewGameHelper(NewGameActivity nga) {

        this.nga = nga;

        players = new ArrayList<Player>();

        getInfo();
        getModify();

    }

    public void getInfo() {

        newPlayersList = (ListView) nga.findViewById(R.id.new_players_list);
        newPlayersList.addHeaderView(nga.getLayoutInflater().inflate(R.layout.activity_new_game_header, null));
        adapter = new NewGamePlayersAdapter(nga, players);
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
                        fillPlayers(gameMode);
                        break;
                    case 1:
                        gameMode = Game.GAME_MODE_2X2;
                        fillPlayers(gameMode);
                        break;
                    default:

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getModify() {
        // TODO Edit Game information
    }


    public void fillPlayers(int numberOfPlayers) {

        players = new ArrayList<Player>();

        for (int i = 0; i < numberOfPlayers; i++) {
            Player newPlayer = new Player();
            newPlayer.setName("Player " + (i + 1));
            players.add(newPlayer);
        }
        adapter = new NewGamePlayersAdapter(nga, players);
        newPlayersList.setAdapter(adapter);
        newPlayersList.smoothScrollToPosition(numberOfPlayers - 1);

    }

    public Game getGame() {

        Game game = new Game(gameMode);
        for(int n = 0; n < gameMode; n++) {
            game.getPlayersList().set(n, players.get(n));
        }
        game.setWinScore(Integer.parseInt(winScoreView.getText().toString()));

        return game;

    }

}
