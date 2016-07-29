package edu.hm.cs.vadere.seating.datacollection.actions;

import android.util.Log;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SitDownAction extends Action {
    private Seat seat;
    private Person person;
    private long logEventId;

    protected SitDownAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        if (getActionManager().isSeatOccupiedByPerson(seat)) {
            Log.wtf("SitDownAction", "it should not be possible to trigger this action under this circumstances");
            return;
        }

        person = new Person();
        person.save();
        getActionManager().removeBaggageIfAny(seat);
        seat.setSeatTaker(person);
        logEventId = getLogEventWriter().logSeatEvent(LogEventType.SIT_DOWN, seat, person);
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        seat.clearSeat();
        person.delete();
        deleteLogEvent(logEventId);
        // Note: perform seems to be atomic but can consist of multiple actions (removeBaggeIfAny)
    }
}

