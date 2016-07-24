package edu.hm.cs.vadere.seating.datacollection.seats;

public class NoPendingAction extends PendingAction {
    public NoPendingAction() {
        super(null);
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        throw new UnsupportedOperationException();
    }
}
