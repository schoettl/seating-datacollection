package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsState;

public class InitCollectionActivity extends AppCompatActivity {

    private Survey survey;
    private LogEventWriter logEventWriter;
    private SeatsFragment seatsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initcollection);
        Utils.setToolbar(this);

        survey = Utils.getSurveyFromIntent(getIntent());
        logEventWriter = new LogEventWriter(survey);

        seatsFragment = Utils.startAndReturnSeatsFragment(this, survey, null);
        getSeatsFragment().getActionManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_init_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        // TODO check if agent is marked (but it should be optional - the agent may be standing)
        logEventWriter.logInitializationEnd();
        Intent intent = new Intent(this, CollectDataActivity.class);
        intent.putExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, survey.getId());
        SeatsState state = getSeatsFragment().getState();
        intent.putExtra(CollectDataActivity.EXTRA_STATE_KEY, state);
        startActivity(intent);
    }

    private SeatsFragment getSeatsFragment() {
        // FragmentManager#findFragmentById(int) does not work.
        // Neither with container id nor with fragment id.
        return seatsFragment;
    }

}
