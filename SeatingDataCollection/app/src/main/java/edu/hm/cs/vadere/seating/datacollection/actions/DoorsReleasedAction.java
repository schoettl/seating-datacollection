package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class DoorsReleasedAction extends Action {
    private long logEventId;
    protected DoorsReleasedAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainEvent(LogEventType.DOOR_RELEASE);
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        deleteLogEvent(logEventId);
    }

}
