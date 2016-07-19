package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class InitCollectionActivity extends AppCompatActivity {

    private Survey survey;
    private LogEventWriter logEventWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initcollection);

        survey = Utils.getSurveyFromIntent(getIntent());
        logEventWriter = new LogEventWriter(survey);
    }

    public void startDataCollection(View view) {
        logEventWriter.logInitializationEnd();
        Intent intent = new Intent(this, CollectDataActivity.class);
        intent.putExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, survey.getId());
        startActivity(intent);
    }

}
