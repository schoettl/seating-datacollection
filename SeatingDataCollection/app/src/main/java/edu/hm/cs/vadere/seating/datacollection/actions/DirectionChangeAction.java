package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.Direction;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;

public class DirectionChangeAction extends Action {
    private final Direction newDirection;
    private final Direction.DirectionChangeListener listener;
    private long logEventId;

    public DirectionChangeAction(ActionManager actionManager, Direction newDirection, Direction.DirectionChangeListener listener) {
        super(actionManager);
        this.newDirection = newDirection;
        this.listener = listener;
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainDirectionEvent(LogEventType.DIRECTION_CHANGE, newDirection);
        listener.onDirectionChanged(newDirection);
    }

    @Override
    public void undo() {
        deleteLogEvent(logEventId);
        listener.onDirectionChanged(newDirection.invert());
    }

}
