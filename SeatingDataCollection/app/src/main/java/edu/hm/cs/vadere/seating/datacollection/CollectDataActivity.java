package edu.hm.cs.vadere.seating.datacollection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.hm.cs.vadere.seating.datacollection.actions.ActionManager;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class CollectDataActivity extends AppCompatActivity {

    public static final String EXTRA_STATE_KEY = "da0a846ffbbf4436f239cc1b84af4d2a52c5d616";

    private Survey survey;
    private SeatsFragment seatsFragment;

    /** State of the train. Currently, initialized with UNKNOWN. */
    private TrainState trainState = TrainState.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdata);
        UiHelper.setToolbar(this);

        survey = Utils.getSurveyFromIntent(getIntent());
        SeatsState state = getStateFromIntent();

        seatsFragment = UiHelper.createAndStartSeatsFragmentIfThisIsNoRecreation(this, savedInstanceState, survey, state);
    }

    private SeatsState getStateFromIntent() {
        return (SeatsState) getIntent().getSerializableExtra(EXTRA_STATE_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_collect_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionManager actionManager = seatsFragment.getActionManager();
        switch (item.getItemId()) {
            case R.id.action_direction_change:
                actionManager.actionDirectionChange();
                return true;
            case R.id.action_door_release:
                actionManager.actionDoorsReleased();
                trainState = TrainState.DOORS_OPENED;
                invalidateOptionsMenu();
                return true;
            case R.id.action_train_starts:
                actionManager.actionTrainStarts();
                trainState = TrainState.DOORS_CLOSED;
                invalidateOptionsMenu();
                return true;
            case R.id.action_count_standing_persons:
                actionManager.actionCountStandingPersons(this);
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
        super.onPrepareOptionsMenu(menu);
        // (To trigger this callback, call invalidateOptionsMenu())
        switch (trainState) {
            case DOORS_CLOSED:
                menu.removeItem(R.id.action_train_starts);
                break;
            case DOORS_OPENED:
                menu.removeItem(R.id.action_door_release);
                break;
            case UNKNOWN:
            default:
                break;
        }
        return true;
    }

    public void endDataCollection(View view) {
        UiHelper.showConfirmDialog(this, R.string.dialog_confirm_end_data_collection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CollectDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private enum TrainState {
        UNKNOWN, DOORS_CLOSED, DOORS_OPENED;
    }
}
