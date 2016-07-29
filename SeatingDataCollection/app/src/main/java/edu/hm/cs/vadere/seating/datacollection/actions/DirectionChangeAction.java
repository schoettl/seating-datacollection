package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class DirectionChangeAction extends Action {
    protected DirectionChangeAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void perform() {
        getLogEventWriter().logTrainEvent(LogEventType.DIRECTION_CHANGE);
    }
}
