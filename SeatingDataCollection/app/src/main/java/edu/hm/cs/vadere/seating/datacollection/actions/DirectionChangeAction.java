package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class DirectionChangeAction extends Action {
    private final SeatsFragment.Direction newDirection;
    private final DirectionChangeListener listener;
    private long logEventId;

    public DirectionChangeAction(ActionManager actionManager, SeatsFragment.Direction newDirection, DirectionChangeListener listener) {
        super(actionManager);
        this.newDirection = newDirection;
        this.listener = listener;
    }

    @Override
    public void perform() {
        logEventId = getLogEventWriter().logTrainDirectionEvent(LogEventType.DIRECTION_CHANGE, newDirection);
        listener.onDirectionChange(newDirection);
    }

    @Override
    public void undo() {
        deleteLogEvent(logEventId);
        listener.onDirectionChange(newDirection.invert());
    }

    public interface DirectionChangeListener {
        void onDirectionChange(SeatsFragment.Direction newDirection);
    }

}
