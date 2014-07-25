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
import br.com.felipeacerbi.scoreboard.models.Round;

public class NewGameHelper {

    private NewGameActivity nga;
    private Spinner gameModes;
    private Spinner numberOfPlayersSelector;
    private ListView newPlayersList;
    private EditText winScoreView;
    private long id = 0;
    private List<Player> players;
    private NewGamePlayersAdapter adapter;

    public NewGameHelper(NewGameActivity nga) {

        this.nga = nga;

        players = new ArrayList<Player>();

        getInfo();

    }

    public void getInfo() {

        newPlayersList = (ListView) nga.findViewById(R.id.new_players_list);
        adapter = new NewGamePlayersAdapter(nga, players);
        newPlayersList.addHeaderView(nga.getLayoutInflater().inflate(R.layout.activity_new_game_header, null));
        newPlayersList.setAdapter(adapter);

        gameModes = (Spinner) newPlayersList.findViewById(R.id.game_modes);
        numberOfPlayersSelector = (Spinner) newPlayersList.findViewById(R.id.number_players);
        winScoreView = (EditText) newPlayersList.findViewById(R.id.win_score);

        ArrayAdapter<CharSequence> numberAdapter = ArrayAdapter.createFromResource(nga,
                R.array.number_of_players, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        numberOfPlayersSelector.setAdapter(numberAdapter);

        ArrayAdapter<CharSequence> modesAdapter = ArrayAdapter.createFromResource(nga,
                R.array.game_modes, android.R.layout.simple_spinner_item);
        modesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gameModes.setAdapter(modesAdapter);

        numberOfPlayersSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        for (int i = 0; i < 2; i++) {
                            Player newPlayer = new Player();
                            newPlayer.setName("Player " + (i + 1));
                            players.add(newPlayer);
                        }
                        adapter = new NewGamePlayersAdapter(nga, players);
                        newPlayersList.setAdapter(adapter);

                        break;
                    default:

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public Game getGame() {

        Game game = new Game();
        game.setPlayer1((Player) adapter.getItem(0));
        game.setPlayer2((Player) adapter.getItem(1));
        game.setTotalScore1(0);
        game.setTotalScore2(0);
        game.setWinScore(3000);
        game.setRounds(new ArrayList<Round>());

        return game;

    }

}
