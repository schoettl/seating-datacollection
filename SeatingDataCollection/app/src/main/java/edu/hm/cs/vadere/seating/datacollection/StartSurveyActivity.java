package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class StartSurveyActivity extends AppCompatActivity {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";

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

    public void startSurvey(View view) {
        try {
            saveSurvey();
            Intent intent = new Intent(this, InitCollectionActivity.class);
            startActivity(intent);
        } catch (NumberFormatException e) {
            // Bitte Zahlen eingeben
        }
    }

    private void saveSurvey() {
        Survey survey = new Survey();
        survey.setValuesFromGUI(this);
        survey.save();
    }

    private EditText getEditTextById(int viewId) {
        return (EditText) findViewById(viewId);
    }

}
