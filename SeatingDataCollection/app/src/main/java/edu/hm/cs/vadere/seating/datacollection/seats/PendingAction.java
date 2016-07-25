package edu.hm.cs.vadere.seating.datacollection.seats;

import android.util.Log;

public abstract class PendingAction {
    private static final String TAG = "PendingAction";
    private ActionManager actionManager;

    protected PendingAction(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    protected ActionManager getActionManager() {
        return actionManager;
    }

    public void clearThisPendingAction() {
        Log.d(TAG, "clearing pending action");
        actionManager.clearPendingAction();
    }

    public abstract void seatSelected(SeatWidget seatWidget);
}
