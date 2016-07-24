package edu.hm.cs.vadere.seating.datacollection.seats;

import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class MarkAgentAction extends PendingAction {
    private final Survey survey;

    public MarkAgentAction(ActionManager actionManager, Survey survey) {
        super(actionManager);
        this.survey = survey;
    }

    @Override
    public void seatSelected(SeatWidget seatWidget) {
        Person p = (Person) seatWidget.getSeat().getSeatTaker();
        getActionManager().finishActionMarkAgent(survey, p);
    }
}
