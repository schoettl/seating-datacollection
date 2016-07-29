package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class PersonStopsDisturbingAction extends Action {
    private final Seat seat;

    protected PersonStopsDisturbingAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        Person p = (Person) seat.getSeatTaker();
        p.setDisturbing(false);
        getLogEventWriter().logStopsDisturbingPerson(p);
    }
}
