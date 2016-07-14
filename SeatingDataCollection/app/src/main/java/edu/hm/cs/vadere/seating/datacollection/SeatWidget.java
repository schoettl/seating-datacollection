package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SeatWidget extends TextView {
    private Seat seat;

    {
        setText("seat " + seat.getId());
    }

    public SeatWidget(Seat seat, Context context) {
        super(context);
        this.seat = seat;
        // I would use this(seat, ...) if I was sure about the default values for the other constructors's parameters.
    }

    public SeatWidget(Seat seat, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.seat = seat;
    }

}
