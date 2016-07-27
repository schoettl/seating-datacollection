package edu.hm.cs.vadere.seating.datacollection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class CollectDataActivity extends AppCompatActivity {

    public static final String EXTRA_STATE_KEY = "da0a846ffbbf4436f239cc1b84af4d2a52c5d616";

    private Survey survey;
    private LogEventWriter logEventWriter;

    /**
     * State of the train. Currently, initialized with UNKNOWN.
     * TODO In future saved and restored (activity lifecycle?).
     */
    private TrainState trainState = TrainState.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdata);
        UiHelper.setToolbar(this);

        survey = Utils.getSurveyFromIntent(getIntent());
        SeatsState state = getStateFromIntent();
        logEventWriter = new LogEventWriter(survey);

        UiHelper.startAndReturnSeatsFragment(this, survey, state);
    }

    private SeatsState getStateFromIntent() {
        return (SeatsState) getIntent().getSerializableExtra(EXTRA_STATE_KEY);
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
                trainState = TrainState.DOORS_OPENED;
                logEventWriter.logTrainEvent(LogEventType.DOOR_RELEASE);
                invalidateOptionsMenu();
                return true;
            case R.id.action_train_starts:
                trainState = TrainState.DOORS_CLOSED;
                logEventWriter.logTrainEvent(LogEventType.TRAIN_STARTS);
                invalidateOptionsMenu();
                return true;
            case R.id.action_count_standing_persons:
                actionCountStandingPersons();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionCountStandingPersons() {
        final EditText editTextCount = new EditText(this);
        editTextCount.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_count_standing_persons);
        builder.setView(editTextCount);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int count = Integer.parseInt(editTextCount.getText().toString());
                    logEventWriter.logCountStandingPersons(count);
                } catch (NumberFormatException e) {
                    UiHelper.showErrorToast(CollectDataActivity.this, R.string.error_invalid_number_for_count);
                }
            }
        });
        UiHelper.setDefaultNegativeButton(builder);
        UiHelper.showAlertWithSoftKeyboard(builder);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
        return super.onPrepareOptionsMenu(menu);
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
