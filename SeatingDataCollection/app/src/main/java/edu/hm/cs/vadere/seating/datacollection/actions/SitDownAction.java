package edu.hm.cs.vadere.seating.datacollection.actions;

import android.util.Log;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class SitDownAction extends Action {
    private Seat seat;
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

        Person person = new Person();
        person.save();
        getActionManager().removeBaggageIfAny(seat);
        seat.setSeatTaker(person);
        getLogEventWriter().logSeatEvent(LogEventType.SIT_DOWN, seat, person);
    }
}

