package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class StartSurveyActivity extends AppCompatActivity {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EXTRA_SURVEY_ID_KEY = "44cb788eb14b3d9a670962249fb0da402999aa72";

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

    public void exportData(View view) {
        AsyncTask task = new DatabaseExportTask(getBaseContext(), null);
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
        survey.setValuesFromGUI(this);
        survey.save();
        return survey;
    }

    private EditText getEditTextById(int viewId) {
        return (EditText) findViewById(viewId);
    }

}
