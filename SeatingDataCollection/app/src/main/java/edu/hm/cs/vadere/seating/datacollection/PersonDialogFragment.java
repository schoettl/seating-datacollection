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
import android.widget.Spinner;

import edu.hm.cs.vadere.seating.datacollection.model.AgeGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Gender;
import edu.hm.cs.vadere.seating.datacollection.model.Person;

public class PersonDialogFragment extends DialogFragment {
    public static final String FRAGMENT_TAG = "500718720dfb5c64e96622f91a9e3786969a9fe2";
    private static final String TAG = "PersonDialog";
    private static final String ARG_PERSON_GENDER_KEY = "bad8cbcf94421963bf863495daba5afd4fe644d4";
    private static final String ARG_PERSON_AGE_GROUP_KEY = "77640998084251deb1926451b2bb2bc5763a9143";

    private PositiveClickListener listener;

    public static PersonDialogFragment newInstance(Person person) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_GENDER_KEY, person.getGender());
        args.putSerializable(ARG_PERSON_AGE_GROUP_KEY, person.getAgeGroup());

        PersonDialogFragment dialog = new PersonDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Log.d(TAG, "on create dialog");

        Gender personGender = (Gender) getArguments().get(ARG_PERSON_GENDER_KEY);
        AgeGroup personAgeGroup = (AgeGroup) getArguments().get(ARG_PERSON_AGE_GROUP_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_person_message);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;
                Spinner spinnerGender = (Spinner) d.findViewById(R.id.spinnerGender);
                Spinner spinnerAgeGroup = (Spinner) d.findViewById(R.id.spinnerAgeGroup);

                Gender gender = (Gender) spinnerGender.getSelectedItem();
                AgeGroup ageGroup = (AgeGroup) spinnerAgeGroup.getSelectedItem();

                if (listener != null)
                    listener.onPersonDialogOkClick(gender, ageGroup);
            }
        });
        UiHelper.setDefaultNegativeButton(builder);

        // This line does not really inflate the view, so setInitialValues does not work
        //builder.setView(R.layout.dialog_person);

        // This line causes a recursion:
        //LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_person, null);
        setInitialValues(view, personGender, personAgeGroup);

        builder.setView(view);
        return builder.create();
    }

    public void setOkClickListener(PositiveClickListener listener) {
        this.listener = listener;
    }

    private void setInitialValues(View view, Gender gender, AgeGroup ageGroup) {
        Log.d(TAG, "setting initial values");

        Spinner spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
        setSimpleSpinnerAdapter(spinnerGender, Gender.values());
        spinnerGender.setSelection(gender.ordinal());

        Spinner spinnerAgeGroup = (Spinner) view.findViewById(R.id.spinnerAgeGroup);
        setSimpleSpinnerAdapter(spinnerAgeGroup, AgeGroup.values());
        spinnerAgeGroup.setSelection(ageGroup.ordinal());
    }

    private <E> void setSimpleSpinnerAdapter(Spinner spinner, E[] values) {
        spinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_plain_textview, values));
    }

    public interface PositiveClickListener {
        void onPersonDialogOkClick(Gender gender, AgeGroup ageGroup);
    }
}

