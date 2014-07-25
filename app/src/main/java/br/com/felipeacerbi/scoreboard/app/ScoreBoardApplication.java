package br.com.felipeacerbi.scoreboard.app;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe.acerbi on 11/07/2014.
 */
public class ScoreBoardApplication extends Application {

    private List<AsyncTask<?, ?, ?>> tasks;

    @Override
    public void onCreate() {
        super.onCreate();

        tasks = new ArrayList<AsyncTask<?, ?, ?>>();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        for(AsyncTask task : tasks) {
            task.cancel(true);
        }
    }

    public void register(AsyncTask<?, ?, ?> task) {
        tasks.add(task);
    }

    public void unregister(AsyncTask<?, ?, ?> task) {
        tasks.remove(task);
    }
}
