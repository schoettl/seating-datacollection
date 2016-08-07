package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.Utils;
import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.MGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class SeatWidget extends FloorRectWidget {
    private static final String TAG = "SeatWidget";
    private Seat seat;
    private ImageView ivSeatIcon;
    private TextView tvMain;
    private TextView tvGroup;
    private TextView tvSeatNumber;
    private TextView tvProperties;

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
        tvGroup = (TextView) findViewById(R.id.tvGroup);
        tvSeatNumber = (TextView) findViewById(R.id.tvSeatNumber);
        tvProperties = (TextView) findViewById(R.id.tvProperties);

        tvSeatNumber.setText(String.valueOf(getSeat().getId()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d(TAG, "drawing seat widget");
        super.onDraw(canvas);
        updateUiForCurrentState();
    }

    public Seat getSeat() {
        return seat;
    }

    private void updateUiForCurrentState() {
        SeatTaker seatTaker = seat.getSeatTaker();
        String mainString;
        String propertiesString = "";
        String groupString = "";
        switch (getSeatState()) {
            case PERSON:
                ivSeatIcon.setImageResource(R.drawable.ic_person_black_18dp);
                final Person person = (Person) seatTaker;
                mainString = Utils.formatString("person #%d", person.getId());
                propertiesString = getPropertiesString(person);
                groupString = getGroupString(person);
                break;
            case BAGGAGE:
                ivSeatIcon.setImageResource(R.drawable.ic_work_black_18dp);
                final HandBaggage baggage = (HandBaggage) seatTaker;
                mainString = Utils.formatString("baggage of #%d", baggage.getOwner().getId());
                break;
            case EMPTY:
            default:
                ivSeatIcon.setImageDrawable(null);
                mainString = "empty";
                break;
        }
        tvMain.setText(mainString);
        tvProperties.setText(propertiesString);
        tvGroup.setText(groupString);
    }

    private String getGroupString(Person person) {
        MGroup g = person.getGroup();
        if (g == null)
            return "";
        return "grp. " + g.getId();
    }

    private String getPropertiesString(Person person) {
        List<Object> propertyList = new LinkedList<>();
        if (person.getGender() != Gender.NA || person.getAgeGroup() != AgeGroup.NA) {
            propertyList.add(person.getGender());
            propertyList.add(person.getAgeGroup());
        }
        if (person.isAgent())
            propertyList.add("agent");
        if (person.isDisturbing())
            propertyList.add("disturbing");

        if (propertyList.isEmpty())
            return "";

        String result = TextUtils.join(", ", propertyList);
        return "(" + result + ")";
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
