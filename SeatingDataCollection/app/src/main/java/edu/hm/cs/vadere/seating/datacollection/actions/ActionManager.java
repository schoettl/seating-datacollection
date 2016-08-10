package edu.hm.cs.vadere.seating.datacollection.actions;

import android.app.Activity;
import android.util.Log;
import android.widget.GridView;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.model.Direction;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.model.TrainState;
import edu.hm.cs.vadere.seating.datacollection.seats.FloorRectAdapter;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class ActionManager {
    private static final String TAG = "ActionManager";
    final SeatsFragment seatsFragment;
    final LogEventWriter logEventWriter;
    private PendingAction pendingAction = null;
    private Stack<Action> actionStack = new Stack<>();

    public ActionManager(SeatsFragment seatsFragment, LogEventWriter logEventWriter) {
        this.logEventWriter = logEventWriter;
        this.seatsFragment = seatsFragment;
    }

    public void cancelPendingAction() {
        if (pendingAction == actionStack.peek()) {
            actionStack.pop();
            clearPendingAction();
        }
    }

    public void clearPendingAction() {
        pendingAction = null;
    }

    public void actionPersonDisturbing(Seat seat) {
        Action action = new PersonDisturbingAction(this, (Person) seat.getSeatTaker());
        performActionAndAddToStack(action);
    }

    public void actionPersonStopsDisturbing(Seat seat) {
        Action action = new PersonStopsDisturbingAction(this, (Person) seat.getSeatTaker());
        performActionAndAddToStack(action);
    }

    public void actionSetPersonProperties(Seat seat) {
        Action action = new UpdatePersonPropertiesAction(this, (Person) seat.getSeatTaker());
        performActionAndAddToStack(action);
    }

    public void actionPlaceBaggage(Seat seat, PendingAction.PendingActionListener listener) {
        addPendingActionToStack(new PlaceBaggageAction(this, (Person) seat.getSeatTaker(), listener));
    }

    public void actionRemoveBaggage(Seat seat) {
        Action action = new RemoveBaggageAction(this, seat);
        performActionAndAddToStack(action);
    }

    public void actionSitDown(Seat seat) {
        Action action = new SitDownAction(this, seat);
        performActionAndAddToStack(action);
    }

    public void actionSitDownAndPlaceBaggage(Seat seat, PendingAction.PendingActionListener listener) {
        performActionAndAddToStack(new SitDownAction(this, seat));
        addPendingActionToStack(new PlaceBaggageAction(this, (Person) seat.getSeatTaker(), listener));
    }

    void showError(int message) {
        UiHelper.showErrorToast(seatsFragment.getContext(), message);
    }

    boolean isSeatOccupiedByPerson(Seat seat) {
        return (seat.getSeatTaker() instanceof Person);
    }

    public void actionChangeSeat(Seat seat, PendingAction.PendingActionListener listener) {
        addPendingActionToStack(new ChangeSeatAction(this, seat, listener));
    }

    public void actionLeave(Seat seat) {
        Action action = new LeaveAction(this, seat);
        performActionAndAddToStack(action);
    }

    public void actionDefineGroup(PendingAction.PendingActionListener listener) {
        if (isActionPending(DefineGroupAction.class)) {
            Log.d(TAG, "finish defining group");
            pendingAction.perform();
        } else {
            Log.d(TAG, "starting defining group");
            addPendingActionToStack(new DefineGroupAction(this, listener));
        }
    }

    public void actionMarkAgent(Survey survey, PendingAction.PendingActionListener listener) {
        if (isActionPending(MarkAgentAction.class)) {
            clearPendingAction();
        } else {
            addPendingActionToStack(new MarkAgentAction(this, survey, listener));
        }
    }

    public void actionRemoveFromGroup(Seat seat) {
        performActionAndAddToStack(new RemoveFromGroupAction(this, (Person) seat.getSeatTaker()));
    }

    public void seatSelected(Seat seat) {
        pendingAction.seatSelected(seat);
    }

    public boolean isActionPending() {
        return pendingAction != null;
    }

    public boolean isActionPending(Class<? extends PendingAction> pendingActionClass) {
        return pendingAction != null && pendingAction.getClass() == pendingActionClass;
    }

    public boolean isActionPending(PendingAction otherAction) {
        return pendingAction == otherAction;
    }

    void removeBaggageForPerson(final Person person) {
        for (Seat seat : getSeatsOfSeatsFragments()) {
            SeatTaker seatTaker = seat.getSeatTaker();
            if (seatTaker instanceof HandBaggage && ((HandBaggage) seatTaker).getOwner() == person) {
                actionRemoveBaggage(seat);
            }
        }
    }

    List<Seat> getSeatsOfSeatsFragments() {
        GridView gridView = seatsFragment.getGridView();
        FloorRectAdapter adapter = (FloorRectAdapter) gridView.getAdapter();
        return adapter.getSeats();
    }

    void removeBaggageIfAny(Seat seat) {
        if (seat.getSeatTaker() instanceof HandBaggage) {
            actionRemoveBaggage(seat);
        }
    }

    private void performActionAndAddToStack(Action action) {
        action.perform();
        actionStack.push(action);
    }

    private void addPendingActionToStack(PendingAction action) {
        actionStack.push(action);
        pendingAction = action;
        UiHelper.showInfoToast(seatsFragment.getContext(), R.string.info_pending_action);
    }

    public boolean tryUndoLastAction() {
        try {
            Action action = actionStack.peek();
            action.undo();
            actionStack.pop();
            return true;
        } catch (EmptyStackException | UnsupportedOperationException e) {
            return false;
        }
    }

    public void actionDirectionChange(Direction.DirectionChangeListener listener, Direction newDirection) {
        performActionAndAddToStack(new DirectionChangeAction(this, newDirection, listener));
    }

    public void actionDoorsReleased(TrainState.TrainStateListener listener) {
        performActionAndAddToStack(new DoorsReleasedAction(this, listener));
    }

    public void actionTrainStarts(TrainState.TrainStateListener listener) {
        performActionAndAddToStack(new TrainStartsAction(this, listener));
    }

    public void actionCountStandingPersons(Activity activity) {
        performActionAndAddToStack(new CountStandingPersonsAction(this, activity));
    }

    public SeatsFragment getSeatsFragment() {
        return seatsFragment;
    }

}
