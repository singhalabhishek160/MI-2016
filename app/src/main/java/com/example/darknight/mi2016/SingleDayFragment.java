package com.example.darknight.mi2016;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darknight.mi2016.ServerConnection.GsonModels;
import com.google.gson.Gson;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleDayFragment extends Fragment {

    private RecyclerView scheduleRecyclerView;
    private EventsListAdapter scheduleListAdapter;
    private List<GsonModels.Event> eventResponse;

    public SingleDayFragment() {
        // Required empty public constructor
    }

    public SingleDayFragment(Context context, List<GsonModels.Event> eventResponse){
        this.eventResponse = eventResponse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View singledayview = (View) inflater.inflate(R.layout.fragment_single_day, container, false);
        scheduleRecyclerView = (RecyclerView) singledayview.findViewById(R.id.schedule_list);
        scheduleListAdapter = new EventsListAdapter(eventResponse, new ItemCLickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //TODO: Launch Event Page
            }
        });
        scheduleRecyclerView.setAdapter(scheduleListAdapter);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return singledayview;
    }

}
