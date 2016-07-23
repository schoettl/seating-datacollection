package edu.hm.cs.vadere.seating.datacollection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.hm.cs.vadere.seating.datacollection.model.LogEvent;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class CollectDataActivity extends AppCompatActivity {

    private Survey survey;
    private LogEventWriter logEventWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdata);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        survey = Utils.getSurveyFromIntent(getIntent());
        logEventWriter = new LogEventWriter(survey);

        Utils.startSeatsFragment(this, logEventWriter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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

}
