package edu.hm.cs.vadere.seating.datacollection.seats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.Utils;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class SeatsFragment extends Fragment {

    private static final String ARG_STATE_KEY = "1f90620b42228f9dbb029a80a79c95d1119c9ea0";
    private static final String ARG_SURVEY_ID_KEY = "2f78552dc00b45e7a0f18701fe3a5b5994eb4d55";
    public static final String TAG = "SeatsFragment";

    private FloorRectAdapter floorRectAdapter;
    private ActionManager actionManager;

    public static SeatsFragment newInstance(Survey survey, SeatsState state) {
        Bundle bundle = new Bundle();
        SeatsFragment fragment = new SeatsFragment();

        bundle.putLong(ARG_SURVEY_ID_KEY, survey.getId());
        bundle.putSerializable(ARG_STATE_KEY, state);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SeatsFragment newInstance(Survey survey) {
        return newInstance(survey, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final SeatsState state = (SeatsState) getArguments().getSerializable(ARG_STATE_KEY);
        final long surveyId = getArguments().getLong(ARG_SURVEY_ID_KEY);
        final Survey survey = Utils.getSurvey(surveyId);
        LogEventWriter logEventWriter = new LogEventWriter(survey);
        actionManager = new ActionManager(this, logEventWriter);
        floorRectAdapter = new FloorRectAdapter(getContext(), state);

        GridView view = (GridView) inflater.inflate(R.layout.fragment_seats, container, false);
        view.setOnItemClickListener(new FloorRectClickListener());
        view.setAdapter(floorRectAdapter);
        registerForContextMenu(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_seats_fragment, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View gridView, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "on create context menu");
        super.onCreateContextMenu(menu, gridView, menuInfo);

        View v = getViewFromMenuInfo(menuInfo);

        if (v instanceof SeatWidget) {
            MenuInflater inflater = getActivity().getMenuInflater();
            SeatWidget seatWidget = (SeatWidget) v;
            Log.d(TAG, "context menu: " + seatWidget.toString());
            SeatTaker seatTaker = seatWidget.getSeat().getSeatTaker();
            if (seatTaker instanceof Person) {
                Log.d(TAG, "person");
                inflater.inflate(R.menu.context_menu_person, menu);

            } else if (seatTaker instanceof HandBaggage) {
                Log.d(TAG, "baggage");
                inflater.inflate(R.menu.context_menu_baggage, menu);

            } else { // seat is empty
                Log.d(TAG, "empty seat");
                inflater.inflate(R.menu.context_menu_seat, menu);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_define_group:
                actionManager.actionDefineGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get clicked view: http://stackoverflow.com/questions/2926293/identifying-the-view-selected-in-a-contextmenu-android
        Log.d(TAG, "on context item selected");
        View v = getViewFromMenuInfo(item.getMenuInfo());

        if (!(v instanceof SeatWidget))
            return false;

        Seat seat = ((SeatWidget) v).getSeat();
        Log.d(TAG, seat.toString());

        actionManager.clearPendingAction(); // every context menu action cancels a pending action
        switch (item.getItemId()) {
            case R.id.action_sit_down:
                actionManager.actionSitDown(seat);
                return true;
            case R.id.action_change_place:
                actionManager.actionChangeSeat(seat);
                return true;
            case R.id.action_leave:
                actionManager.actionLeave(seat);
                return true;
            case R.id.action_place_baggage:
                actionManager.actionPlaceBaggage(seat);
                return true;
            case R.id.action_remove_baggage:
                actionManager.actionRemoveBaggage(seat);
                return true;
            case R.id.action_set_person_properties:
                actionManager.actionSetPersonProperties(seat);
                return true;
            case R.id.action_person_disturbing:
                actionManager.actionPersonDisturbing(seat);
                return true;
            case R.id.action_person_stops_disturbing:
                actionManager.actionPersonStopsDisturbing(seat);
                return true;
            default:
                Log.w(TAG, "context menu item not implemented");
                return super.onContextItemSelected(item);
        }
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public SeatsState getState() {
        return new SeatsState(floorRectAdapter.getSeats());
    }

    private View getViewFromMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        return info.targetView; //floorRectAdapter.getItem(menuInfo.position);
    }

    private class FloorRectClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view instanceof SeatWidget) {
                SeatWidget seatWidget = (SeatWidget) view;
                Seat seat = seatWidget.getSeat();
                if (actionManager.isActionPending()) {
                    actionManager.seatSelected(seat);
                } else {
                    Log.d(TAG, "normal click on seat");
                    SeatTaker seatTaker = seat.getSeatTaker();
                    if (seatTaker instanceof Person) {
                        Log.d(TAG, "person");
                        actionPersonLeave(seat);

                    } else if (seatTaker instanceof HandBaggage) {
                        Log.d(TAG, "baggage");
                        actionManager.actionRemoveBaggage(seat);

                    } else { // seat is empty
                        Log.d(TAG, "empty seat");
                        actionManager.actionSitDown(seat);
                    }
                }
            } else {
                Log.d(TAG, "not a seat - ignoring click");
            }
        }
    }

    private void actionPersonLeave(final Seat seat) {
        UiHelper.showConfirmDialog(getActivity(), R.string.dialog_confirm_leave, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionManager.actionLeave(seat);
            }
        });
    }

}
