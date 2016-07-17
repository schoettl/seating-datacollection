package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEvent extends SugarRecord {
    public static final String TIME_FORMAT = "HH:mm:ss";

    private Survey survey;
    private String time;
    private LogEventType eventType;
    private Integer seat;
    private Person person;

    private Integer extraInt;
    private String extraString;

    public LogEvent(Survey survey, LogEventType eventType, Integer seat, Person person, Integer extraInt, String extraString) {
        this.time = new SimpleDateFormat(TIME_FORMAT).format(new Date());
        this.eventType = eventType;
        this.seat = seat;
        this.person = person;
        this.survey = survey;
        this.extraInt = extraInt;
        this.extraString = extraString;
    }

    public LogEvent(Survey survey, LogEventType eventType, Integer seat, Person person) {
        this(survey, eventType, seat, person, null, null);
    }
}
