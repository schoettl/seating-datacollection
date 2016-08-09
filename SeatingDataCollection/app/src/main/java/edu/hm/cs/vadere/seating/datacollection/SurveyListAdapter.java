package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class SurveyListAdapter extends ArrayAdapter<Survey> {
    public SurveyListAdapter(Context context, int resource) {
        super(context, resource);
        reload();
    }

    public void reload() {
        List<Survey> allSurveys = Survey.listAll(Survey.class, "id DESC"); // findAll's iterator cannot be used in an adapter
        clear();
        addAll(allSurveys);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SurveyItem view;
        if (convertView instanceof SurveyItem) {
            view = (SurveyItem) convertView;
        } else {
            view = new SurveyItem(getContext());
        }
        view.setSurvey(getItem(position));
        return view;
    }

}
