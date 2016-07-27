package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEvent extends SugarRecord {
    public static final String TIME_FORMAT = "HH:mm:ss";

    private String time;
    private Survey survey;

    private LogEventType eventType;
    private Integer seat;
    private Person person;

    private Integer extraInt;
    private String extraString;

    public LogEvent() {
        this(null, null, null, null, null, null);
    }

    public LogEvent(Survey survey, LogEventType eventType, Integer seat, Person person, Integer extraInt, String extraString) {
        this.time = new SimpleDateFormat(TIME_FORMAT).format(new Date());
        this.eventType = eventType;
        this.seat = seat;
        this.person = person;
        this.survey = survey;
        this.extraInt = extraInt;
        this.extraString = extraString;
    }

    public String getTime() {
        return time;
    }

    public Survey getSurvey() {
        return survey;
    }

    public LogEventType getEventType() {
        return eventType;
    }

    public Integer getSeat() {
        return seat;
    }

    public Person getPerson() {
        return person;
    }

    public Integer getExtraInt() {
        return extraInt;
    }

    public String getExtraString() {
        return extraString;
    }
}
