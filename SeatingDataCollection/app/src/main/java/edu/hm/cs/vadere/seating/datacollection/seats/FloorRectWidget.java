package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.Context;
import android.widget.RelativeLayout;

public class FloorRectWidget extends RelativeLayout {
    public FloorRectWidget(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
