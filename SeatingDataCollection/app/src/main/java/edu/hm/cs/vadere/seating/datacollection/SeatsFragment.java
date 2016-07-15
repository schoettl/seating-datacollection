package edu.hm.cs.vadere.seating.datacollection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

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

}
