package edu.hm.cs.vadere.seating.datacollection.model;

public class Person implements SeatTaker {
    private static int nextId = 1;
    private int id;
    private boolean disruptive;
    private AgeGroup ageGroup;
    private Group group;

    public Person() {
        id = nextId++;
    }

    public int getId() {
        return id;
    }

    public boolean isDisruptive() {
        return disruptive;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public Group getGroup() {
        return group;
    }

    public void setDisruptive(boolean disruptive) {
        this.disruptive = disruptive;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
