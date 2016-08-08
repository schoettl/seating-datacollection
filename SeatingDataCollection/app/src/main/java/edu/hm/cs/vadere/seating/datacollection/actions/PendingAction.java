package edu.hm.cs.vadere.seating.datacollection.actions;

import android.util.Log;

import edu.hm.cs.vadere.seating.datacollection.PendingActionListener;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public abstract class PendingAction extends Action {
    private static final String TAG = "PendingAction";
    private final PendingActionListener listener;

    protected PendingAction(ActionManager actionManager, PendingActionListener listener) {
        super(actionManager);
        this.listener = listener;
    }

    @Override
    public final void undo() throws UnsupportedOperationException {
        // Actions that are still pending are easy to undo (= cancel)
        if (getActionManager().isActionPending(this)) {
            getActionManager().clearPendingAction();
            firePendingActionCanceled();
        } else {
            undoFinishedAction();
        }
    }

    /** Please override this in subclasses. */
    protected void undoFinishedAction() {
        throw new UnsupportedOperationException("undo not supported for pending action");
    }

    public final void clearThisPendingAction() {
        Log.d(TAG, "clearing pending action");
        getActionManager().clearPendingAction();
    }

    public final void cancelThisPendingAction() {
        getActionManager().cancelPendingAction();
    }

    public final boolean isThisActionPending() {
        return getActionManager().isActionPending(this);
    }

    public abstract void seatSelected(Seat seat);

    protected final void firePendingActionFinished() {
        listener.onPendingActionFinished(false);
    }

    protected final void firePendingActionCanceled() {
        listener.onPendingActionFinished(true);
    }

}
