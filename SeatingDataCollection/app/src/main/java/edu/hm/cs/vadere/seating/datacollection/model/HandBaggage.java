package edu.hm.cs.vadere.seating.datacollection.model;

/**
 * HandBaggage ist was man auch ohne Probleme auf dem Schoß, bei den Füßen oder auf der Gepäckablage
 * platzieren kann. Große Gegenstände, z.B. Koffer oder Instrumente gehören nicht dazu.
 */
public class HandBaggage implements SeatTaker {
    private Person owner;

    public HandBaggage(Person owner) {
        this.owner = owner;
    }

}
