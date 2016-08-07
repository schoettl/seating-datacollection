package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.MGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class RemoveFromGroupAction extends Action {
    private final Person person;
    private MGroup groupBefore;

    public RemoveFromGroupAction(ActionManager actionManager, Person person) {
        super(actionManager);
        this.person = person;
    }

    @Override
    public void perform() {
        groupBefore = person.getGroup();
        person.setGroup(null);
        person.save();
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        person.setGroup(groupBefore);
        person.save();
    }
}
