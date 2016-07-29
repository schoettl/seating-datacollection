package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class PlaceBaggageAction extends PendingAction {
    private Person person;
    private Seat otherSeat;

    public PlaceBaggageAction(ActionManager actionManager, Person person) {
        super(actionManager);
        this.person = person;
    }

    @Override
    public void seatSelected(Seat seat) {
        this.otherSeat = seat;
        clearThisPendingAction();
        perform();
    }

    @Override
    public void perform() {
        if (getActionManager().isSeatOccupiedByPerson(otherSeat)) {
            getActionManager().showError(R.string.error_seat_occupied_by_person);
            return;
        }

        HandBaggage baggage = new HandBaggage(person);
        otherSeat.setSeatTaker(baggage);
        getActionManager().logEventWriter.logSeatEvent(LogEventType.PLACE_BAGGAGE, otherSeat, person);
    }
}
