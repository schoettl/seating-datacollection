package edu.hm.cs.vadere.seating.datacollection.seats;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class ChangeSeatAction extends PendingAction {
    private Seat oldSeat;

    public ChangeSeatAction(ActionManager actionManager, Seat oldSeat) {
        super(actionManager);
        this.oldSeat = oldSeat;
    }

    @Override
    public void seatSelected(Seat newSeat) {
        getActionManager().finishActionChangeSeat(oldSeat, newSeat);
        clearThisPendingAction();
    }
}
