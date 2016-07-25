package edu.hm.cs.vadere.seating.datacollection.seats;

import java.util.HashSet;
import java.util.Set;

import edu.hm.cs.vadere.seating.datacollection.model.MGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class DefineGroupAction extends PendingAction {
    private Set<Person> persons = new HashSet<>();

    public DefineGroupAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        SeatTaker seatTaker = seatWidget.getSeat().getSeatTaker();
        if (seatTaker instanceof Person) {
            persons.add((Person) seatTaker);
        }
    }

    public void setCommonGroupForSelectedPersons() {
        // Simple algorithm:
        // Create new group for selected persons, overwrite old group (if any).
        // Leave old group for not selected persons.
        MGroup group = new MGroup();
        group.save();
        for (Person p : persons) {
            p.setGroup(group);
            p.save();
        }

        clearThisPendingAction();
    }
}
