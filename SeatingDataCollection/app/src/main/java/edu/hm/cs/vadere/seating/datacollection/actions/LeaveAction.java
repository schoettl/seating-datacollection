package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class LeaveAction extends Action {
    private final Seat seat;
    private Person person;
    private long logEventId;

    public LeaveAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        person = (Person) seat.getSeatTaker();

        getActionManager().removeBaggageForPerson(person);

        seat.clearSeat();
        logEventId = getLogEventWriter().logSeatEvent(LogEventType.LEAVE, seat, person);
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        seat.setSeatTaker(person);
        deleteLogEvent(logEventId);
    }

}
