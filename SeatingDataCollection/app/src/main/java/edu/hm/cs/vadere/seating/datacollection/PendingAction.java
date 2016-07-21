package edu.hm.cs.vadere.seating.datacollection;

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
