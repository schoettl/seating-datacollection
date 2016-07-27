package edu.hm.cs.vadere.seating.datacollection.seats;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class MarkAgentAction extends PendingAction {
    private final Survey survey;

    public MarkAgentAction(ActionManager actionManager, Survey survey) {
        super(actionManager);
        this.survey = survey;
    }

    @Override
    public void seatSelected(Seat seat) {
        getActionManager().finishActionMarkAgent(survey, seat);
    }
}
