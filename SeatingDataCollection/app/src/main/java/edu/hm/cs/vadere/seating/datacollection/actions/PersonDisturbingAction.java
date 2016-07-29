package edu.hm.cs.vadere.seating.datacollection.actions;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class PersonDisturbingAction extends Action {
    private Person person;
    private long logEventId;

    public PersonDisturbingAction(ActionManager actionManager, Person person) {
        super(actionManager);
        this.person = person;
    }

    @Override
    public void perform() {
        final EditText editTextReason = new EditText(getActionManager().getSeatsFragment().getContext());
        final DisturbingReasonOkClickListener okClickListener = new DisturbingReasonOkClickListener(editTextReason);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActionManager().getSeatsFragment().getContext());
        builder.setTitle(R.string.dialog_disturbing_reason);
        builder.setView(editTextReason);
        builder.setPositiveButton(R.string.dialog_ok, okClickListener);
        UiHelper.createAndShowAlertWithSoftKeyboard(builder);

    }

    private class DisturbingReasonOkClickListener implements DialogInterface.OnClickListener {
        private EditText edit;
        public DisturbingReasonOkClickListener(EditText edit) {
            this.edit = edit;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            person.setDisturbing(true);
            logEventId = getLogEventWriter().logDisturbingPerson(person, edit.getText().toString());
        }
    }

    @Override
    public void undo() {
        person.setDisturbing(false);
        deleteLogEvent(logEventId);
    }

}
