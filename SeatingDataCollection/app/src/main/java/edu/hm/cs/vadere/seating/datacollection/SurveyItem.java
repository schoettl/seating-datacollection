package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class SurveyItem extends RelativeLayout {
    private TextView tvTitle;
    private TextView tvSub1;
    private TextView tvSub2;

    public SurveyItem(Context context) {
        super(context);
        init();
    }

    public SurveyItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_survey, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSub1 = (TextView) findViewById(R.id.tvSub1);
        tvSub2 = (TextView) findViewById(R.id.tvSub2);
    }

    public void setSurvey(Survey survey) {
        tvTitle.setText(survey.toString());
        String dateAndTime = Utils.formatString("%s %s", survey.getDate(), survey.getStartTime());
        tvSub1.setText(dateAndTime);
        tvSub2.setText(survey.getAgentName());
    }

}
