package edu.hm.cs.vadere.seating.datacollection.seats;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.model.MGroup;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
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
        private Long groupIdIfPresent;

        public SerializableSeatTaker(SeatTaker seatTaker) {
            this.seatTaker = seatTaker;
            this.idIfPresent = null;
            if (seatTaker instanceof SugarRecord)
                this.idIfPresent = ((SugarRecord) seatTaker).getId();

            this.groupIdIfPresent = maybeGetGroupId(seatTaker);
        }

        public SeatTaker getSeatTaker() {
            SeatTaker result = seatTaker;
            if (result instanceof SugarRecord)
                ((SugarRecord) result).setId(idIfPresent);

            maybeSetGroupId(result, groupIdIfPresent);

            return result;
        }

        private Long maybeGetGroupId(SeatTaker seatTaker) {
            MGroup g = getGroupIfPresent(seatTaker);
            if (g != null)
                return g.getId();
            return null;
        }

        private void maybeSetGroupId(SeatTaker seatTaker, Long groupIdOrNull) {
            MGroup g = getGroupIfPresent(seatTaker);
            if (g != null)
                g.setId(groupIdOrNull);
        }

        private MGroup getGroupIfPresent(SeatTaker seatTaker) {
            if (seatTaker instanceof Person)
                return ((Person) seatTaker).getGroup();
            return null;
        }
    }

}
