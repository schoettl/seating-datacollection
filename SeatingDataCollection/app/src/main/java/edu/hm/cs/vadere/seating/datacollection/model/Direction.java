package edu.hm.cs.vadere.seating.datacollection.model;

import edu.hm.cs.vadere.seating.datacollection.R;

public enum Direction {
    FORWARD(R.drawable.ic_arrow_upward_black_18dp),
    BACKWARD(R.drawable.ic_arrow_downward_black_18dp);

    private int iconResourceId;

    Direction(int iconResource) {
        iconResourceId = iconResource;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public Direction invert() {
        return (this == FORWARD) ? BACKWARD : FORWARD;
    }

    public interface DirectionChangeListener {
        void onDirectionChanged(Direction newDirection);
    }

}
