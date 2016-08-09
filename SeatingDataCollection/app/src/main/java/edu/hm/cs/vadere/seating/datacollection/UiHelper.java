package edu.hm.cs.vadere.seating.datacollection;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import edu.hm.cs.vadere.seating.datacollection.actions.ActionManager;
import edu.hm.cs.vadere.seating.datacollection.model.Direction;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;
import edu.hm.cs.vadere.seating.datacollection.seats.SeatsFragment;

public class UiHelper {

    private static final String TAG = "UiHelper";
    private static final DialogInterface.OnClickListener alertDialogDefaultListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            };

    public static void setDefaultNegativeButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(R.string.dialog_cancel, alertDialogDefaultListener);
    }

    public static void createAndShowAlertWithSoftKeyboard(AlertDialog.Builder alertDialogBuilder) {
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    public static void showConfirmDialog(Activity activity, int message, DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_ok, positiveButtonListener);
        setDefaultNegativeButton(builder);
        builder.show();
    }

    public static void showInfoDialog(Activity activity, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_ok, alertDialogDefaultListener);
        builder.show();
    }

    public static void setToolbar(AppCompatActivity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.app_toolbar);
        activity.setSupportActionBar(toolbar);
    }

    /** Show a small hint for non-severe errors. */
    public static void showErrorToast(Context context, int message) {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Tint an icon and text of a MenuItem. Default color (dark on light background) is black at 54 % opacity.
     * See https://google.github.io/material-design-icons/ > Coloring
     */
    public static void tintMenuItem(MenuItem item, int color) {
        if (item.getIcon() != null)
            item.getIcon().setTint(color);

        SpannableString coloredTitle = new SpannableString(item.getTitle().toString());
        coloredTitle.setSpan(new ForegroundColorSpan(color), 0, coloredTitle.length(), 0);
        item.setTitle(coloredTitle);
    }

    public static void showInfoToast(Context context, int message) {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void undoOrShowToast(ActionManager actionManager) {
        final Activity activity = actionManager.getSeatsFragment().getActivity();
        if (!actionManager.tryUndoLastAction()) {
            UiHelper.showErrorToast(activity, R.string.error_cannot_undo);
        }
    }

    public static SeatsFragment createAndStartSeatsFragmentIfThisIsNoRecreation(AppCompatActivity activity, Bundle savedInstanceState, Survey survey, @Nullable SeatsState seatsState, @Nullable Direction direction) {
        if (savedInstanceState != null) {
            // Fragment has already been created.
            // Try to find it ;)
            Log.d(TAG, "fragment found: " + activity.getSupportFragmentManager().findFragmentById(R.id.seats_fragment)); // null
            Log.d(TAG, "fragment found: " + activity.getSupportFragmentManager().findFragmentByTag(SeatsFragment.FRAGMENT_TAG)); // works!!
            Log.d(TAG, "fragment found: " + activity.getFragmentManager().findFragmentById(R.id.seats_fragment)); // null
            Log.d(TAG, "fragment found: " + activity.getFragmentManager().findFragmentByTag(SeatsFragment.FRAGMENT_TAG)); // null
            return (SeatsFragment) activity.getSupportFragmentManager().findFragmentByTag(SeatsFragment.FRAGMENT_TAG);
        } else {
            // Should not be created when activity is recreated because.
            // creation of fragment happens earlier in activity's super.onCreate.
            return UiHelper.startAndReturnSeatsFragment(activity, survey, seatsState, direction);
        }
    }

    public static SeatsFragment startAndReturnSeatsFragment(FragmentActivity activity, Survey survey, @Nullable SeatsState state, @Nullable Direction direction) {
        final SeatsFragment fragment = SeatsFragment.newInstance(survey, state, direction);
        final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.seats_fragment_placeholder, fragment, SeatsFragment.FRAGMENT_TAG);
        Log.d(TAG, "committing fragment transaction");
        ft.commit();
        return fragment;
    }

    public static View getViewFromMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        return info.targetView;
    }

    public static <T> T getItemFromMenuInfo(ContextMenu.ContextMenuInfo menuInfo, ArrayAdapter<T> adapter) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        return adapter.getItem(info.position);
    }

}
