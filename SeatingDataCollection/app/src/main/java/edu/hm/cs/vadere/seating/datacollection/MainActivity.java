package edu.hm.cs.vadere.seating.datacollection;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.orm.SugarTransactionHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.hm.cs.vadere.seating.datacollection.model.LogEvent;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int EXPORT_DATA_REQUEST_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private SurveyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiHelper.setToolbar(this);

        adapter = new SurveyListAdapter(this, R.layout.item_survey);
        // For the CursorAdapter I need to know database details :/
        //CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_survey, cursor, columns, toViewIds, 0);
        ListView listView = (ListView) findViewById(R.id.listViewSurvey);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Reload the survey list when the activity restarts.
        Log.d(TAG, "reloading survey list");
        adapter.reload();
        // According to Google's guide, it's uncommon to implement onRestart().
        // In this case it makes sense: reload() is implicitly called in the adapter's constructor.
        // Therefore onStart() would call it a second time.
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listViewSurvey) {
            final MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_survey, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final Survey selectedSurvey = UiHelper.getItemFromMenuInfo(item.getMenuInfo(), adapter);
        switch (item.getItemId()) {
            case R.id.survey_delete:
                UiHelper.showConfirmDialog(this, R.string.dialog_confirm_delete_survey, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSurvey(selectedSurvey.getId());
                        adapter.remove(selectedSurvey);
                    }
                });
                return true;
            case R.id.survey_new_from_template:
                startNewSurvey(selectedSurvey.getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteSurvey(long id) {
        Log.d(TAG, "deleting survey " + id + " in transaction");
        final String idAsString = String.valueOf(id);
        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {

                Survey.deleteAll(Survey.class, "id = ?", idAsString);

                final Set<Person> personsToDelete = new HashSet<>();
                Iterator<LogEvent> it = LogEvent.findAsIterator(LogEvent.class, "survey = ?", idAsString);
                while (it.hasNext()) {
                    LogEvent event = it.next();
                    personsToDelete.add(event.getPerson());
                }
                Person.deleteInTx(personsToDelete);

                LogEvent.deleteAll(LogEvent.class, "survey = ?", idAsString);
            }
        });
    }

    public void startNewSurvey(View view) {
        Long lastSurveyId = null;
        if (!adapter.isEmpty())
            lastSurveyId = adapter.getItem(0).getId();
        startNewSurvey(lastSurveyId);
    }

    public void exportData(View view) {
        Log.i(TAG, "requesting permissions for export first");
        if (!requestPermissions()) {
            return;
        }

        Log.i(TAG, "starting export");
        ProgressDialog progressDialog = new ProgressDialog(this);
        AsyncTask<Void, ?, ?> task = new DatabaseExportTask(this, progressDialog);
        task.execute();
    }

    public void deleteDatabase(View view) {
        UiHelper.showConfirmDialog(this, R.string.dialog_confirm_delete_db, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "deleting database");
                deleteDatabase(SeatingDataCollectionApp.DATABASE_NAME);
                adapter.reload();
            }
        });
    }

    public void startNewSurvey(Long surveyId) {
        Intent intent = new Intent(this, StartSurveyActivity.class);
        intent.putExtra(StartSurveyActivity.EXTRA_SURVEY_ID, surveyId); // as a template
        startActivity(intent);
    }

    private boolean requestPermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // TODO show explanation instead of just retrying:
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, EXPORT_DATA_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, EXPORT_DATA_REQUEST_CODE);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXPORT_DATA_REQUEST_CODE:
                if (areAllPermissionsGranted(grantResults)) {
                    Log.i(TAG, "permissions for export granted");
                    exportData(null);
                } else {
                    Log.i(TAG, "permissions for export denied");
                }
                break;
        }
    }

    private boolean areAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int result : grantResults)
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

}
