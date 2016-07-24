package edu.hm.cs.vadere.seating.datacollection.seats;

public abstract class PendingAction {
    public static final PendingAction NO_PENDING_ACTION = new NoPendingAction();

    private ActionManager actionManager;

    protected PendingAction(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    protected ActionManager getActionManager() {
        return actionManager;
    }

    public boolean isActionPending() {
        return this.getClass() != NO_PENDING_ACTION.getClass();
    }

    public abstract void seatSelected(SeatWidget seatWidget);
}
