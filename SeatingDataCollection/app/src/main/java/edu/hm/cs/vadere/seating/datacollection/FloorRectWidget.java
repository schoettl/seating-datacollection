package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.widget.TextView;

public class FloorRectWidget extends TextView {
    public FloorRectWidget(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
