package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.orm.util.NamingHelper;

import java.util.ArrayList;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class Utils {

    public static Survey getSurveyFromIntent(Intent intent) {
        final int invalidId = 0;
        long surveyId = intent.getLongExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, invalidId);
        if (surveyId == invalidId)
            throw new IllegalArgumentException("missing intent extra: " + StartSurveyActivity.EXTRA_SURVEY_ID_KEY);
        return Survey.findById(Survey.class, surveyId);
    }

    /** Start a SeatsFragment. The state can be null. */
    public static SeatsFragment startAndReturnSeatsFragment(FragmentActivity activity, LogEventWriter logEventWriter, ArrayList<Seat> state) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        SeatsFragment fragment = SeatsFragment.newInstance(logEventWriter, state);
        ft.replace(R.id.seats_fragment_placeholder, fragment);
        ft.commit();
        return fragment;
    }

    /** EntityName -> ENTITY_NAME */
    public static String toSugarTableName(Class<?> entityClass) {
        return NamingHelper.toSQLName(entityClass);
        // in next release of Sugar ORM it will be com.orm.helper.NamingHelper#toTableName(Class<?>)
    }

}
