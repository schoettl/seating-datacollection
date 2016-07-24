package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class InitCollectionActivity extends AppCompatActivity {

    private Survey survey;
    private LogEventWriter logEventWriter;
    private SeatsFragment seatsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initcollection);

        survey = Utils.getSurveyFromIntent(getIntent());
        logEventWriter = new LogEventWriter(survey);

        seatsFragment = Utils.startAndReturnSeatsFragment(this, logEventWriter, null);
        getSeatsFragment().getActionManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_init_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_mark_agent:
                SeatsFragment fragment = getSeatsFragment();
                fragment.getActionManager().actionMarkAgent(survey);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startDataCollection(View view) {
        // TODO check if agent is marked
        logEventWriter.logInitializationEnd();
        Intent intent = new Intent(this, CollectDataActivity.class);
        intent.putExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, survey.getId());
        ArrayList<Seat> state = getSeatsFragment().getState();
        intent.putExtra(CollectDataActivity.EXTRA_STATE_KEY, state);
        startActivity(intent);
    }

    private SeatsFragment getSeatsFragment() {
        // FragmentManager#findFragmentById(int) does not work.
        // Neither with container id nor with fragment id.
        return seatsFragment;
    }

}
