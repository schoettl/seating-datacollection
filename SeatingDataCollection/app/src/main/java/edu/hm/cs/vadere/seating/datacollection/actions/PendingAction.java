package edu.hm.cs.vadere.seating.datacollection.actions;

import android.util.Log;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public abstract class PendingAction extends Action {
    private static final String TAG = "PendingAction";

    protected PendingAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        // Actions that are still pending are easy to undo (= cancel)
        if (getActionManager().isActionPending(this)) {
            getActionManager().clearPendingAction();
        } else {
            undoFinishedAction();
        }
    }

    /** Please override this in subclasses. */
    protected void undoFinishedAction() {
        throw new UnsupportedOperationException("undo not supported for pending action");
    }

    public void clearThisPendingAction() {
        Log.d(TAG, "clearing pending action");
        getActionManager().clearPendingAction();
    }

    public boolean isThisActionPending() {
        return getActionManager().isActionPending(this);
    }

    public abstract void seatSelected(Seat seat);
}
