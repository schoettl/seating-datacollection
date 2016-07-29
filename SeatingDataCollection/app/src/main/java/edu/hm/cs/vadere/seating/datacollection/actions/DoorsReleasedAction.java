package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class DoorsReleasedAction extends Action {
    protected DoorsReleasedAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void perform() {
        getLogEventWriter().logTrainEvent(LogEventType.DOOR_RELEASE);
    }
}
