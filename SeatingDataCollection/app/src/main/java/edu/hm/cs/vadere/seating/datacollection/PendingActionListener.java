package edu.hm.cs.vadere.seating.datacollection;

public interface PendingActionListener {
    void onPendingActionFinished(boolean wasCanceled);
}
