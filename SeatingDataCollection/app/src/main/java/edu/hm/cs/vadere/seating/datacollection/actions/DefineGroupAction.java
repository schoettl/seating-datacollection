package edu.hm.cs.vadere.seating.datacollection.actions;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import edu.hm.cs.vadere.seating.datacollection.PendingActionListener;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.model.MGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class DefineGroupAction extends PendingAction {
    private static final String TAG = "DefineGroupAction";
    private Set<Person> persons = new HashSet<>();

    public DefineGroupAction(ActionManager actionManager, PendingActionListener listener) {
        super(actionManager, listener);
    }

    @Override
    public void perform() {
        if (persons.size() < 2) {
            UiHelper.showErrorToast(getActionManager().getSeatsFragment().getContext(), R.string.error_group_must_have_more);
            cancelThisPendingAction();
            firePendingActionCanceled();
        } else {
            setCommonGroupForSelectedPersons();
            clearThisPendingAction();
            firePendingActionFinished();
        }
    }

    @Override
    public void seatSelected(Seat seat) {
        SeatTaker seatTaker = seat.getSeatTaker();
        if (seatTaker instanceof Person) {
            Log.d(TAG, "adding person to future group");
            persons.add((Person) seatTaker);
        }
    }

    @Override
    protected void undoFinishedAction() {
        // This is not perfect. Previous groups are lost and have to be reassigned manually.
        for (Person p : persons) {
            p.setGroup(null);
            p.save();
        }
    }

    public void setCommonGroupForSelectedPersons() {

        // Simple algorithm:
        // Create new group for selected persons, overwrite old group (if any).
        // Leave old group for not selected persons.

        MGroup group = new MGroup();
        group.save();
        for (Person p : persons) {
            p.setGroup(group);
            p.save();
        }
    }
}
