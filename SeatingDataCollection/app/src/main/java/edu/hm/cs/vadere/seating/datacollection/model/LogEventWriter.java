package edu.hm.cs.vadere.seating.datacollection.model;

public class LogEventWriter {

    private Survey survey;

    public LogEventWriter(Survey survey) {
        this.survey = survey;
    }

    public void logInitializationEnd() {
        logEvent(new LogEvent(survey, LogEventType.INITIALIZATION_END, null, null));
    }

    public void logSeatEvent(LogEventType eventType, Seat seat, Person person) {
        logEvent(new LogEvent(survey, eventType, seat.getId(), person));
    }

    public void logTrainEvent(LogEventType eventType) {
        logEvent(new LogEvent(survey, eventType, null, null));
    }

    public void logCountStandingPersons(int count) {
        logEvent(new LogEvent(survey, LogEventType.COUNT_STANDING_PERSONS, null, null, count, null));
    }

    public void logDisturbingPerson(Person person, String reason) {
        logEvent(new LogEvent(survey, LogEventType.DISTURBING, null, person, null, reason));
    }

    public void logDisturbingSeat(Seat seat, String reason) {
        logEvent(new LogEvent(survey, LogEventType.DISTURBING, seat.getId(), null, null, reason));
    }

    public void logStopsDisturbingPerson(Person p) {
        logEvent(new LogEvent(survey, LogEventType.STOPS_DISTURBING, null, p));
    }

    public void logStopsDisturbingSeat(Seat s) {
        logEvent(new LogEvent(survey, LogEventType.STOPS_DISTURBING, s.getId(), null));
    }

    private void logEvent(LogEvent e) {
        e.save();
    }
}
