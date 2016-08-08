package edu.hm.cs.vadere.seating.datacollection;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.hm.cs.vadere.seating.datacollection.actions.ActionManager;
import edu.hm.cs.vadere.seating.datacollection.model.Direction;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.model.TrainState;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class CollectDataActivity extends AppCompatActivity implements TrainState.TrainStateListener {

    private static final String TAG = "CollectDataActivity";
    public static final String EXTRA_STATE = "da0a846ffbbf4436f239cc1b84af4d2a52c5d616";
    public static final String EXTRA_DIRECTION = "fd2d3ed50627a5804fc241bf5f07005b30e41c6f";

    private Survey survey;
    private SeatsFragment seatsFragment;

    /** State of the train. */
    private TrainState trainState = TrainState.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdata);
        UiHelper.setToolbar(this);

        survey = Utils.getSurveyFromIntent(getIntent());
        final SeatsState state = getStateFromIntent();
        final Direction direction = getDirectionFromIntent();

        seatsFragment = UiHelper.createAndStartSeatsFragmentIfThisIsNoRecreation(this, savedInstanceState, survey, state, direction);
    }

    private SeatsState getStateFromIntent() {
        return (SeatsState) getIntent().getSerializableExtra(EXTRA_STATE);
    }

    private Direction getDirectionFromIntent() {
        return (Direction) getIntent().getSerializableExtra(EXTRA_DIRECTION);
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
            case R.id.action_door_release:
                actionManager.actionDoorsReleased(this);
                return true;
            case R.id.action_train_starts:
                actionManager.actionTrainStarts(this);
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
            case HALTING:
                Log.d(TAG, "prepare options menu after door release");
                menu.removeItem(R.id.action_door_release);
                final MenuItem trainStartsItem = menu.findItem(R.id.action_train_starts);
                UiHelper.tintMenuItem(trainStartsItem, Color.RED); // does not work somehow
                break;
            case MOVING:
                menu.removeItem(R.id.action_train_starts);
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

    @Override
    public void onTrainStateChanged(TrainState newState) {
        trainState = newState;
        invalidateOptionsMenu();
    }

}
