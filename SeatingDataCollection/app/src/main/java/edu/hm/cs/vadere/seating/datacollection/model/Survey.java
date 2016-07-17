package edu.hm.cs.vadere.seating.datacollection.model;

import android.app.Activity;
import android.widget.EditText;

import com.orm.SugarRecord;

import java.io.Serializable;

import edu.hm.cs.vadere.seating.datacollection.R;

public class Survey extends SugarRecord implements Serializable {
    private String agentName;
    private String date;
    private String startingAt;
    private String destination;
    private String line;
    private int wagonNo;
    private int doorNo;
    private int agentSeat;
    private String trainType;
    private String trainNumber;

    public Survey() { }

    public String getAgentName() {
        return agentName;
    }

    public String getDate() {
        return date;
    }

    public String getStartingAt() {
        return startingAt;
    }

    public String getDestination() {
        return destination;
    }

    public String getLine() {
        return line;
    }

    public int getWagonNo() {
        return wagonNo;
    }

    public int getDoorNo() {
        return doorNo;
    }

    public int getAgentSeat() {
        return agentSeat;
    }

    public String getTrainType() {
        return trainType;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    /**
     * Not sure if this is the best solution, because this way I tie the GUI closely to the model.
     * On the other hand, the field assignments are much closer to the field declarations and I do not
     * need all those setters.
     */
    public void setValuesFromGUI(Activity activity) {
        agentName = getTextFromEdit(activity, R.id.editTextName);
        agentSeat = getIntFromEdit(activity, R.id.editTextSeat);
        date = getTextFromEdit(activity, R.id.editTextDate);
        startingAt = getTextFromEdit(activity, R.id.editTextStartingAt);
        destination = getTextFromEdit(activity, R.id.editTextDirection);
        line = getTextFromEdit(activity, R.id.editTextLine);
        wagonNo = getIntFromEdit(activity, R.id.editTextWagonNo);
        doorNo = getIntFromEdit(activity, R.id.editTextDoorNo);
        trainType = getTextFromEdit(activity, R.id.editTextTrainType);
        trainNumber = getTextFromEdit(activity, R.id.editTextTrainNo);
    }

    private String getTextFromEdit(Activity activity, int viewId) {
        EditText edit = getEditTextById(activity, viewId);
        return edit.getText().toString();
    }

    private EditText getEditTextById(Activity activity, int viewId) {
        return (EditText) activity.findViewById(viewId);
    }

    private int getIntFromEdit(Activity activity, int viewId) {
        String text = getTextFromEdit(activity, viewId);
        if (text.isEmpty())
            text = "0"; // default for numbers in Person record
        return Integer.parseInt(text);
    }

}
