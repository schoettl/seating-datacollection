package edu.hm.cs.vadere.seating.datacollection;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.model.FloorRectWidget;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SeatWidget extends FloorRectWidget {
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

    @Override
    public String toString() {
        return "seat " + seat.getId();
    }

    public Seat getSeat() {
        return seat;
    }
}
