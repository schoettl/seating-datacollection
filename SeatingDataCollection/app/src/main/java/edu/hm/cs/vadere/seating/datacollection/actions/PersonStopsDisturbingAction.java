package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class PersonStopsDisturbingAction extends Action {
    private final Person person;
    private long logEventId;

    public PersonStopsDisturbingAction(ActionManager actionManager, Person person) {
        super(actionManager);
        this.person = person;
    }

    @Override
    public void perform() {
        person.setDisturbing(false);
        logEventId = getLogEventWriter().logStopsDisturbingPerson(person);
    }

    @Override
    public void undo() {
        person.setDisturbing(true);
        deleteLogEvent(logEventId);
    }
}
