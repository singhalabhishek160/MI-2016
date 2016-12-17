package com.iitb.moodindigo.mi2016;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iitb.moodindigo.mi2016.ServerConnection.GsonModels;
import com.iitb.moodindigo.mi2016.ServerConnection.RetrofitInterface;
import com.iitb.moodindigo.mi2016.ServerConnection.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment implements Callback<List<GsonModels.Event>> {

    private ProgressDialog scheduleProgressDialog;
    private View inflatedView;
    private List<GsonModels.Event> day1List = new ArrayList<>();
    private List<GsonModels.Event> day2List = new ArrayList<>();
    private List<GsonModels.Event> day3List = new ArrayList<>();
    private List<GsonModels.Event> day4List = new ArrayList<>();

    public ScheduleFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Schedule");
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_schedule, container, false);

        if (Cache.isSendEventRequest()) {
            scheduleProgressDialog = new ProgressDialog(getContext());
            scheduleProgressDialog.setIndeterminate(true);
            scheduleProgressDialog.setCancelable(false);
            scheduleProgressDialog.setMessage("Requesting Details");
            RetrofitInterface scheduleretrofitInterface = ServiceGenerator.createService(RetrofitInterface.class);
            scheduleretrofitInterface.getEvents().enqueue(this);
            scheduleProgressDialog.show();
        } else {
            inflateTabs(Cache.getEventList());
        }
        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResponse(Call<List<GsonModels.Event>> call, Response<List<GsonModels.Event>> response) {
        if (response.isSuccessful()) {
            Cache.setEventList(response.body());
            inflateTabs(Cache.getEventList());
            Cache.setSendEventRequest(false);
        } else {
            Toast.makeText(getContext(), "Response code " + response.code(), Toast.LENGTH_SHORT).show();
        }
        scheduleProgressDialog.dismiss();
    }

    @Override
    public void onFailure(Call<List<GsonModels.Event>> call, Throwable t) {
        Toast.makeText(getContext(), "Network error occurred", Toast.LENGTH_LONG).show();
        Log.d("TAG", "onFailure: " + t.toString());
        scheduleProgressDialog.dismiss();
    }

    public void inflateTabs(List<GsonModels.Event> eventList) {
        for (GsonModels.Event event : eventList) {
            if (event.getDay().get_1()) {
                day1List.add(event);
            }
            if (event.getDay().get_2()) {
                day2List.add(event);
            }
            if (event.getDay().get_3()) {
                day3List.add(event);
            }
            if (event.getDay().get_4()) {
                day4List.add(event);
            }
        }
        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.scheduleTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Day1"));
        tabLayout.addTab(tabLayout.newTab().setText("Day2"));
        tabLayout.addTab(tabLayout.newTab().setText("Day3"));
        tabLayout.addTab(tabLayout.newTab().setText("Day4"));
        final ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.scheduleViewPager);

        viewPager.setAdapter(new ScheduleFragment.PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    SingleDayFragment tab1 = new SingleDayFragment(getContext(), day1List);
                    return tab1;
                case 1:
                    SingleDayFragment tab2 = new SingleDayFragment(getContext(), day2List);
                    return tab2;
                case 2:
                    SingleDayFragment tab3 = new SingleDayFragment(getContext(), day3List);
                    return tab3;
                case 3:
                    SingleDayFragment tab4 = new SingleDayFragment(getContext(), day4List);
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
