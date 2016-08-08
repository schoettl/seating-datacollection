package edu.hm.cs.vadere.seating.datacollection.model;

public enum TrainState {
    UNKNOWN, MOVING, HALTING;
    public interface TrainStateListener {
        void onTrainStateChanged(TrainState newState);
    }
}

