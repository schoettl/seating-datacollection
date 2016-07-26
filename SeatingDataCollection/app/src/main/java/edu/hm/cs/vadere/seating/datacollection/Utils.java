package edu.hm.cs.vadere.seating.datacollection;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.orm.util.NamingHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsState;

public class Utils {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final DialogInterface.OnClickListener ON_CLICK_DO_NOTHING_LISTENER =
            new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
            };

    public static Survey getSurveyFromIntent(Intent intent) {
        final int invalidId = 0;
        long surveyId = intent.getLongExtra(StartSurveyActivity.EXTRA_SURVEY_ID_KEY, invalidId);
        if (surveyId == invalidId)
            throw new IllegalArgumentException("missing intent extra: " + StartSurveyActivity.EXTRA_SURVEY_ID_KEY);
        return getSurvey(surveyId);
    }

    public static Survey getSurvey(long surveyId) {
        Survey survey = Survey.findById(Survey.class, surveyId);
        if (survey == null)
            throw new IllegalArgumentException("invalid survey id: " + surveyId);
        return survey;
    }

    /** Start a SeatsFragment. The state can be null. */
    public static SeatsFragment startAndReturnSeatsFragment(FragmentActivity activity, Survey survey, SeatsState state) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        SeatsFragment fragment = SeatsFragment.newInstance(survey, state);
        ft.replace(R.id.seats_fragment_placeholder, fragment);
        ft.commit();
        return fragment;
    }

    /** EntityName -> ENTITY_NAME */
    public static String toSugarTableName(Class<?> entityClass) {
        return NamingHelper.toSQLName(entityClass);
        // in next release of Sugar ORM it will be com.orm.helper.NamingHelper#toTableName(Class<?>)
    }

    public static void setToolbar(AppCompatActivity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.app_toolbar);
        activity.setSupportActionBar(toolbar);
    }

    public static String getTodaysDateIsoFormat() {
        return new SimpleDateFormat(ISO_DATE_FORMAT).format(new Date());
    }

}
