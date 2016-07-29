package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.OnOptionsMenuInvalidatedListener;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.PendingAction;

public class MarkAgentAction extends PendingAction {
    private final Survey survey;
    private final OnOptionsMenuInvalidatedListener optionsMenuInvalidatedListener;

    public MarkAgentAction(ActionManager actionManager, Survey survey, OnOptionsMenuInvalidatedListener invalidatedListener) {
        super(actionManager);
        this.survey = survey;
        this.optionsMenuInvalidatedListener = invalidatedListener;
    }

    @Override
    public void seatSelected(Seat seat) {
        getActionManager().finishActionMarkAgent(survey, seat, optionsMenuInvalidatedListener);
    }
}
