package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SeatWidget extends FloorRectWidget {
    private Seat seat;

    private void init(Seat seat) {
        this.seat = seat;
        setText(toString());
        setBackgroundColor(Color.BLUE);
        setGravity(Gravity.CENTER);
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
