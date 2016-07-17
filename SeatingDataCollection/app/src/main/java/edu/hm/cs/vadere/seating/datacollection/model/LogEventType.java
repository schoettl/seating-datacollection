package edu.hm.cs.vadere.seating.datacollection.model;

public enum LogEventType {
    /** Initial state of seating occupation, baggage, etc. has been recorded. */
    INITIALIZATION_END,

    SIT_DOWN,
    LEAVE,

    /** Person changes seat. The new seat is recorded. */
    CHANGE_SEAT,

    PLACE_BAGGAGE,
    REMOVE_BAGGAGE,

    DISTURBING,
    STOPS_DISTURBING,

    /** Türen werden entriegelt. */
    DOOR_RELEASE,
    /** Zug fährt an. */
    TRAIN_STARTS,

    /** Number of standing persons in both adjacent entrance areas and in the aisle between them. */
    COUNT_STANDING_PERSONS,

    /** Fahrtrichtung ändert sich (z.B. S3 am Ostbahnhof). */
    DIRECTION_CHANGE;
}
