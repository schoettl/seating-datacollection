package edu.hm.cs.vadere.seating.datacollection.actions;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;

public class PersonDisturbingAction extends Action {
    private final Seat seat;

    protected PersonDisturbingAction(ActionManager actionManager, Seat seat) {
        super(actionManager);
        this.seat = seat;
    }

    @Override
    public void perform() {
        Person p = (Person) seat.getSeatTaker();
        // Action is done in the okClickListener

        final EditText editTextReason = new EditText(getActionManager().hostFragment.getContext());
        final DisturbingReasonOkClickListener okClickListener = new DisturbingReasonOkClickListener(p, editTextReason);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActionManager().hostFragment.getContext());
        builder.setTitle(R.string.dialog_disturbing_reason);
        builder.setView(editTextReason);
        builder.setPositiveButton(R.string.dialog_ok, okClickListener);
        UiHelper.createAndShowAlertWithSoftKeyboard(builder);

    }

    private class DisturbingReasonOkClickListener implements DialogInterface.OnClickListener {
        private Person p;
        private EditText edit;
        public DisturbingReasonOkClickListener(Person p, EditText edit) {
            this.p = p;
            this.edit = edit;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            p.setDisturbing(true);
            getActionManager().logEventWriter.logDisturbingPerson(p, edit.getText().toString());
        }
    }

}
