package com.narij.checkv2.activity;

import android.os.Bundle;

import com.narij.checkv2.R;
import com.narij.checkv2.adapter.FriendAdapter;
import com.baoyz.widget.PullRefreshLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsActivity extends AppCompatActivity {

    PullRefreshLayout layout;
    FriendAdapter adapter;
    RecyclerView rcFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        rcFriends = findViewById(R.id.rcFriends);
        adapter = new FriendAdapter();

        rcFriends.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FriendsActivity.this, RecyclerView.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        rcFriends.setLayoutManager(layoutManager);


        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);

//        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }
}
