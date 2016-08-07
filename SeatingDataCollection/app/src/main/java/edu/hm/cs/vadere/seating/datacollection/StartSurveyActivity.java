package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class StartSurveyActivity extends AppCompatActivity {

    private static final String TAG = "StartSurveyActivity";
    public static final String EXTRA_SURVEY_ID = "44cb788eb14b3d9a670962249fb0da402999aa72";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startsurvey);
        UiHelper.setToolbar(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String todayStr = Utils.getTodaysDateIsoFormat();
        getEditTextById(R.id.editTextDate).setText(todayStr);
    }

    public void startSurvey(View view) {
        try {
            Survey survey = saveSurvey();
            Intent intent = new Intent(this, InitCollectionActivity.class);
            intent.putExtra(EXTRA_SURVEY_ID, survey.getId());
            startActivity(intent);
        } catch (NumberFormatException e) { // TODO catch date format exception and check for date format in saveSurvey()
            // TODO Error message
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
