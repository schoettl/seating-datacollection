package edu.hm.cs.vadere.seating.datacollection.actions;

import android.util.Log;
import android.widget.GridView;

import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.OnOptionsMenuInvalidatedListener;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.FloorRectAdapter;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class ActionManager {
    private static final String TAG = "ActionManager";
    final SeatsFragment hostFragment;
    final LogEventWriter logEventWriter;
    private PendingAction pendingAction = null;

    public ActionManager(SeatsFragment hostFragment, LogEventWriter logEventWriter) {
        this.logEventWriter = logEventWriter;
        this.hostFragment = hostFragment;
    }

    public void clearPendingAction() {
        pendingAction = null;
    }

    public void actionPersonDisturbing(Seat seat) {
        Action action = new PersonDisturbingAction(this, seat);
        action.perform();
    }

    public void actionPersonStopsDisturbing(Seat seat) {
        Action action = new PersonStopsDisturbingAction(this, seat);
        action.perform();
    }

    public void actionSetPersonProperties(Seat seat) {
        Action action = new UpdatePersonPropertiesAction(this, seat);
        action.perform();
    }

    public void actionPlaceBaggage(Seat seat) {
        pendingAction = new PlaceBaggageAction(this, (Person) seat.getSeatTaker());
    }

    public void actionRemoveBaggage(Seat seat) {
        Action action = new RemoveBaggageAction(this, seat);
        action.perform();
    }

    public void actionSitDown(Seat seat) {
        Action action = new SitDownAction(this, seat);
        action.perform();
    }

    void showError(int message) {
        UiHelper.showErrorToast(hostFragment.getContext(), message);
    }

    boolean isSeatOccupiedByPerson(Seat seat) {
        return (seat.getSeatTaker() instanceof Person);
    }

    public void actionChangeSeat(Seat seat) {
        pendingAction = new ChangeSeatAction(this, seat);
    }

    public void actionLeave(Seat seat) {
        Action action = new LeaveAction(this, seat);
        action.perform();
    }

    public void actionDefineGroup() {
        if (isActionPending(DefineGroupAction.class)) {
            Log.d(TAG, "finish defining group");
            pendingAction.perform();
            hostFragment.onOptionsMenuInvalidated(); // TODO onOptionsMenuInvalidated - better call in action itself? How did I do it in the MarkAgentAction?
        } else {
            Log.d(TAG, "starting defining group");
            pendingAction = new DefineGroupAction(this);
            hostFragment.onOptionsMenuInvalidated();
        }
    }

    public void actionMarkAgent(Survey survey, OnOptionsMenuInvalidatedListener invalidatedListener) {
        if (!isActionPending(MarkAgentAction.class)) {
            pendingAction = new MarkAgentAction(this, survey, invalidatedListener);
        } else {
            clearPendingAction();
        }
        invalidatedListener.onOptionsMenuInvalidated();
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
        GridView gridView = (GridView) hostFragment.getView();
        FloorRectAdapter adapter = (FloorRectAdapter) gridView.getAdapter();
        return adapter.getSeats();
    }

    void removeBaggageIfAny(Seat seat) {
        if (seat.getSeatTaker() instanceof HandBaggage) {
            actionRemoveBaggage(seat);
        }
    }

}
