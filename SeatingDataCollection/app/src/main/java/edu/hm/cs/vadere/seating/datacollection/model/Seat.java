package edu.hm.cs.vadere.seating.datacollection.model;

public class Seat {
    private int id;
    private SeatTaker seatTaker;

    public Seat(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public SeatTaker getSeatTaker() {
        return seatTaker;
    }

    public void setSeatTaker(SeatTaker seatTaker) {
        this.seatTaker = seatTaker;
    }

    public void clearSeat() {
        seatTaker = null;
    }

    @Override
    public String toString() {
        String seatTakerStr = "empty";
        if (seatTaker != null)
            seatTakerStr = seatTaker.toString();
        return "seat " + id + " - " + seatTakerStr;
    }
}
