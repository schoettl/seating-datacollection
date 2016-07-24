package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

// 4 Vierer
public class FloorRectAdapter extends BaseAdapter {
    private final static int FLOOR_RECT_COUNT = 20;
    private static final int SEAT_COUNT = 16;
    private final Context context;
    private final List<View> views;

    public FloorRectAdapter(Context c) {
        this(c, null);
    }

    public FloorRectAdapter(Context c, List<Seat> state) {
        context = c;

        state = checkAndPrepareState(state);

        // Add seats row-wise
        views = new ArrayList<>(FLOOR_RECT_COUNT);
        for (int i = 0, seatIndex = 0; i < FLOOR_RECT_COUNT; i++) {
            if ((i - 2) % 5 == 0) { // 2, 7, 12, 17
                // Placeholder
                views.add(new PlaceholderWidget(context));
            } else {
                Seat seat = state.get(seatIndex++);
                views.add(new SeatWidget(seat, context));
            }
        }
    }

    @NonNull
    private List<Seat> checkAndPrepareState(List<Seat> state) {
        if (state == null) {
            state = new ArrayList<>(SEAT_COUNT);
            for (int seatNumber = 1; seatNumber <= SEAT_COUNT; seatNumber++)
                state.add(new Seat(seatNumber));
        } else if (state.size() != SEAT_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "state has wrong number of seats. expected: %d, actual: %d",
                    SEAT_COUNT, state.size()));
        }
        return state;
    }

    @Override
    public int getCount() {
        return FLOOR_RECT_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return views.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; // not implemented
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return views.get(position);
    }

    public ArrayList<Seat> getSeats() {
        ArrayList<Seat> result = new ArrayList<>(SEAT_COUNT);
        for (int i = 0; i < getCount(); i++) {
            Object o = getItem(i);
            if (o instanceof SeatWidget) {
                Seat seat = ((SeatWidget) o).getSeat();
                result.add(seat);
            }
        }
        return result;
    }
}
