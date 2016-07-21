package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.SeatWidget;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class PlaceBaggageAction extends PendingAction {
    private Person person;

    public PlaceBaggageAction(LogEventWriter logEventWriter, Person person) {
        super(logEventWriter);
        this.person = person;
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        Seat otherSeat = seatWidget.getSeat();
        HandBaggage baggage = new HandBaggage(person);
        otherSeat.setSeatTaker(baggage);
        getLogEventWriter().logSeatEvent(LogEventType.PLACE_BAGGAGE, otherSeat, person);

    }
}
