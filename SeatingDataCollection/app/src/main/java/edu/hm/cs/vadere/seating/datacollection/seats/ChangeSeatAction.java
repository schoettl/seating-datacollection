package edu.hm.cs.vadere.seating.datacollection.seats;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class ChangeSeatAction extends PendingAction {
    private Seat seat;

    public ChangeSeatAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        Seat newSeat = seatWidget.getSeat();
        getActionManager().finishActionChangeSeat(seat, newSeat);
    }
}
