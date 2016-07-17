package edu.hm.cs.vadere.seating.datacollection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;

public class SeatsFragment extends Fragment {

    public static SeatsFragment newInstance() {
        return new SeatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GridView view = (GridView) inflater.inflate(R.layout.fragment_seats, container, false);
        ListAdapter adapter = new FloorRectAdapter(getContext());
        view.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v instanceof SeatWidget) {
            MenuInflater inflater = getActivity().getMenuInflater();
            SeatWidget seatWidget = (SeatWidget) v;
            SeatTaker seatTaker = seatWidget.getSeat().getSeatTaker();
            if (seatTaker instanceof Person) {
                inflater.inflate(R.menu.context_menu_person, menu);

            } else if (seatTaker instanceof HandBaggage) {
                inflater.inflate(R.menu.context_menu_baggage, menu);

            } else { // seat is empty
                inflater.inflate(R.menu.context_menu_seat, menu);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get clicked view: http://stackoverflow.com/questions/2926293/identifying-the-view-selected-in-a-contextmenu-android
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d(SeatsFragment.class.getSimpleName(), menuInfo.targetView.toString());
        Log.d(SeatsFragment.class.getSimpleName(), String.valueOf(menuInfo.id));

        switch (item.getItemId()) {
            case R.id.action_sit_down:
                return true;
            case R.id.action_leave:
                return true;
            case R.id.action_place_baggage:
                return true;
            case R.id.action_remove_baggage:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
