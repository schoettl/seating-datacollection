package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.TrainState;

public class DoorsReleasedAction extends Action {
    private long logEventId;
    private final TrainState.TrainStateListener listener;

    public DoorsReleasedAction(ActionManager actionManager, TrainState.TrainStateListener listener) {
        super(actionManager);
        this.listener = listener;
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainEvent(LogEventType.DOOR_RELEASE);
        listener.onTrainStateChanged(TrainState.HALTING);
    }

    @Override
    public void undo() {
        deleteLogEvent(logEventId);
        listener.onTrainStateChanged(TrainState.MOVING);
    }

}
