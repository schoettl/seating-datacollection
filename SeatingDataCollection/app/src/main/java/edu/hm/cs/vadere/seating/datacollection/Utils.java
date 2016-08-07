package edu.hm.cs.vadere.seating.datacollection;

import android.content.Intent;

import com.orm.util.NamingHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class Utils {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";

    public static Survey getSurveyFromIntent(Intent intent) {
        final int invalidId = 0;
        long surveyId = intent.getLongExtra(StartSurveyActivity.EXTRA_SURVEY_ID, invalidId);
        if (surveyId == invalidId)
            throw new IllegalArgumentException("missing intent extra: " + StartSurveyActivity.EXTRA_SURVEY_ID);
        return getSurvey(surveyId);
    }

    public static Survey getSurvey(long surveyId) {
        Survey survey = Survey.findById(Survey.class, surveyId);
        if (survey == null)
            throw new IllegalArgumentException("invalid survey id: " + surveyId);
        return survey;
    }

    /** EntityName -> ENTITY_NAME */
    public static String toSugarTableName(Class<?> entityClass) {
        return NamingHelper.toSQLName(entityClass);
        // in next release of Sugar ORM it will be com.orm.helper.NamingHelper#toTableName(Class<?>)
    }

    /**
     * fieldName -> FIELD_NAME
     * It does not look good if used in queries, though.
     * I will not use this method but it is good to look up the rules!
     */
    public static String toSugarFieldName(String fieldName) {
        return NamingHelper.toSQLNameDefault(fieldName);
        // in next release of Sugar ORM it will be com.orm.helper.NamingHelper#to???
    }

    public static String getTodaysDateIsoFormat() {
        return new SimpleDateFormat(ISO_DATE_FORMAT).format(new Date());
    }

    public static String formatString(String formatString, Object... args) {
        return String.format(Locale.getDefault(), formatString, args);
    }

}
