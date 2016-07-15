package edu.hm.cs.vadere.seating.datacollection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class InitCollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initcollection);
    }

    public void startDataCollection(View view) {
        // TODO save initial state to database
        Intent intent = new Intent(this, CollectDataActivity.class);
        startActivity(intent);
    }
}
