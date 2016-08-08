package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.hm.cs.vadere.seating.datacollection.actions.ActionManager;
import edu.hm.cs.vadere.seating.datacollection.actions.MarkAgentAction;
import edu.hm.cs.vadere.seating.datacollection.model.Direction;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class InitCollectionActivity extends AppCompatActivity implements PendingActionListener {

    private static final String TAG = "InitCollectionActivity";
    private Survey survey;
    private LogEventWriter logEventWriter;
    private SeatsFragment seatsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "entering activity's onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initcollection);
        UiHelper.setToolbar(this);

        survey = Utils.getSurveyFromIntent(this);
        logEventWriter = new LogEventWriter(survey);

        seatsFragment = UiHelper.createAndStartSeatsFragmentIfThisIsNoRecreation(this, savedInstanceState, survey, null, null);

        Log.d(TAG, "leaving activity's onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_init_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ActionManager actionManager = getSeatsFragment().getActionManager();
        switch (item.getItemId()) {
            case R.id.action_mark_agent:
                actionManager.actionMarkAgent(survey, this);
                invalidateOptionsMenu();
                return true;
            case R.id.action_undo:
                UiHelper.undoOrShowToast(actionManager);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SeatsFragment fragment = getSeatsFragment();
        MenuItem item = menu.findItem(R.id.action_mark_agent);
        if (fragment.getActionManager().isActionPending(MarkAgentAction.class)) {
            UiHelper.tintMenuItem(item, Color.RED);
        } else {
            UiHelper.tintMenuItem(item, Color.BLACK);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void startDataCollection(View view) {
        // TODO check if agent is marked (but it should be optional - the agent may be standing)
        logEventWriter.logInitializationEnd();
        Intent intent = new Intent(this, CollectDataActivity.class);
        intent.putExtra(StartSurveyActivity.EXTRA_SURVEY_ID, survey.getId());
        SeatsState state = getSeatsFragment().getCurrentState();
        Direction direction = getSeatsFragment().getDirection();
        intent.putExtra(CollectDataActivity.EXTRA_STATE, state);
        intent.putExtra(CollectDataActivity.EXTRA_DIRECTION, direction);
        startActivity(intent);
    }

    private SeatsFragment getSeatsFragment() {
        // FragmentManager#findFragmentById(int) does not work.
        // Neither with container id nor with fragment id.
        return seatsFragment;
    }

    @Override
    public void onPendingActionFinished(boolean wasCanceled) {
        invalidateOptionsMenu();
    }
}
