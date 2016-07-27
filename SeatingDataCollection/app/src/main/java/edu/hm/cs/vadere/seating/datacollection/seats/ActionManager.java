package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;

import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.PersonDialogFragment;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.Utils;
import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
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
    private static final String TAG = "ActionManager";
    private final Fragment hostFragment;
    private final LogEventWriter logEventWriter;
    private PendingAction pendingAction = null;

    public ActionManager(Fragment hostFragment, LogEventWriter logEventWriter) {
        this.logEventWriter = logEventWriter;
        this.hostFragment = hostFragment;
    }

    public void clearPendingAction() {
        pendingAction = null;
    }

    public void actionPersonDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        // Action is done in the okClickListener

        final EditText editTextReason = new EditText(hostFragment.getContext());
        final DisturbingReasonOkClickListener okClickListener = new DisturbingReasonOkClickListener(p, editTextReason);
        final AlertDialog.Builder builder = new AlertDialog.Builder(hostFragment.getContext());
        builder.setTitle(R.string.dialog_disturbing_reason);
        builder.setView(editTextReason);
        builder.setPositiveButton(R.string.ok, okClickListener);
        UiHelper.showAlertWithSoftKeyboard(builder);
    }

    public void actionPersonStopsDisturbing(Seat seat) {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(false);
        logEventWriter.logStopsDisturbingPerson(p);
    }

    public void actionSetPersonProperties(Seat seat) {
        final Person person = (Person) seat.getSeatTaker();
        PersonDialogFragment dialog = PersonDialogFragment.newInstance(person);
        dialog.setOkClickListener(new PersonDialogFragment.PositiveClickListener() {
            @Override
            public void onPersonDialogOkClick(Gender gender, AgeGroup ageGroup) {
                person.setGender(gender);
                person.setAgeGroup(ageGroup);
                person.save();
            }
        });
        dialog.show(hostFragment.getActivity().getSupportFragmentManager(), PersonDialogFragment.FRAGMENT_TAG);
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

    public void actionDefineGroup() {
        if (pendingAction instanceof DefineGroupAction) {
            Log.d(TAG, "finish defining group");
            ((DefineGroupAction) pendingAction).setCommonGroupForSelectedPersons();
        } else {
            Log.d(TAG, "starting defining group");
            pendingAction = new DefineGroupAction(this);
        }
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

    public void seatSelected(Seat seat) {
        pendingAction.seatSelected(seat);
    }

    public boolean isActionPending() {
        return pendingAction != null;
    }

    private void removeBaggageForPerson(final Person person) {
        for (Seat seat : getSeatsOfSeatsFragments()) {
            SeatTaker seatTaker = seat.getSeatTaker();
            if (seatTaker instanceof HandBaggage && ((HandBaggage) seatTaker).getOwner() == person) {
                actionRemoveBaggage(seat);
            }
        }
    }

    private List<Seat> getSeatsOfSeatsFragments() {
        GridView gridView = (GridView) hostFragment.getView();
        FloorRectAdapter adapter = (FloorRectAdapter) gridView.getAdapter();
        return adapter.getSeats();
    }

    private void removeBaggageIfAny(Seat seat) {
        if (seat.getSeatTaker() instanceof HandBaggage) {
            actionRemoveBaggage(seat);
        }
    }

    private class DisturbingReasonOkClickListener implements DialogInterface.OnClickListener {
        private Person p;
        private EditText edit;
        public DisturbingReasonOkClickListener(Person p, EditText edit) {
            this.p = p;
            this.edit = edit;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            p.setDisturbing(true);
            logEventWriter.logDisturbingPerson(p, edit.getText().toString());
        }
    }

}
