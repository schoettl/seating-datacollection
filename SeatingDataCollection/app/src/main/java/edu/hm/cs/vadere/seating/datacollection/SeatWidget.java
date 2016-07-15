package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SeatWidget extends TextView {
    private Seat seat;

    private void init(Seat seat) {
        this.seat = seat;
        setText(toString());
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

}
