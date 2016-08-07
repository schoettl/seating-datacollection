package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class DirectionChangeAction extends Action {
    private final DirectionChangeListener listener;
    private long logEventId;

    public DirectionChangeAction(ActionManager actionManager, DirectionChangeListener listener) {
        super(actionManager);
        this.listener = listener;
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainEvent(LogEventType.DIRECTION_CHANGE);
        listener.onDirectionChange();
    }

    @Override
    public void undo() {
        deleteLogEvent(logEventId);
        listener.onDirectionChange();
    }

    public interface DirectionChangeListener {
        void onDirectionChange();
    }

}
