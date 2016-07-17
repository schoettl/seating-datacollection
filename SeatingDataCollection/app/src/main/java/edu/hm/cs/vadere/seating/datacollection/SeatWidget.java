package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SeatWidget extends TextView {
    private Seat seat;

    private void init(Seat seat) {
        this.seat = seat;
        setText(toString());
        setBackgroundColor(Color.BLUE);
    }

    public SeatWidget(Seat seat, Context context) {
        super(context);
        init(seat);
    }

    public SeatWidget(Seat seat, Context context, AttributeSet attrs) {
        super(context, attrs);
        init(seat);
    }

    @Override
    public String toString() {
        return "seat " + seat.getId();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        width = height = Math.min(width, height);
//        setMeasuredDimension(width, height);
    }
}
