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

    public long logInitializationEnd() {
        logcatInfo("initialization end", INITIALIZATION_END);
        return logEvent(createLogEvent(INITIALIZATION_END, null, null));
    }

    public long logSeatEvent(LogEventType eventType, Seat seat, Person person) {
        logcatInfo("seat event", eventType);
        return logEvent(createLogEvent(eventType, seat.getId(), person));
    }

    public long logTrainEvent(LogEventType eventType) {
        logcatInfo("train event (seat, person)", eventType);
        return logEvent(createLogEvent(eventType, null, null));
    }

    public long logCountStandingPersons(int count) {
        logcatInfo("count standing persons", COUNT_STANDING_PERSONS);
        return logEvent(createLogEvent(COUNT_STANDING_PERSONS, null, null, count, null));
    }

    public long logDisturbingPerson(Person person, String reason) {
        logcatInfo("disturbing person: " + reason, DISTURBING);
        return logEvent(createLogEvent(DISTURBING, null, person, null, reason));
    }

    public long logDisturbingSeat(Seat seat, String reason) {
        logcatInfo("disturbing seat: " + reason, DISTURBING);
        return logEvent(createLogEvent(DISTURBING, seat.getId(), null, null, reason));
    }

    public long logStopsDisturbingPerson(Person p) {
        logcatInfo("stops disturbing person", DISTURBING);
        return logEvent(createLogEvent(STOPS_DISTURBING, null, p));
    }

    public long logStopsDisturbingSeat(Seat s) {
        logcatInfo("stops disturbing seat", DISTURBING);
        return logEvent(createLogEvent(STOPS_DISTURBING, s.getId(), null));
    }

    private long logEvent(LogEvent e) {
        e.save();
        return e.getId();
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
