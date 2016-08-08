package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.TrainState;

public class TrainStartsAction extends Action {
    private long logEventId;
    private final TrainState.TrainStateListener listener;

    public TrainStartsAction(ActionManager actionManager, TrainState.TrainStateListener listener) {
        super(actionManager);
        this.listener = listener;
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainEvent(LogEventType.TRAIN_STARTS);
        listener.onTrainStateChanged(TrainState.MOVING);
    }

    @Override
    public void undo() {
        deleteLogEvent(logEventId);
        listener.onTrainStateChanged(TrainState.HALTING);
    }

}
