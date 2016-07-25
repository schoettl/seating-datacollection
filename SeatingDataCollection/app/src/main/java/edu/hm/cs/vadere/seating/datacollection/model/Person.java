package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Person extends SugarRecord implements SeatTaker {
    @Ignore
    private boolean disturbing;

    private Gender gender = Gender.NA;
    private AgeGroup ageGroup = AgeGroup.NA;
    private MGroup mGroup = null;

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

    public MGroup getGroup() {
        return mGroup;
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

    /** MGroup should be saved before saving this (at least up to version 1.5). */
    public void setGroup(MGroup group) {
        this.mGroup = group;
    }

    @Override
    public String toString() {
        return String.format("person %d (%s, %s, %s)", getId(), getGender(), getAgeGroup(), getGroup());
    }
}
