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
    private static final String ARG_PERSON_ID_KEY = "6f962b504bd465cc949cfef640653d173daf0c4c";
    private static final String ARG_PERSON_GENDER_KEY = "bad8cbcf94421963bf863495daba5afd4fe644d4";
    private static final String ARG_PERSON_AGE_GROUP_KEY = "77640998084251deb1926451b2bb2bc5763a9143";

    private long personId;
    private Gender personGender;
    private AgeGroup personAgeGroup;

    public static DialogFragment newInstance(Person person) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_ID_KEY, person.getId());
        args.putSerializable(ARG_PERSON_GENDER_KEY, person.getGender());
        args.putSerializable(ARG_PERSON_AGE_GROUP_KEY, person.getAgeGroup());

        DialogFragment dialog = new PersonDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Log.d(TAG, "on create dialog");

        personId = (long) getArguments().get(ARG_PERSON_ID_KEY);
        personGender = (Gender) getArguments().get(ARG_PERSON_GENDER_KEY);
        personAgeGroup = (AgeGroup) getArguments().get(ARG_PERSON_AGE_GROUP_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_person_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;
                Spinner spinnerGender = (Spinner) d.findViewById(R.id.spinnerGender);
                Spinner spinnerAgeGroup = (Spinner) d.findViewById(R.id.spinnerAgeGroup);

                Log.d(TAG, "updating person with id " + personId);
                Person person = Person.findById(Person.class, personId);
                person.setGender((Gender) spinnerGender.getSelectedItem());
                person.setAgeGroup((AgeGroup) spinnerAgeGroup.getSelectedItem());
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

        // This line does not really inflate the view, so setInitialValues does not work
        //builder.setView(R.layout.dialog_person);

        // This line causes a recursion:
        //LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_person, null);
        setInitialValues(view);

        builder.setView(view);
        return builder.create();
    }

    private void setInitialValues(View view) {
        Log.d(TAG, "setting initial values");

        Spinner spinnerGender = getSpinner(view, R.id.spinnerGender);
        setSimpleSpinnerAdapter(spinnerGender, Gender.values());
        spinnerGender.setSelection(personGender.ordinal());

        Spinner spinnerAgeGroup = getSpinner(view, R.id.spinnerAgeGroup);
        setSimpleSpinnerAdapter(spinnerAgeGroup, AgeGroup.values());
        spinnerAgeGroup.setSelection(personAgeGroup.ordinal());
    }

    private Spinner getSpinner(View view, int spinnerAgeGroup) {
        return (Spinner) view.findViewById(spinnerAgeGroup);
    }

    private <E> void setSimpleSpinnerAdapter(Spinner spinner, E[] values) {
        spinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.plain_textview, values));
    }

}

