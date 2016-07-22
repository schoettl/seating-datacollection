package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.model.FloorRectWidget;

public class PlaceholderWidget extends FloorRectWidget {

    private void init() {
        setText("placeholder");
    }

    public PlaceholderWidget(Context context) {
        super(context);
        init();
    }

}
