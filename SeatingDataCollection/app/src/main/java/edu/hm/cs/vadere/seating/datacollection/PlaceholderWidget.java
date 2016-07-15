package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PlaceholderWidget extends TextView {

    private void init() {
        setText("placeholder");
    }

    public PlaceholderWidget(Context context) {
        super(context);
        init();
    }

    public PlaceholderWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

}
