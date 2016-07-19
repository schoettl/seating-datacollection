package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class Utils {

    public static Survey getSurveyFromIntent(Intent intent) {
        final int invalidId = 0;
        long surveyId = intent.getLongExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, invalidId);
        if (surveyId == invalidId)
            throw new IllegalArgumentException("missing intent extra: " + StartSurveyActivity.EXTRA_SURVEY_ID_KEY);
        return Survey.findById(Survey.class, surveyId);
    }

    public static void startSeatsFragment(FragmentActivity activity, LogEventWriter logEventWriter) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        SeatsFragment fragment = SeatsFragment.newInstance(logEventWriter);
        ft.replace(R.id.seats_fragment_placeholder, fragment);
        ft.commit();
    }

}
