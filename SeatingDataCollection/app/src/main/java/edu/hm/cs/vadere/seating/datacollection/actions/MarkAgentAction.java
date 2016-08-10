package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class MarkAgentAction extends PendingAction {
    private final Survey survey;
    private Person person;

    public MarkAgentAction(ActionManager actionManager, Survey survey, PendingActionListener listener) {
        super(actionManager, listener);
        this.survey = survey;
    }

    @Override
    public void seatSelected(Seat seat) {
        SeatTaker seatTaker = seat.getSeatTaker();
        if (seatTaker instanceof Person) {
            this.person = (Person) seatTaker;
            perform();
            clearThisPendingAction();
        } else {
            getActionManager().showError(R.string.error_please_select_a_person);
        }
    }

    @Override
    public void perform() {
        makeNoPersonBeingAgent();
        person.setAgent(true);
        survey.setAgent(person);
        survey.save();
        firePendingActionFinished();
    }

    @Override
    protected void undoFinishedAction() {
        makeNoPersonBeingAgent();
        survey.setAgent(null);
        survey.save();
    }

    private void makeNoPersonBeingAgent() {
        for (Seat s : getActionManager().getSeatsOfSeatsFragments()) {
            if (s.getSeatTaker() instanceof Person) {
                ((Person) s.getSeatTaker()).setAgent(false);
            }
        }
    }

}
