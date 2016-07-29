package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class TrainStartsAction extends Action {
    private long logEventId;

    public TrainStartsAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainEvent(LogEventType.TRAIN_STARTS);
    }

    @Override
    public void undo() {
        deleteLogEvent(logEventId);
    }

}
