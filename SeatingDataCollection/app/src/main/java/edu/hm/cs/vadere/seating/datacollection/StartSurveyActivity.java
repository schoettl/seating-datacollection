package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartSurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startsurvey);
    }

    public void startSurvey(View view) {
        // TODO save input to database
        Intent intent = new Intent(this, InitCollectionActivity.class);
        startActivity(intent);
    }
}
