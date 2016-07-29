package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.model.LogEvent;

public abstract class Action {
    private ActionManager actionManager;

    protected Action(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    protected ActionManager getActionManager() {
        return actionManager;
    }

    protected LogEventWriter getLogEventWriter() {
        return actionManager.logEventWriter;
    }

    public abstract void perform();

    public void undo() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("undo not supported");
    }

    protected void deleteLogEvent(long id) {
        LogEvent event = LogEvent.findById(LogEvent.class, id);
        event.delete();
    }

}
