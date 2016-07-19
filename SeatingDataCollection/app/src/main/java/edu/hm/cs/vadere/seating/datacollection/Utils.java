package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class Utils {

    public static Survey getSurveyFromIntent(Intent intent) {
        final int invalidId = 0;
        long surveyId = intent.getLongExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, invalidId);
        if (surveyId == invalidId)
            throw new IllegalArgumentException("missing intent extra: " + StartSurveyActivity.EXTRA_SURVEY_ID_KEY);
        return Survey.findById(Survey.class, surveyId);
    }

}
