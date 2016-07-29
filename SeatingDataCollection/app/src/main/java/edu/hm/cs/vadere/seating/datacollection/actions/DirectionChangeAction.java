package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class DirectionChangeAction extends Action {
    private long logEventId;

    public DirectionChangeAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainEvent(LogEventType.DIRECTION_CHANGE);
    }

    @Override
    public void undo() throws UnsupportedOperationException {
        deleteLogEvent(logEventId);
    }
}
