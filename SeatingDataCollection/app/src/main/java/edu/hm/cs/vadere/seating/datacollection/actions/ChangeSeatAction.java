package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.SeatWidget;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class ChangeSeatAction extends PendingAction {
    private Seat seat;

    public ChangeSeatAction(LogEventWriter logEventWriter, Seat seat) {
        super(logEventWriter);
        this.seat = seat;
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        Seat newSeat = seatWidget.getSeat();
        Person person = (Person) seat.getSeatTaker();
        seat.clearSeat();
        newSeat.setSeatTaker(person);
        getLogEventWriter().logSeatEvent(LogEventType.CHANGE_SEAT, newSeat, person);
    }
}
