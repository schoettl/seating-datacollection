package edu.hm.cs.vadere.seating.datacollection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class CollectDataActivity extends AppCompatActivity {

    public static final String EXTRA_STATE_KEY = "da0a846ffbbf4436f239cc1b84af4d2a52c5d616";

    private Survey survey;
    private LogEventWriter logEventWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdata);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        survey = Utils.getSurveyFromIntent(getIntent());
        ArrayList<Seat> state = getStateFromIntent();
        logEventWriter = new LogEventWriter(survey);

        Utils.startAndReturnSeatsFragment(this, logEventWriter, state);
    }

    private ArrayList<Seat> getStateFromIntent() {
        // ArrayList because it must be serializable
        return (ArrayList<Seat>) getIntent().getSerializableExtra(EXTRA_STATE_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_direction_change:
                logEventWriter.logTrainEvent(LogEventType.DIRECTION_CHANGE);
                return true;
            case R.id.action_door_release:
                logEventWriter.logTrainEvent(LogEventType.DOOR_RELEASE);
                return true;
            case R.id.action_train_starts:
                logEventWriter.logTrainEvent(LogEventType.TRAIN_STARTS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // E.g. remove action_door_release when door is currently open.
        // To trigger this callback, call invalidateOptionsMenu()
        return super.onPrepareOptionsMenu(menu);
    }
}
