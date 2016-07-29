package edu.hm.cs.vadere.seating.datacollection.actions;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;

public class CountStandingPersonsAction extends Action {
    private final Activity activity;

    protected CountStandingPersonsAction(ActionManager actionManager, Activity activity) {
        super(actionManager);
        this.activity = activity;
    }

    @Override
    public void perform() {
        final EditText editTextCount = new EditText(activity);
        editTextCount.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_count_standing_persons);
        builder.setView(editTextCount);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int count = Integer.parseInt(editTextCount.getText().toString());
                    getLogEventWriter().logCountStandingPersons(count);
                } catch (NumberFormatException e) {
                    UiHelper.showErrorToast(activity, R.string.error_invalid_number_for_count);
                }
            }
        });
        UiHelper.setDefaultNegativeButton(builder);
        UiHelper.createAndShowAlertWithSoftKeyboard(builder);
    }

}
