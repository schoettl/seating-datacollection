package edu.hm.cs.vadere.seating.datacollection.seats;

public abstract class PendingAction {
    private ActionManager actionManager;

    protected PendingAction(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    protected ActionManager getActionManager() {
        return actionManager;
    }

    public void clearThisPendingAction() {
        actionManager.clearPendingAction();
    }

    public abstract void seatSelected(SeatWidget seatWidget);
}
