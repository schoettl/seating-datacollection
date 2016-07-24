package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import edu.hm.cs.vadere.seating.datacollection.FloorRectAdapter;
import edu.hm.cs.vadere.seating.datacollection.FloorRectWidget;
import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.PersonDialogFragment;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.SeatWidget;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.LEAVE;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.REMOVE_BAGGAGE;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.SIT_DOWN;

public class ActionManager {
    private final Fragment hostFragment;
    private final LogEventWriter logEventWriter;
    private PendingAction pendingAction = PendingAction.NO_PENDING_ACTION;

    public ActionManager(Fragment hostFragment, LogEventWriter logEventWriter) {
        this.logEventWriter = logEventWriter;
        this.hostFragment = hostFragment;
    }

    public void clearPendingAction() {
        pendingAction = PendingAction.NO_PENDING_ACTION;
    }

    public void actionPersonDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(true);

        final EditText editTextReason = new EditText(hostFragment.getContext());
        final OkClickListener okClickListener = new OkClickListener(editTextReason);
        final AlertDialog.Builder builder = new AlertDialog.Builder(hostFragment.getContext());
        builder.setTitle("Reason for disturbing");
        builder.setMessage("You can type-in a reason");
        builder.setView(editTextReason);
        builder.setPositiveButton(R.string.ok, okClickListener);
        builder.show();

        logEventWriter.logDisturbingPerson(p, okClickListener.getResult());
    }

    public void actionPersonStopsDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(false);
        logEventWriter.logStopsDisturbingPerson(p);
    }

    public void actionSetPersonProperties(Seat seat) {
        DialogFragment dialog = PersonDialogFragment.newInstance((Person) seat.getSeatTaker());
        dialog.show(hostFragment.getActivity().getSupportFragmentManager(), "???");
    }

    public void actionPlaceBaggage(Seat seat) {
        pendingAction = new PlaceBaggageAction(this, (Person) seat.getSeatTaker());
    }

    public void actionRemoveBaggage(Seat seat) {
        HandBaggage b = (HandBaggage) seat.getSeatTaker();
        seat.clearSeat();
        logEventWriter.logSeatEvent(REMOVE_BAGGAGE, seat, b.getOwner());
    }

    public void actionSitDown(Seat seat) {
        Person person = new Person();
        person.save();
        checkSeatNotOccupiedByPerson(seat);
        removeBaggageIfAny(seat);
        seat.setSeatTaker(person);
        logEventWriter.logSeatEvent(SIT_DOWN, seat, person);
    }

    private void checkSeatNotOccupiedByPerson(Seat seat) {
        if (seat.getSeatTaker() instanceof Person) {
            throw new IllegalStateException("seat is occupied by another person");
        }
    }

    public void actionChangeSeat(Seat seat) {
        pendingAction = new ChangeSeatAction(this, seat);
    }

    public void actionLeave(Seat seat) {
        Person person = (Person) seat.getSeatTaker();

        removeBaggageForPerson(person);

        seat.clearSeat();
        logEventWriter.logSeatEvent(LEAVE, seat, person);
    }

    public void actionMarkAgent(Survey survey) {
        pendingAction = new MarkAgentAction(this, survey);
    }

    public void finishActionPlaceBaggage(Person person, Seat otherSeat) {
        HandBaggage baggage = new HandBaggage(person);
        otherSeat.setSeatTaker(baggage);
        logEventWriter.logSeatEvent(LogEventType.PLACE_BAGGAGE, otherSeat, person);
    }

    public void finishActionChangeSeat(Seat seat, Seat newSeat) {
        checkSeatNotOccupiedByPerson(newSeat);

        Person person = (Person) seat.getSeatTaker();
        seat.clearSeat();

        removeBaggageIfAny(newSeat);
        newSeat.setSeatTaker(person);
        logEventWriter.logSeatEvent(LogEventType.CHANGE_SEAT, newSeat, person);
    }

    public void finishActionMarkAgent(Survey survey, Person person) {
        survey.setAgent(person);
        survey.save();
    }

    public void finishPendingAction(SeatWidget view) {
        pendingAction.seatSelected(view);
        clearPendingAction();
    }

    public boolean isActionPending() {
        return pendingAction.isActionPending();
    }

    private void removeBaggageForPerson(Person person) {
        GridView gridView = (GridView) hostFragment.getView();
        FloorRectAdapter adapter = (FloorRectAdapter) gridView.getAdapter();
        for (View w : adapter) {
            if (w instanceof SeatWidget) {
                Seat seatWithBaggage = ((SeatWidget) w).getSeat();
                SeatTaker seatTaker = seatWithBaggage.getSeatTaker();
                if (seatTaker instanceof HandBaggage && ((HandBaggage) seatTaker).getOwner() == person) {
                    actionRemoveBaggage(seatWithBaggage);
                }
            }
        }
    }

    private void removeBaggageIfAny(Seat seat) {
        if (seat.getSeatTaker() instanceof HandBaggage) {
            actionRemoveBaggage(seat);
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
