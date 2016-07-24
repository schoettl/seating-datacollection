package edu.hm.cs.vadere.seating.datacollection;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class PersonDialogFragment extends DialogFragment {
    private static final String TAG = "PersonDialog";
    private static final String PERSON_ARG_KEY = "bad8cbcf94421963bf863495daba5afd4fe644d4";

    private PersonDialogListener listener;
    private Person person;

    private Spinner spinnerGender;
    private Spinner spinnerAgeGroup;
    private CheckBox cbDisturbing;

    public static DialogFragment newInstance(Person person) {
        Bundle args = new Bundle();
        args.putSerializable(PERSON_ARG_KEY, person);

        DialogFragment dialog = new PersonDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }

    public void updateAndSavePerson() {
        person.setAgeGroup((AgeGroup) spinnerAgeGroup.getSelectedItem());
        person.setGender((Gender) spinnerGender.getSelectedItem());
        person.setDisturbing(cbDisturbing.isChecked());
        person.save();
    }

    public interface PersonDialogListener {
        void onPersonDialogPositiveClick(PersonDialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PersonDialogListener) getTargetFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "on create dialog");
        super.onCreateDialog(savedInstanceState);

        person = (Person) getArguments().get(PERSON_ARG_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_person_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPersonDialogPositiveClick(PersonDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

//        LayoutInflater inflator = getActivity().getLayoutInflater();
//        inflator.inflate() // not necessary, builder can do it better:
        builder.setView(R.layout.dialog_person);
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitialValues(view);
    }

    private void setInitialValues(View view) {
        spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
        setSimpleSpinnerAdapter(spinnerGender, Gender.values());
        spinnerGender.setSelection(person.getGender().ordinal());

        spinnerAgeGroup = (Spinner) view.findViewById(R.id.spinnerAgeGroup);
        setSimpleSpinnerAdapter(spinnerAgeGroup, AgeGroup.values());
        spinnerAgeGroup.setSelection(person.getAgeGroup().ordinal());

        cbDisturbing = (CheckBox) view.findViewById(R.id.cbDisturbing);
        cbDisturbing.setChecked(person.isDisturbing());
    }

    private <E> void setSimpleSpinnerAdapter(Spinner spinner, E[] values) {
        spinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.plain_textview, values));
    }
}

