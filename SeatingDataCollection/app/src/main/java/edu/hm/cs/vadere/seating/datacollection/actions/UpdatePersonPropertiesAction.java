package edu.hm.cs.vadere.seating.datacollection.actions;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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
        AlertDialog ageGroupDialog = createAgeGroupDialog();
        AlertDialog genderDialog = createGenderDialog(ageGroupDialog);
        genderDialog.show(); // genderDialog starts ageGroupDialog
    }

    private AlertDialog createAgeGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_age_group);
        builder.setSingleChoiceItems(getDialogItems(AgeGroup.values()), person.getAgeGroup().ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != -1) {
                    person.setAgeGroup(AgeGroup.values()[which]);
                    person.save();
                }
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private AlertDialog createGenderDialog(final AlertDialog ageGroupDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_gender);
        builder.setSingleChoiceItems(getDialogItems(Gender.values()), person.getGender().ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != -1) {
                    person.setGender(Gender.values()[which]);
                    person.save();
                }
                dialog.dismiss();
                ageGroupDialog.show();
            }
        });
        return builder.create();
    }

    private <E> String[] getDialogItems(E[] values) {
//        return new ArrayAdapter<>(getContext(), R.layout.item_spinner, values); // I want to use the standard layout!
        // What I want to do: convert object array to string array
        // In Haskell: map toString values
        // In Java: http://stackoverflow.com/questions/1018750/how-to-convert-object-array-to-string-array-in-java
//        return Arrays.copyOf(values, values.length, String[].class); // does not work on Android (ArrayStoreException)
//        return Arrays.asList(values).toArray(new String[values.length]); // does not work on Android (ArrayStoreException)
        // Solution:
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].toString();
        }
        return result;
    }

    private Context getContext() {
        return getActionManager().getSeatsFragment().getContext();
    }

    @Override
    public void undo() {
        UiHelper.showInfoDialog(getActionManager().getSeatsFragment().getActivity(), R.string.dialog_info_undo_update_person);
    }
}
