package edu.hm.cs.vadere.seating.datacollection.model;

import com.orm.SugarRecord;

/** GROUP is a reserved word in SQL. Therefore, "M" for model or mates. */
public class MGroup extends SugarRecord {
    @Override
    public String toString() {
        return "group " + getId();
    }
}
