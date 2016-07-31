package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class SeatWidget extends FloorRectWidget {
    private Seat seat;
    private ImageView ivSeatIcon;
    private TextView tvPerson;
    private TextView tvTest;

    public SeatWidget(Seat seat, Context context) {
        super(context);
        init(seat);
    }

    private void init(Seat seat) {
        this.seat = seat;
        inflate(getContext(), R.layout.seat_widget, this);
        ivSeatIcon = (ImageView) findViewById(R.id.ivSeatIcon);
        tvPerson = (TextView) findViewById(R.id.tvPerson);
        tvTest = (TextView) findViewById(R.id.tvTest);

        setBackgroundColor(Color.BLUE);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        tvPerson.setText(seat.toString());
        setIconForCurrentSeatState();
        super.onDraw(canvas);
    }

    public Seat getSeat() {
        return seat;
    }

    private void setIconForCurrentSeatState() {
        switch (getSeatState()) {
            case PERSON:
                ivSeatIcon.setImageResource(R.drawable.ic_person_black_18dp);
                break;
            case BAGGAGE:
                ivSeatIcon.setImageResource(R.drawable.ic_work_black_18dp);
                break;
            case EMPTY:
            default:
                ivSeatIcon.setImageDrawable(null);
                break;
        }
    }

    private SeatState getSeatState() {
        SeatTaker seatTaker = seat.getSeatTaker();
        if (seatTaker instanceof Person)
            return SeatState.PERSON;
        if (seatTaker instanceof HandBaggage)
            return SeatState.BAGGAGE;
        return SeatState.EMPTY;
    }

    private enum SeatState {
        EMPTY, PERSON, BAGGAGE;
    }
}
