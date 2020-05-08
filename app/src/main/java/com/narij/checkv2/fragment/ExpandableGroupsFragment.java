package com.narij.checkv2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitGroups;
import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpandableGroupsFragment extends Fragment{

    PullRefreshLayout layout;



    protected static final String ARG_COLUMN_COUNT = "column_count";
    protected int mColumnCount = 2;
    public RecyclerView mRecyclerView;
//    ArrayList<MyExpandableGroup> exgs = new ArrayList<>();
    public int nextindex = 4;

    public static ExpandableGroupsFragment newInstance(int columnCount) {
        ExpandableGroupsFragment fragment = new ExpandableGroupsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExpandableGroupsFragment() {
    }

    APIInterface apiInterface;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (Globals.groups.size() == 0) {
            Call<RetrofitGroups> call = apiInterface.getDutiesAndGroups(Globals.loggedInUser.getId(), System.currentTimeMillis());
            call.enqueue(new Callback<RetrofitGroups>() {
                @Override
                public void onResponse(Call<RetrofitGroups> call, Response<RetrofitGroups> response) {
                    RetrofitGroups retrofitGroups = response.body();
                    if (retrofitGroups.isError() == false) {
                        Globals.setGroups(retrofitGroups.getGroups());
                        createGroupsData(Globals.groups);
                    }
                }

                @Override
                public void onFailure(Call<RetrofitGroups> call, Throwable t) {

                }
            });

        } else {
            createGroupsData(Globals.groups);
        }

    }

    public void createGroupsData(ArrayList<Group> groups) {
        for (Group group : groups) {
            for (Duty duty : group.getDuties()) {
            }
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray("mItems", Globals.getGroups());
        outState.putString("name", "reza");
        Log.d(Globals.LOG_TAG, "SAVED");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        layout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


//                layout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        layout.setRefreshing(false);
//                    }
//                }, 3000);


                Call<RetrofitGroups> call = apiInterface.getDutiesAndGroups(Globals.loggedInUser.getId(), System.currentTimeMillis());
                call.enqueue(new Callback<RetrofitGroups>() {
                    @Override
                    public void onResponse(Call<RetrofitGroups> call, Response<RetrofitGroups> response) {
                        RetrofitGroups retrofitGroups = response.body();
                        if (retrofitGroups.isError() == false) {
                            Globals.setGroups(retrofitGroups.getGroups());
                            createGroupsData(Globals.groups);
                            layout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitGroups> call, Throwable t) {

                        layout.setRefreshing(false);
                    }
                });

            }
        });
        return view;
    }


    @SuppressWarnings({"ConstantConditions", "NullableProblems"})


    //    @Override
//    public void showNewLayoutInfo(MenuItem item) {
//        super.showNewLayoutInfo(item);
//        mRecyclerView.setAdapter(mAdapter);
//    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

}