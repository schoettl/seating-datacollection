package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.Utils;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class SeatWidget extends FloorRectWidget {
    private static final String TAG = "SeatWidget";
    private Seat seat;
    private ImageView ivSeatIcon;
    private TextView tvMain;
    private TextView tvTest;
    private TextView tvSeatNumber;

    public SeatWidget(Seat seat, Context context) {
        super(context);
        init(seat);
    }

    private void init(Seat seat) {
        this.seat = seat;
        inflate(getContext(), R.layout.seat_widget, this);
        setWillNotDraw(false); // not even mentioned in Google's guide for custom components / drawing

        ivSeatIcon = (ImageView) findViewById(R.id.ivSeatIcon);
        tvMain = (TextView) findViewById(R.id.tvMain);
        tvTest = (TextView) findViewById(R.id.tvTest);
        tvSeatNumber = (TextView) findViewById(R.id.tvSeatNumber);

        tvSeatNumber.setText(String.valueOf(getSeat().getId()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "drawing seat widged");
        super.onDraw(canvas);
        updateUiForCurrentState();
    }

    public Seat getSeat() {
        return seat;
    }

    private void updateUiForCurrentState() {
        SeatTaker seatTaker = seat.getSeatTaker();
        switch (getSeatState()) {
            case PERSON:
                ivSeatIcon.setImageResource(R.drawable.ic_person_black_18dp);
                final Person person = (Person) seatTaker;
                tvMain.setText(Utils.formatString("person #%d", person.getId()));
                break;
            case BAGGAGE:
                ivSeatIcon.setImageResource(R.drawable.ic_work_black_18dp);
                final HandBaggage baggage = (HandBaggage) seatTaker;
                tvMain.setText(Utils.formatString("baggage of #%d", baggage.getOwner().getId()));
                break;
            case EMPTY:
            default:
                ivSeatIcon.setImageDrawable(null);
                tvMain.setText("emtpy");
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
