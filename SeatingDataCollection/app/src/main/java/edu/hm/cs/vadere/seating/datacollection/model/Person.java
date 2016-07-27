package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import edu.hm.cs.vadere.seating.datacollection.Utils;

public class Person extends SugarRecord implements SeatTaker {
    @Ignore
    private boolean disturbing;
    @Ignore
    private boolean agent;

    private Gender gender = Gender.NA;
    private AgeGroup ageGroup = AgeGroup.NA;
    private MGroup mGroup = null;

    public Person() { }

    public boolean isDisturbing() {
        return disturbing;
    }

    /** Get the value set by {@link #setAgent(boolean)}. */
    public boolean isAgent() {
        return agent;
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

    /**
     * Mark this person as the agent conducting the data collection.
     * Only one person should be marked as agent!
     */
    public void setAgent(boolean agent) {
        this.agent = agent;
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
        return Utils.formatString("person %d (%s, %s, %s)", getId(), getGender(), getAgeGroup(), getGroup());
    }
}
