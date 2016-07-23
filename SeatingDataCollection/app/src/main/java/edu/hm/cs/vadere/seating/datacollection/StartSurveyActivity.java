package edu.hm.cs.vadere.seating.datacollection;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class StartSurveyActivity extends AppCompatActivity {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EXTRA_SURVEY_ID_KEY = "44cb788eb14b3d9a670962249fb0da402999aa72";

    private static final int EXPORT_DATA_REQUEST_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private static final String TAG = "StartSurveyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startsurvey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String todayStr = new SimpleDateFormat(ISO_DATE_FORMAT).format(new Date());
        getEditTextById(R.id.editTextDate).setText(todayStr);
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

    public void exportData(View view) {
        Log.i(TAG, "requesting permissions for export first");
        if (!requestPermissions()) {
            return;
        }

        Log.i(TAG, "starting export");
        ProgressDialog progressDialog = new ProgressDialog(this);
        AsyncTask<Void, ?, ?> task = new DatabaseExportTask(getBaseContext(), progressDialog);
        task.execute();
    }

    public void startSurvey(View view) {
        try {
            Survey survey = saveSurvey();
            Intent intent = new Intent(this, InitCollectionActivity.class);
            intent.putExtra(EXTRA_SURVEY_ID_KEY, survey.getId());
            startActivity(intent);
        } catch (NumberFormatException e) { // TODO catch date format exception and check for date format in saveSurvey()
            // Error message
        }
    }

    private Survey saveSurvey() {
        Survey survey = new Survey();
        survey.setAgentName(getTextFromEdit(R.id.editTextName));
        survey.setDate(getTextFromEdit(R.id.editTextDate));
        survey.setStartingAt(getTextFromEdit(R.id.editTextStartingAt));
        survey.setDestination(getTextFromEdit(R.id.editTextDirection));
        survey.setLine(getTextFromEdit(R.id.editTextLine));
        survey.setWagonNo(getIntFromEdit(R.id.editTextWagonNo));
        survey.setDoorNo(getIntFromEdit(R.id.editTextDoorNo));
        survey.setTrainType(getTextFromEdit(R.id.editTextTrainType));
        survey.setTrainNumber(getTextFromEdit(R.id.editTextTrainNo));
        survey.save();
        return survey;
    }

    private EditText getEditTextById(int viewId) {
        return (EditText) findViewById(viewId);
    }

    private String getTextFromEdit(int viewId) {
        EditText edit = getEditTextById(viewId);
        return edit.getText().toString();
    }

    private int getIntFromEdit(int viewId) {
        String text = getTextFromEdit(viewId);
        if (text.isEmpty())
            text = "0"; // default for numbers in Person record
        return Integer.parseInt(text);
    }

}
