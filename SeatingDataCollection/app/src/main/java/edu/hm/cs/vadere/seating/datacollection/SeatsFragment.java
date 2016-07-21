package edu.hm.cs.vadere.seating.datacollection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import edu.hm.cs.vadere.seating.datacollection.actions.ChangeSeatAction;
import edu.hm.cs.vadere.seating.datacollection.actions.PendingAction;
import edu.hm.cs.vadere.seating.datacollection.actions.PlaceBaggageAction;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.LEAVE;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.REMOVE_BAGGAGE;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.SIT_DOWN;

public class SeatsFragment extends Fragment {

    private static final String LOG_EVENT_WRITER_KEY = "2f78552dc00b45e7a0f18701fe3a5b5994eb4d55";
    public static final String TAG = "SeatsFragment";

    private FloorRectAdapter floorRectAdapter;
    private LogEventWriter logEventWriter;
    private PendingAction pendingAction = PendingAction.NO_PENDING_ACTION;

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
        floorRectAdapter = new FloorRectAdapter(getContext());
        View.OnClickListener seatClickListener = new SeatClickListener();
        for (View v : floorRectAdapter) {
            if (v instanceof SeatWidget) {
                SeatWidget seatWidget = (SeatWidget) v;
                seatWidget.setOnClickListener(seatClickListener);
            }
        }
        view.setAdapter(floorRectAdapter);
        registerForContextMenu(view);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View gridView, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "on create context menu");
        super.onCreateContextMenu(menu, gridView, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Log.d(SeatsFragment.class.getSimpleName(), info.targetView.toString());
        View v = info.targetView;

        if (v instanceof SeatWidget) {
            MenuInflater inflater = getActivity().getMenuInflater();
            SeatWidget seatWidget = (SeatWidget) v;
            SeatTaker seatTaker = seatWidget.getSeat().getSeatTaker();
            if (seatTaker instanceof Person) {
                Log.d(TAG, "person");
                inflater.inflate(R.menu.context_menu_person, menu);

            } else if (seatTaker instanceof HandBaggage) {
                Log.d(TAG, "baggage");
                inflater.inflate(R.menu.context_menu_baggage, menu);

            } else { // seat is empty
                Log.d(TAG, "empty seat");
                inflater.inflate(R.menu.context_menu_seat, menu);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get clicked view: http://stackoverflow.com/questions/2926293/identifying-the-view-selected-in-a-contextmenu-android
        Log.d(TAG, "on options item selected");
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View listItem = (View) floorRectAdapter.getItem(menuInfo.position);
        Log.d(TAG, listItem.toString());
        if (!(menuInfo.targetView instanceof SeatWidget))
            return false;

        Seat seat = ((SeatWidget) menuInfo.targetView).getSeat();
        Log.d(TAG, seat.toString());

        cancelPendingAction();
        switch (item.getItemId()) {
            case R.id.action_sit_down:
                Log.d(TAG, "action sit down");
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
                Log.w(TAG, "context menu item not implemented");
                return super.onOptionsItemSelected(item);
        }
    }

    private void cancelPendingAction() {
        pendingAction = PendingAction.NO_PENDING_ACTION;
    }

    private void actionPersonDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(true);

        final EditText editTextReason = new EditText(getContext());
        final OkClickListener okClickListener = new OkClickListener(editTextReason);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reason for disturbing");
        builder.setMessage("You can type-in a reason");
        builder.setView(editTextReason);
        builder.setPositiveButton(R.string.ok, okClickListener);
        builder.show();

        logEventWriter.logDisturbingPerson(p, okClickListener.getResult());
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
        pendingAction = new PlaceBaggageAction(logEventWriter, (Person) seat.getSeatTaker());
    }

    private void actionRemoveBaggage(Seat seat) {
        HandBaggage b = (HandBaggage) seat.getSeatTaker();
        seat.clearSeat();
        logEventWriter.logSeatEvent(REMOVE_BAGGAGE, seat, b.getOwner());
    }

    private void actionSitDown(Seat seat) {
        Person person = new Person();
        person.save();
        seat.setSeatTaker(person);
        logEventWriter.logSeatEvent(SIT_DOWN, seat, person);
    }

    private void actionChangeSeat(Seat seat) {
        pendingAction = new ChangeSeatAction(logEventWriter, seat);
    }

    private void actionLeave(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        seat.clearSeat();
        logEventWriter.logSeatEvent(LEAVE, seat, p);
    }

    private class SeatClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, v.toString());
            if (pendingAction.isActionPending()) {
                pendingAction.seatSelected((SeatWidget) v);
            }
            cancelPendingAction();
        }
    }

    private class OkClickListener implements DialogInterface.OnClickListener {
        private EditText edit;
        private String result;
        public OkClickListener(EditText edit) {
            this.edit = edit;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            result = edit.getText().toString();
        }
        public String getResult() {
            return result;
        }
    }
}
