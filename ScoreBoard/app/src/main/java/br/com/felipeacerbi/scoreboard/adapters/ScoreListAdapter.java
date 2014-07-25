package br.com.felipeacerbi.scoreboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.scoreboard.R;
import br.com.felipeacerbi.scoreboard.models.Round;

/**
 * Created by felipe.acerbi on 03/07/2014.
 */
public class ScoreListAdapter extends ArrayAdapter<Round> {

    private List<Round> rounds;
    private Context context;

    public ScoreListAdapter(Context context, int resource, List<Round> rounds) {
        super(context, resource, rounds);

        this.context = context;
        this.rounds = rounds;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.round_item, parent, false);

        }

        TextView scoreTitle = (TextView) convertView.findViewById(R.id.round_score_title);
        TextView score1 = (TextView) convertView.findViewById(R.id.round_score1);
        TextView score2 = (TextView) convertView.findViewById(R.id.round_score2);
        TextView subScore1 = (TextView) convertView.findViewById(R.id.sub_score1);
        TextView subScore2 = (TextView) convertView.findViewById(R.id.sub_score2);

        Round round = rounds.get(position);

        scoreTitle.setText(round.getScoreTitle());
        score1.setText(String.valueOf(round.getScore1()));
        score2.setText(String.valueOf(round.getScore2()));
        subScore1.setText(String.valueOf(round.getSubScore1()));
        subScore2.setText(String.valueOf(round.getSubScore2()));

        return convertView;

    }
}
