package edu.hm.cs.vadere.seating.datacollection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class PersonDialogFragment extends DialogFragment {
    private static final String TAG = "PersonDialog";
    private static final String ARG_PERSON_KEY = "bad8cbcf94421963bf863495daba5afd4fe644d4";

    private Person person;

    public static DialogFragment newInstance(Person person) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_KEY, person);

        DialogFragment dialog = new PersonDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Log.d(TAG, "on create dialog");

        person = (Person) getArguments().get(ARG_PERSON_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_person_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;
                Spinner spinnerGender = (Spinner) d.findViewById(R.id.spinnerGender);
                Spinner spinnerAgeGroup = (Spinner) d.findViewById(R.id.spinnerAgeGroup);
                CheckBox cbDisturbing = (CheckBox) d.findViewById(R.id.cbDisturbing);

                Log.d(TAG, "update and save person: " + person);
                person.setGender((Gender) spinnerGender.getSelectedItem());
                person.setAgeGroup((AgeGroup) spinnerAgeGroup.getSelectedItem());
                person.setDisturbing(cbDisturbing.isChecked());
                person.save();

                dismiss(); // TODO required?
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // This line causes a recursion:
        //LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_person, null);
        setInitialValues(view);

        builder.setView(view);
        return builder.create();
    }

    private void setInitialValues(View view) {
        Log.d(TAG, "sitting initial values");

        Spinner spinnerGender = getSpinner(view, R.id.spinnerGender);
        setSimpleSpinnerAdapter(spinnerGender, Gender.values());
        spinnerGender.setSelection(person.getGender().ordinal());

        Spinner spinnerAgeGroup = getSpinner(view, R.id.spinnerAgeGroup);
        setSimpleSpinnerAdapter(spinnerAgeGroup, AgeGroup.values());
        spinnerAgeGroup.setSelection(person.getAgeGroup().ordinal());

        CheckBox cbDisturbing = getCheckBoxDisturbing(view);
        cbDisturbing.setChecked(person.isDisturbing());
    }

    private Spinner getSpinner(View view, int spinnerAgeGroup) {
        return (Spinner) view.findViewById(spinnerAgeGroup);
    }

    private CheckBox getCheckBoxDisturbing(View view) {
        return (CheckBox) view.findViewById(R.id.cbDisturbing);
    }

    private <E> void setSimpleSpinnerAdapter(Spinner spinner, E[] values) {
        spinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.plain_textview, values));
    }

}

