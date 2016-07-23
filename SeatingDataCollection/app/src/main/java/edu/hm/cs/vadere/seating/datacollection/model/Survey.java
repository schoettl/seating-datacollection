package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Survey extends SugarRecord implements Serializable {
    private String agentName;
    private String date;
    private String startingAt;
    private String destination;
    private String line;
    private int wagonNo;
    private int doorNo;
    private Person agent;
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

    public Person getAgent() {
        return agent;
    }

    public String getTrainType() {
        return trainType;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartingAt(String startingAt) {
        this.startingAt = startingAt;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setWagonNo(int wagonNo) {
        this.wagonNo = wagonNo;
    }

    public void setDoorNo(int doorNo) {
        this.doorNo = doorNo;
    }

    public void setAgent(Person agent) {
        this.agent = agent;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
}
