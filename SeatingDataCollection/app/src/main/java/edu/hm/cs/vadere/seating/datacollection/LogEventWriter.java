package edu.hm.cs.vadere.seating.datacollection;

import android.util.Log;

import java.io.Serializable;

import edu.hm.cs.vadere.seating.datacollection.model.LogEvent;
import edu.hm.cs.vadere.seating.datacollection.model.LogEventType;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.COUNT_STANDING_PERSONS;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.DISTURBING;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.INITIALIZATION_END;
import static edu.hm.cs.vadere.seating.datacollection.model.LogEventType.STOPS_DISTURBING;

public class LogEventWriter implements Serializable {

    private static final String TAG = "LogEventWriter";

    private Survey survey;

    public LogEventWriter(Survey survey) {
        this.survey = survey;
    }

    public void logInitializationEnd() {
        logcatInfo("initialization end", INITIALIZATION_END);
        logEvent(createLogEvent(INITIALIZATION_END, null, null));
    }

    public void logSeatEvent(LogEventType eventType, Seat seat, Person person) {
        logcatInfo("seat event", eventType);
        logEvent(createLogEvent(eventType, seat.getId(), person));
    }

    public void logTrainEvent(LogEventType eventType) {
        logcatInfo("train event (seat, person)", eventType);
        logEvent(createLogEvent(eventType, null, null));
    }

    public void logCountStandingPersons(int count) {
        logcatInfo("count standing persons", COUNT_STANDING_PERSONS);
        logEvent(createLogEvent(COUNT_STANDING_PERSONS, null, null, count, null));
    }

    public void logDisturbingPerson(Person person, String reason) {
        logcatInfo("disturbing person: " + reason, DISTURBING);
        logEvent(createLogEvent(DISTURBING, null, person, null, reason));
    }

    public void logDisturbingSeat(Seat seat, String reason) {
        logcatInfo("disturbing seat: " + reason, DISTURBING);
        logEvent(createLogEvent(DISTURBING, seat.getId(), null, null, reason));
    }

    public void logStopsDisturbingPerson(Person p) {
        logcatInfo("stops disturbing person", DISTURBING);
        logEvent(createLogEvent(STOPS_DISTURBING, null, p));
    }

    public void logStopsDisturbingSeat(Seat s) {
        logcatInfo("stops disturbing seat", DISTURBING);
        logEvent(createLogEvent(STOPS_DISTURBING, s.getId(), null));
    }

    private void logEvent(LogEvent e) {
        e.save();
    }

    private LogEvent createLogEvent(LogEventType eventType, Integer seat, Person person, Integer extraInt, String extraString) {
        return new LogEvent(survey, eventType, seat, person, extraInt, extraString);
    }

    private LogEvent createLogEvent(LogEventType eventType, Integer seat, Person person) {
        return new LogEvent(survey, eventType, seat, person, null, null);
    }

    private void logcatInfo(String info, LogEventType event) {
        Log.i(TAG, Utils.formatString("%s (%s)", info, event));
    }
}
