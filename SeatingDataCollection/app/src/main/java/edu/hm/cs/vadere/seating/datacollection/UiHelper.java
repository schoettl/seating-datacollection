package edu.hm.cs.vadere.seating.datacollection;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Toast;

import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class UiHelper {

    private static final DialogInterface.OnClickListener alertDialogDefaultNegativeListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            };

    public static void setDefaultNegativeButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(R.string.cancel, alertDialogDefaultNegativeListener);
    }

    public static void showAlertWithSoftKeyboard(AlertDialog.Builder alertDialogBuilder) {
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    public static void showConfirmDialog(Activity activity, int message, DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, positiveButtonListener);
        setDefaultNegativeButton(builder);
        builder.show();
    }

    public static void setToolbar(AppCompatActivity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.app_toolbar);
        activity.setSupportActionBar(toolbar);
    }

    /** Start a SeatsFragment. The state can be null. */
    public static SeatsFragment startAndReturnSeatsFragment(FragmentActivity activity, Survey survey, SeatsState state) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        SeatsFragment fragment = SeatsFragment.newInstance(survey, state);
        ft.replace(R.id.seats_fragment_placeholder, fragment);
        ft.commit();
        return fragment;
    }

    /** Show a small hint for non-severe errors. */
    public static void showErrorToast(Context context, int message) {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
