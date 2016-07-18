package edu.hm.cs.vadere.seating.datacollection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.*;

public class SeatsFragment extends Fragment {

    private static final String LOG_EVENT_WRITER_KEY = "2f78552dc00b45e7a0f18701fe3a5b5994eb4d55";
    private LogEventWriter logEventWriter;

    public static SeatsFragment newInstance(LogEventWriter logEventWriter) {
        SeatsFragment fragment = new SeatsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOG_EVENT_WRITER_KEY, logEventWriter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        logEventWriter = (LogEventWriter) getArguments().getSerializable(LOG_EVENT_WRITER_KEY);

        GridView view = (GridView) inflater.inflate(R.layout.fragment_seats, container, false);
        registerForContextMenu(view);
        ListAdapter adapter = new FloorRectAdapter(getContext());
        view.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View gridView, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("SeatsFragment", "on create context menu");
        super.onCreateContextMenu(menu, gridView, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Log.d(SeatsFragment.class.getSimpleName(), info.targetView.toString());
        View v = info.targetView;

        if (v instanceof SeatWidget) {
            MenuInflater inflater = getActivity().getMenuInflater();
            SeatWidget seatWidget = (SeatWidget) v;
            SeatTaker seatTaker = seatWidget.getSeat().getSeatTaker();
            if (seatTaker instanceof Person) {
                Log.d("SeatsFragment", "person");
                inflater.inflate(R.menu.context_menu_person, menu);

            } else if (seatTaker instanceof HandBaggage) {
                Log.d("SeatsFragment", "baggage");
                inflater.inflate(R.menu.context_menu_baggage, menu);

            } else { // seat is empty
                Log.d("SeatsFragment", "empty seat");
                inflater.inflate(R.menu.context_menu_seat, menu);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get clicked view: http://stackoverflow.com/questions/2926293/identifying-the-view-selected-in-a-contextmenu-android
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d("SeatsFragment", menuInfo.targetView.toString());
        if (!(menuInfo.targetView instanceof SeatWidget))
            return false;

        Seat seat = ((SeatWidget) menuInfo.targetView).getSeat();

        switch (item.getItemId()) {
            case R.id.action_sit_down:
                actionSitDown(seat);
                return true;
            case R.id.action_change_place:
                actionChangeSeat(seat);
                return true;
            case R.id.action_leave:
                actionLeave(seat);
                return true;
            case R.id.action_place_baggage:
                actionPlaceBaggage(seat);
                return true;
            case R.id.action_remove_baggage:
                actionRemoveBaggage(seat);
                return true;
            case R.id.action_set_person_properties:
                actionSetPersonProperties(seat);
                return true;
            case R.id.action_person_disturbing:
                actionPersonDisturbing(seat);
                return true;
            case R.id.action_person_stops_disturbing:
                actionPersonStopsDisturbing(seat);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionPersonDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(true);
        logEventWriter.logDisturbingPerson(p, null); // TODO prompt for reason
    }

    private void actionPersonStopsDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(false);
        logEventWriter.logStopsDisturbingPerson(p);
    }

    private void actionSetPersonProperties(Seat seat) {
        // TODO show dialog
    }

    private void actionPlaceBaggage(Seat seat) {
        HandBaggage b = new HandBaggage((Person) seat.getSeatTaker());
        // TODO wait for select target seat
        //otherSeat.setSeatTaker(b);
        //logEventWriter.logSeatEvent(PLACE_BAGGAGE, seat, b.getOwner());
    }

    private void actionRemoveBaggage(Seat seat) {
        HandBaggage b = (HandBaggage) clearSeat(seat);
        logEventWriter.logSeatEvent(REMOVE_BAGGAGE, seat, b.getOwner());
    }

    private void actionSitDown(Seat seat) {
        Person person = new Person();
        person.save();
        seat.setSeatTaker(person);
        logEventWriter.logSeatEvent(SIT_DOWN, seat, person);
    }

    private void actionChangeSeat(Seat seat) {
        Person personChangingSeat = (Person) seat.getSeatTaker();
        // TODO wait for select target seat
        //newSeat.setSeatTaker(personChangingSeat);
        //logEventWriter.logSeatEvent(CHANGE_SEAT, newSeat, personChangingSeat);
    }

    private void actionLeave(Seat seat) {
        Person p = (Person) clearSeat(seat);
        logEventWriter.logSeatEvent(LEAVE, seat, p);
    }

    private SeatTaker clearSeat(Seat seat) {
        SeatTaker x = seat.getSeatTaker();
        seat.setSeatTaker(null);
        return x;
    }
}
