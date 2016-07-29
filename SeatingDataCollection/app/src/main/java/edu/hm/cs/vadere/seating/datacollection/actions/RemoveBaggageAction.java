package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class RemoveBaggageAction extends Action {
    private Seat seat;
    private Person owner;
    private long logEventId;

    public RemoveBaggageAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        final HandBaggage baggage = (HandBaggage) seat.getSeatTaker();
        owner = baggage.getOwner();
        seat.clearSeat();
        logEventId = getLogEventWriter().logSeatEvent(LogEventType.REMOVE_BAGGAGE, seat, owner);
    }

    @Override
    public void undo() {
        seat.setSeatTaker(new HandBaggage(owner));
        deleteLogEvent(logEventId);
    }
}
