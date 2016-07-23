package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.SeatWidget;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class PlaceBaggageAction extends PendingAction {
    private Person person;

    public PlaceBaggageAction(ActionManager actionManager, Person person) {
        super(actionManager);
        this.person = person;
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        Seat otherSeat = seatWidget.getSeat();
        getActionManager().finishActionPlaceBaggage(person, otherSeat);
    }
}
