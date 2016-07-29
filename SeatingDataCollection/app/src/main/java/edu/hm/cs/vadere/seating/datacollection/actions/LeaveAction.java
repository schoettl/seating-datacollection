package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class LeaveAction extends Action {
    private final Seat seat;

    protected LeaveAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        Person person = (Person) seat.getSeatTaker();

        getActionManager().removeBaggageForPerson(person);

        seat.clearSeat();
        getLogEventWriter().logSeatEvent(LogEventType.LEAVE, seat, person);
    }
}
