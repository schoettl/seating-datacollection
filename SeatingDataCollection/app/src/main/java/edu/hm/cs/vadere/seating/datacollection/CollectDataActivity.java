package edu.hm.cs.vadere.seating.datacollection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsState;

public class CollectDataActivity extends AppCompatActivity {

    public static final String EXTRA_STATE_KEY = "da0a846ffbbf4436f239cc1b84af4d2a52c5d616";

    private Survey survey;
    private LogEventWriter logEventWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdata);
        Utils.setToolbar(this);

        survey = Utils.getSurveyFromIntent(getIntent());
        SeatsState state = getStateFromIntent();
        logEventWriter = new LogEventWriter(survey);

        Utils.startAndReturnSeatsFragment(this, survey, state);
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
                logEventWriter.logTrainEvent(LogEventType.DOOR_RELEASE);
                return true;
            case R.id.action_train_starts:
                logEventWriter.logTrainEvent(LogEventType.TRAIN_STARTS);
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
                int count = Integer.parseInt(editTextCount.getText().toString());
                // TODO catch exception
                logEventWriter.logCountStandingPersons(count);
            }
        });
        builder.setNegativeButton(R.string.cancel, Utils.ON_CLICK_DO_NOTHING_LISTENER);
        builder.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // E.g. remove action_door_release when door is currently open.
        // To trigger this callback, call invalidateOptionsMenu()
        return super.onPrepareOptionsMenu(menu);
    }
}
