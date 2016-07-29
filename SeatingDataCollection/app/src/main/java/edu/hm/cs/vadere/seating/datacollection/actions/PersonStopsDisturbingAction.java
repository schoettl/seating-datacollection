package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class PersonStopsDisturbingAction extends Action {
    private final Person person;
    private long logEventId;

    public PersonStopsDisturbingAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.person = (Person) seat.getSeatTaker();
    }

    @Override
    public void perform() {
        person.setDisturbing(false);
        logEventId = getLogEventWriter().logStopsDisturbingPerson(person);
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        person.setDisturbing(true);
        deleteLogEvent(logEventId);
    }
}
