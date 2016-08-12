package edu.hm.cs.vadere.seating.datacollection.actions;

import android.support.annotation.NonNull;

import edu.hm.cs.vadere.seating.datacollection.model.MGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class AddToGroupAction extends PendingAction {
    private MGroup group;
    private Person personJoining = null;

    public AddToGroupAction(ActionManager actionManager, PendingActionListener listener, @NonNull MGroup group) {
        super(actionManager, listener);
        if (group == null)
            throw new IllegalArgumentException("group cannot be null");
        this.group = group;
    }

    @Override
    public void seatSelected(Seat seat) {
        SeatTaker seatTaker = seat.getSeatTaker();
        if (seatTaker instanceof Person) {
            personJoining = (Person) seatTaker;
            perform();
            clearThisPendingAction();
        }
    }

    @Override
    public void perform() {
        if (personJoining == null)
            throw new IllegalStateException("no joining person selected");
        personJoining.setGroup(group);
        firePendingActionFinished();
    }
}
