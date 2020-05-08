package com.narij.checkv2.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.narij.checkv2.R;
import com.narij.checkv2.adapter.FriendAdapter;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.ContactModel;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitRegisteredUsers;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    PullRefreshLayout layout;
    FriendAdapter adapter;
    RecyclerView rcFriends;

    APIInterface apiInterface;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        rcFriends = view.findViewById(R.id.rcFriends);
        adapter = new FriendAdapter();

        rcFriends.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        rcFriends.setLayoutManager(layoutManager);

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

                ArrayList<String> phones = new ArrayList<>();
                for (ContactModel cm : Globals.contacts) {
                    phones.add(cm.mobileNumber);
                }

                String strphones = TextUtils.join(",", phones);
//                strphones = "+989112547896,+981245692322,+961442525252,+989304982248,+145236521412,+989111425011,+989113334886";
                Log.d(Globals.LOG_TAG, strphones);
                Call<RetrofitRegisteredUsers> call = apiInterface.getRegisteredUsers(
                        Globals.loggedInUser.getId(),
                        strphones,
                        System.currentTimeMillis());
                call.enqueue(new Callback<RetrofitRegisteredUsers>() {
                    @Override
                    public void onResponse(Call<RetrofitRegisteredUsers> call, Response<RetrofitRegisteredUsers> response) {
                        RetrofitRegisteredUsers retrofitRegisteredUsers = response.body();
                        if (retrofitRegisteredUsers.isError() == false) {
                            Globals.registeredUsers = retrofitRegisteredUsers.getUsers();
                            adapter.notifyDataSetChanged();
                            layout.setRefreshing(false);
//                            Log.d(Globals.LOG_TAG, "REGISTERED USERS : " + Globals.registeredUsers.size());
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitRegisteredUsers> call, Throwable t) {
                        layout.setRefreshing(false);
                    }
                });


            }
        });

        return view;
    }

}
