package edu.hm.cs.vadere.seating.datacollection.model;

public class Group {
    private static int nextId = 1;
    private int id;

    public Group() {
        id = nextId++;
    }

    public int getId() {
        return id;
    }

}
