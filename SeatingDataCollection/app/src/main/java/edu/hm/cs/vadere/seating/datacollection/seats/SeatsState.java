package edu.hm.cs.vadere.seating.datacollection.seats;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

/** A class to transfer state of seats. */
public class SeatsState implements Serializable {

    private ArrayList<Seat> seatsState;
    private ArrayList<SerializableSeatTaker> seatTakersState;

    public SeatsState(List<Seat> seats) {
        seatsState = new ArrayList<>(seats);
        seatTakersState = new ArrayList<>(seats.size());
        for (Seat s : seats) {
            SeatTaker seatTaker = s.getSeatTaker();
            seatTakersState.add(new SerializableSeatTaker(seatTaker));
        }
    }

    public List<Seat> restoreSeats() {
        for (int i = 0; i < seatsState.size(); i++) {
            SeatTaker restoredSeatTaker = seatTakersState.get(i).getSeatTaker();
            seatsState.get(i).setSeatTaker(restoredSeatTaker);
        }
        return new ArrayList<>(seatsState);
    }

    public class SerializableSeatTaker implements Serializable {
        private Long idIfPresent;
        private SeatTaker seatTaker;

        public SeatTaker getSeatTaker() {
            SeatTaker result = seatTaker;
            if (result instanceof SugarRecord)
                ((SugarRecord) result).setId(idIfPresent);
            return result;
        }

        public SerializableSeatTaker(SeatTaker seatTaker) {
            this.seatTaker = seatTaker;
            this.idIfPresent = null;
            if (seatTaker instanceof SugarRecord)
                this.idIfPresent = ((SugarRecord) seatTaker).getId();
        }
    }
}
