package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class ChangeSeatAction extends PendingAction {
    private Seat oldSeat;
    private Seat newSeat;

    public ChangeSeatAction(ActionManager actionManager, Seat oldSeat) {
        super(actionManager);
        this.oldSeat = oldSeat;
    }

    @Override
    public void seatSelected(Seat newSeat) {
        if (getActionManager().isSeatOccupiedByPerson(newSeat)) {
            getActionManager().showError(R.string.error_seat_occupied_by_person);
            return;
        }
        this.newSeat = newSeat;
        clearThisPendingAction();
        perform();
    }

    @Override
    public void perform() {
        Person person = (Person) oldSeat.getSeatTaker();
        oldSeat.clearSeat();

        getActionManager().removeBaggageIfAny(newSeat);
        newSeat.setSeatTaker(person);
        getActionManager().logEventWriter.logSeatEvent(LogEventType.CHANGE_SEAT, newSeat, person);
    }

}
