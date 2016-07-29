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
import android.view.View;
import android.widget.ListView;

import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int EXPORT_DATA_REQUEST_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiHelper.setToolbar(this);

        List<Survey> allSurveys = Survey.listAll(Survey.class); // findAll's iterator cannot be used in an adapter
        SurveyListAdapter adapter = new SurveyListAdapter(this, R.layout.item_survey, allSurveys);
        // For the CursorAdapter I need to know database details :/
        //CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_survey, cursor, columns, toViewIds, 0);
        ListView listView = (ListView) findViewById(R.id.listViewSurvey);
        listView.setAdapter(adapter);
    }

    public void startNewSurvey(View view) {
        Intent intent = new Intent(this, StartSurveyActivity.class);
        startActivity(intent);
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
                // TODO list of surveys only gets updated onCreate
            }
        });
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
