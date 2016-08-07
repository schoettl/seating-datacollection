package edu.hm.cs.vadere.seating.datacollection.seats;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.ListView;

import edu.hm.cs.vadere.seating.datacollection.LogEventWriter;
import edu.hm.cs.vadere.seating.datacollection.OnOptionsMenuInvalidatedListener;
import edu.hm.cs.vadere.seating.datacollection.R;
import edu.hm.cs.vadere.seating.datacollection.UiHelper;
import edu.hm.cs.vadere.seating.datacollection.Utils;
import edu.hm.cs.vadere.seating.datacollection.actions.ActionManager;
import edu.hm.cs.vadere.seating.datacollection.actions.DefineGroupAction;
import edu.hm.cs.vadere.seating.datacollection.model.HandBaggage;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Seat;
import edu.hm.cs.vadere.seating.datacollection.model.SeatTaker;
import edu.hm.cs.vadere.seating.datacollection.model.SeatsState;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class SeatsFragment extends Fragment implements OnOptionsMenuInvalidatedListener {

    private static final String TAG = "SeatsFragment";
    private static final String ARG_STATE = "1f90620b42228f9dbb029a80a79c95d1119c9ea0";
    private static final String ARG_DIRECTION = "da5cdc08303f8829862418afbf74b5ced6405fb4";
    private static final String ARG_SURVEY_ID = "2f78552dc00b45e7a0f18701fe3a5b5994eb4d55";

    public static final String FRAGMENT_TAG = "9995463f9f1dfb976b0a274dd34cc8fb36e47ded";

    private FloorRectAdapter floorRectAdapter;
    private ActionManager actionManager;
    private Direction direction = Direction.FORWARD;

    private ImageView ivDirection;
    private GridView gridView;

    public static SeatsFragment newInstance(Survey survey, @Nullable SeatsState state, Direction direction) {
        Bundle bundle = new Bundle();
        SeatsFragment fragment = new SeatsFragment();

        bundle.putLong(ARG_SURVEY_ID, survey.getId());
        bundle.putSerializable(ARG_STATE, state);
        bundle.putSerializable(ARG_DIRECTION, direction);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // onCreate gets called twice when the activity is recreated:
        // SeatsFragment is created twice!!
        // 1th time with the savedInstanceState
        // 2nd time with the arguments
        Log.w(TAG, "fragment onCreate: " + this);

        restoreStateFromBundleOrArgs(savedInstanceState);

        final long surveyId = getArguments().getLong(ARG_SURVEY_ID);
        final Survey survey = Utils.getSurvey(surveyId);
        final LogEventWriter logEventWriter = new LogEventWriter(survey);
        actionManager = new ActionManager(this, logEventWriter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "fragment's on create view");

        View view = inflater.inflate(R.layout.fragment_seats, container, false);
        gridView = (GridView) view.findViewById(R.id.seats_grid);
        gridView.setOnItemClickListener(new FloorRectClickListener());
        gridView.setOnCreateContextMenuListener(this);
        gridView.setAdapter(floorRectAdapter);
        ivDirection = (ImageView) view.findViewById(R.id.ivDirection);
        ivDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invertDirection();
                setCurrentDirectionIcon();
            }
        });
        setCurrentDirectionIcon();

        registerForContextMenu(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving instance state");
        outState.putSerializable(ARG_STATE, getCurrentState());
        outState.putSerializable(ARG_DIRECTION, direction);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_seats_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_define_group);
        if (actionManager.isActionPending(DefineGroupAction.class)) {
            UiHelper.tintIconOfMenuItem(item, Color.RED);
        } else {
            UiHelper.tintIconOfMenuItem(item, Color.BLACK);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "on create context menu");
        super.onCreateContextMenu(menu, view, menuInfo);

        // Only create context menu for the seats grid
        if (!(view instanceof GridView))
            return;

        View v = getViewFromMenuInfo(menuInfo);

        if (v instanceof SeatWidget) {
            MenuInflater inflater = getActivity().getMenuInflater();
            SeatWidget seatWidget = (SeatWidget) v;
            Log.d(TAG, "context menu: " + seatWidget.toString());
            SeatTaker seatTaker = seatWidget.getSeat().getSeatTaker();
            if (seatTaker instanceof Person) {
                Log.d(TAG, "person");
                inflater.inflate(R.menu.context_menu_person, menu);
                if (!((Person) seatTaker).isDisturbing())
                    menu.removeItem(R.id.action_person_stops_disturbing);

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
            case R.id.action_sit_down_place_baggage:
                actionManager.actionSitDownAndPlaceBaggage(seat);
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
            case R.id.action_remove_from_group:
                actionManager.actionRemoveFromGroup(seat);
            default:
                Log.w(TAG, "context menu item not implemented");
                return super.onContextItemSelected(item);
        }
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public SeatsState getCurrentState() {
        return new SeatsState(floorRectAdapter.getSeats());
    }

    private View getViewFromMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        return info.targetView; //floorRectAdapter.getItem(menuInfo.position);
    }

    private void setCurrentDirectionIcon() {
        ivDirection.setImageResource(direction.getIconResourceId());
    }

    @Override
    public void onOptionsMenuInvalidated() {
        getActivity().invalidateOptionsMenu();
    }

    public GridView getGridView() {
        return gridView;
    }

    public Direction getDirection() {
        return direction;
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
                        actionManager.actionLeave(seat);

                    } else if (seatTaker instanceof HandBaggage) {
                        Log.d(TAG, "baggage");
                        actionManager.actionRemoveBaggage(seat);

                    } else { // seat is empty
                        Log.d(TAG, "empty seat");
                        actionManager.actionSitDown(seat);
                    }
                }
                seatWidget.invalidate();
            } else {
                Log.d(TAG, "not a seat - ignoring click");
            }
        }
    }

    private void restoreStateFromBundleOrArgs(Bundle savedInstanceState) {
        SeatsState state = null;
        Direction dir = Direction.FORWARD;
        if (savedInstanceState != null) {
            Log.d(TAG, "saved instance state available, using that state");
            state = getSeatsStateFromBundle(savedInstanceState);
            dir = getDirectionFromBundle(savedInstanceState);
        } else if (getArguments() != null) {
            Log.d(TAG, "arguments available, using that state");
            state = getSeatsStateFromBundle(getArguments());
            dir = getDirectionFromBundle(getArguments());
        }

        Log.d(TAG, "restoring directoin and state: " + state);
        floorRectAdapter = new FloorRectAdapter(getContext(), state);
        direction = (dir == null) ? Direction.FORWARD : dir;
    }

    private SeatsState getSeatsStateFromBundle(Bundle bundle) {
        return (SeatsState) bundle.getSerializable(ARG_STATE);
    }

    private Direction getDirectionFromBundle(Bundle bundle) {
        return (Direction) bundle.getSerializable(ARG_DIRECTION);
    }

    private void invertDirection() {
        direction = (direction == Direction.FORWARD) ? Direction.BACKWARD : Direction.FORWARD;
    }

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
    }

}
