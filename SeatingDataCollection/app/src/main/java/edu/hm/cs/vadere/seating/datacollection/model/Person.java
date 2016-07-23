package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;

public class Person extends SugarRecord implements SeatTaker, Serializable {
    @Ignore
    private boolean disturbing;

    private Gender gender;
    private AgeGroup ageGroup;
    private Group group;

    public Person() { }

    public boolean isDisturbing() {
        return disturbing;
    }

    public Gender getGender() {
        return gender;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public Group getGroup() {
        return group;
    }

    public void setDisturbing(boolean disruptive) {
        this.disturbing = disruptive;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    /** Group should be saved before saving this (at least up to version 1.5). */
    public void setGroup(Group group) {
        this.group = group;
    }

}
