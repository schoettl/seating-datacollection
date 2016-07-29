package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class TrainStartsAction extends Action {
    protected TrainStartsAction(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public void perform() {
        getLogEventWriter().logTrainEvent(LogEventType.TRAIN_STARTS);
    }
}
