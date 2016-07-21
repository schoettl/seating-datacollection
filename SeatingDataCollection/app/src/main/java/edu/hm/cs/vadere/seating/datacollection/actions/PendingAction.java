package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.SeatWidget;

public abstract class PendingAction {
    public static final PendingAction NO_PENDING_ACTION = new NoPendingAction();

    private LogEventWriter logEventWriter;

    protected PendingAction(LogEventWriter logEventWriter) {
        this.logEventWriter = logEventWriter;
    }

    protected LogEventWriter getLogEventWriter() {
        return logEventWriter;
    }

    public boolean isActionPending() {
        return this.getClass() != NO_PENDING_ACTION.getClass();
    }

    public abstract void seatSelected(SeatWidget seatWidget);
}
