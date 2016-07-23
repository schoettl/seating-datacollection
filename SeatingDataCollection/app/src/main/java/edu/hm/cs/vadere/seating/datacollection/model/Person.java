package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Person extends SugarRecord implements SeatTaker {
    @Ignore
    private boolean disturbing;

    private Gender gender;
    private AgeGroup ageGroup;
    private Long groupId;

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
        return Group.findById(Group.class, groupId);
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

    public void setGroup(Group group) {
        this.groupId = group.getId();
    }

}
