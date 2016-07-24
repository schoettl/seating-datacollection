package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;

public class Person extends SugarRecord implements SeatTaker {
    @Ignore
    private boolean disturbing;

    private Gender gender;
    private AgeGroup ageGroup;
    /** "group" is a illegal name because it is a reserved word in SQL. */
    private Group mateGroup;

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
        return mateGroup;
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
        this.mateGroup = group;
    }

}
