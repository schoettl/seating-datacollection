package edu.hm.cs.vadere.seating.datacollection.actions;

import edu.hm.cs.vadere.seating.datacollection.PersonDialogFragment;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class UpdatePersonPropertiesAction extends Action {
    private final Person person;

    public UpdatePersonPropertiesAction(ActionManager actionManager, Person person) {
        super(actionManager);
        this.person = person;
    }

    @Override
    public void perform() {
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

    @Override
    public void undo() throws UnsupportedOperationException {
        UiHelper.showInfoDialog(getActionManager().hostFragment.getActivity(), R.string.dialog_info_undo_update_person);
    }
}
