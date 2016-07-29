package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.PersonDialogFragment;
import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class UpdatePersonPropertiesAction extends Action {
    private final Seat seat;

    public UpdatePersonPropertiesAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        final Person person = (Person) seat.getSeatTaker();
        PersonDialogFragment dialog = PersonDialogFragment.newInstance(person);
        dialog.setOkClickListener(new PersonDialogFragment.PositiveClickListener() {
            @Override
            public void onPersonDialogOkClick(Gender gender, AgeGroup ageGroup) {
                person.setGender(gender);
                person.setAgeGroup(ageGroup);
                person.save();
            }
        });
        dialog.show(getActionManager().hostFragment.getActivity().getSupportFragmentManager(), PersonDialogFragment.FRAGMENT_TAG);
    }
}
