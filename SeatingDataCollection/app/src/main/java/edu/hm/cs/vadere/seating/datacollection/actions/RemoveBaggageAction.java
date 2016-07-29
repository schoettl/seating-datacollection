package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class RemoveBaggageAction extends Action {
    private Seat seat;
    protected RemoveBaggageAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        HandBaggage b = (HandBaggage) seat.getSeatTaker();
        seat.clearSeat();
        getLogEventWriter().logSeatEvent(LogEventType.REMOVE_BAGGAGE, seat, b.getOwner());
    }
}
