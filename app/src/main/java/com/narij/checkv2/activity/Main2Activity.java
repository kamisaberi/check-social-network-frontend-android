package com.narij.checkv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.narij.checkv2.R;
import com.narij.checkv2.adapter.DutyCardViewAdapter;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.fragment.BottomNavigationDrawerFragment;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitGroups;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    APIInterface apiInterface;

    //    private int currentFabAlignmentMode;
    BottomAppBar bottom_app_bar;
//    CoordinatorLayout coordinatorLayout2;

//    Button toggle_fab_alignment_button;

    FloatingActionButton fab;
    RecyclerView rcDuties;


    ImageView imgLoading;
//    TextView screen_label;

    FloatingActionButton.OnVisibilityChangedListener addVisibilityChanged;


//    public void createGroupsData(ArrayList<Group> groups) {
//        for (Group group : groups) {
//            ExpandableGroupHeaderItem g = new ExpandableGroupHeaderItem(group, getActivity());
//            for (Duty duty : group.getDuties()) {
//                DutySubItem d = new DutySubItem(duty, getActivity());
//                g.addSubItem(d);
//            }
//            mItems.add(g);
//        }
//    }

    ArrayList<Duty> duties = new ArrayList<>();
    DutyCardViewAdapter adapter;


    ChipGroup chgGroups;

    PullRefreshLayout layout;


    private Chip createFilterChip(Group group) {
        Chip chip = (Chip) LayoutInflater.from(getBaseContext()).inflate(R.layout.chip_group_filter, null);
        chip.setText(group.getTitle());
        chip.setTag(group.getId() + "");
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                                rcDuties.stopScroll();
//                                Log.d(Globals.LOG_TAG, "computing :" + rcDuties.isComputingLayout());

                if (isChecked) {
//                                    Log.d(Globals.LOG_TAG, "TAG : " + chip.getTag() + "");
                    adapter.getFilter().filter((CharSequence) chip.getTag());
                    adapter.notifyDataSetChanged();
//                                    Toast.makeText(Main2Activity.this, chip.getText(), Toast.LENGTH_LONG).show();
                }

                boolean b = false;
                for (int i = 0; i < chgGroups.getChildCount(); i++) {
                    Chip chip1 = (Chip) chgGroups.getChildAt(i);
                    if (chip1.isChecked()) {
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    adapter.getFilter().filter("");
                    adapter.notifyDataSetChanged();
                }


            }
        });
        return chip;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (Globals.DEBUG_MODE) {
            Globals.loggedInUser.setId(35);
        }

        imgLoading = findViewById(R.id.imgLoading);
        Glide.with(getBaseContext())
                .load(R.drawable.ic_loading)
                .into(imgLoading);
        chgGroups = findViewById(R.id.chgGroups);

        Call<RetrofitGroups> call = apiInterface.getDutiesAndGroups(Globals.loggedInUser.getId(), System.currentTimeMillis());
        call.enqueue(new Callback<RetrofitGroups>() {
            @Override
            public void onResponse(Call<RetrofitGroups> call, Response<RetrofitGroups> response) {
                RetrofitGroups retrofitGroups = response.body();
                if (retrofitGroups.isError() == false) {
                    Globals.setGroups(retrofitGroups.getGroups());
//                    Log.d(Globals.LOG_TAG, Globals.groups.size() + "");
                    for (Group group : Globals.groups) {
                        for (Duty duty : group.getDuties()) {
                            duty.setGroupids(group.getId() + "");
                            duties.add(duty);
//                            Log.d(Globals.LOG_TAG, duty.getGroupids());
                        }
                        chgGroups.addView(createFilterChip(group));
//                        TextViewCompat.setTextAppearance(chip, R.style.Widget_MaterialComponents_Chip_Filter);

//                        chip.setText("HIIIII");

                    }

                    adapter = new DutyCardViewAdapter(duties);
                    rcDuties.setAdapter(adapter);
                    rcDuties.setHasFixedSize(true);
                    rcDuties.setItemAnimator(new SlideInUpAnimator());

                    LinearLayoutManager layoutManager = new LinearLayoutManager(Main2Activity.this, RecyclerView.VERTICAL, false);
                    layoutManager.scrollToPosition(0);
                    rcDuties.setLayoutManager(layoutManager);

                    imgLoading.setVisibility(View.GONE);
                    rcDuties.setVisibility(View.VISIBLE);
//                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Main2Activity.this, 1);
//                    rcDuties.setLayoutManager(mLayoutManager);
//                    rcDuties.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//                    rcDuties.setItemAnimator(new DefaultItemAnimator());
//                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<RetrofitGroups> call, Throwable t) {

            }
        });

        Call<RetrofitGroups> call1 = apiInterface.getGroups(Globals.loggedInUser.getId(), System.currentTimeMillis());
        call1.enqueue(new Callback<RetrofitGroups>() {
            @Override
            public void onResponse(Call<RetrofitGroups> call, Response<RetrofitGroups> response) {

                RetrofitGroups retrofitGroups = response.body();
                if (retrofitGroups.isError() == false) {
                    Globals.justGroups = retrofitGroups.getGroups();

                }
            }

            @Override
            public void onFailure(Call<RetrofitGroups> call, Throwable t) {

            }
        });


//        currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;
        fab = findViewById(R.id.fab);
        bottom_app_bar = findViewById(R.id.bottom_app_bar);

        rcDuties = findViewById(R.id.rcDuties);
        rcDuties.setHasFixedSize(true);
        rcDuties.setItemViewCacheSize(20);
        rcDuties.setDrawingCacheEnabled(true);
        rcDuties.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

//        rcDuties.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });


//        screen_label = findViewById(R.id.screen_label);
//        coordinatorLayout2 = findViewById(R.id.coordinatorLayout2);

        setSupportActionBar(bottom_app_bar);

        addVisibilityChanged = new FloatingActionButton.OnVisibilityChangedListener() {

            @Override
            public void onShown(FloatingActionButton fab) {
                super.onShown(fab);
            }

            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);

                if (bottom_app_bar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
//                    currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END;
                    bottom_app_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

                } else {
//                    currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;
                    bottom_app_bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                }
                if (bottom_app_bar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                    bottom_app_bar.replaceMenu(R.menu.bottomappbar_menu_primary);
                    fab.setImageDrawable(getDrawable(R.drawable.baseline_add_white_24));
//                    fab.show();
                } else {
//                    bottom_app_bar.replaceMenu(R.menu.bottomappbar_menu_secondary);
                    fab.setImageDrawable(getDrawable(R.drawable.baseline_reply_white_24));
//                    fab.show();
                }
                fab.show();

//                if (bottom_app_bar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
//
//                    bottom_app_bar.replaceMenu(R.menu.bottomappbar_menu_primary);
////                    fab.setImageDrawable(getDrawable(R.drawable.baseline_add_white_24));
////                    fab.show();
//
//                } else {
//                    bottom_app_bar.replaceMenu(R.menu.bottomappbar_menu_secondary);
////                    fab.setImageDrawable(getDrawable(R.drawable.baseline_reply_white_24));
////                    fab.show();
//
//                }


            }
        };

//        toggle_fab_alignment_button = findViewById(R.id.toggle_fab_alignment_button);
//        toggle_fab_alignment_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fab.hide(addVisibilityChanged);
//
////                invalidateOptionsMenu();
////                if (bottom_app_bar.getNavigationIcon() != null) {
////                    bottom_app_bar.setNavigationIcon(null);
////                } else {
////                    bottom_app_bar.setNavigationIcon(R.drawable.baseline_menu_white_24);
////                }
//
//
//                switch (screen_label.getText() + "") {
//                    case "Primary Screen":
//                        screen_label.setText(getString(R.string.secondary_sceen_text));
//                        break;
//                    case "Secondary Screen":
//                        screen_label.setText(getString(R.string.primary_screen_text));
//                        break;
//                }
//            }
//        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                displayMaterialSnackBar();


                Intent intent = new Intent(Main2Activity.this, AddDuty2Activity.class);
                startActivity(intent);

            }
        });


        layout = findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call<RetrofitGroups> call = apiInterface.getDutiesAndGroups(Globals.loggedInUser.getId(), System.currentTimeMillis());
                call.enqueue(new Callback<RetrofitGroups>() {
                    @Override
                    public void onResponse(Call<RetrofitGroups> call, Response<RetrofitGroups> response) {
                        RetrofitGroups retrofitGroups = response.body();


                        if (retrofitGroups.isError() == false) {
                            Globals.setGroups(retrofitGroups.getGroups());
                            chgGroups.removeAllViews();
                            duties.clear();
                            for (Group group : Globals.groups) {
                                for (Duty duty : group.getDuties()) {
                                    duty.setGroupids(group.getId() + "");
                                    duties.add(duty);
                                }

                                chgGroups.addView(createFilterChip(group));
                            }

//                            adapter = new DutyCardViewAdapter(duties);
//                            rcDuties.setAdapter(null);
//                            rcDuties.setAdapter(adapter);
//                            rcDuties.setHasFixedSize(true);
//                            rcDuties.setItemAnimator(new SlideInUpAnimator());
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(Main2Activity.this, RecyclerView.VERTICAL, false);
//                            layoutManager.scrollToPosition(0);
//                            rcDuties.setLayoutManager(layoutManager);
                            layout.setRefreshing(false);
                            adapter.setDuties(duties);
                            adapter.notifyDataSetChanged();
                            adapter.getFilter().filter("");
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitGroups> call, Throwable t) {

                        layout.setRefreshing(false);
                    }
                });

            }
        });


//        FloatingActionButton fab = findViewById(R.id.fab);
//
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(Main2Activity.this, "testststtsts", Toast.LENGTH_LONG).show();
//
//            }
//        });


//        ArrayList<String> stackImageModelArrayList = new ArrayList<>();
//        stackImageModelArrayList.add("http://img.timeinc.net/time/daily/2010/1011/poy_nomination_agassi.jpg");
//        stackImageModelArrayList.add("https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg");
//        stackImageModelArrayList.add("http://www.tiig-eg.com/uploads/d991c-person.jpg");
//        stackImageModelArrayList.add("https://engineering.unl.edu/images/staff/Kayla_Person-small.jpg");
//        stackImageModelArrayList.add("https://i.pinimg.com/originals/84/c0/2d/84c02d14934ede220f0de14224faff6b.jpg");
//        stackImageModelArrayList.add("http://www.pick-health.com/wp-content/uploads/2013/08/happy-person.jpg");
//        stackImageModelArrayList.add("https://www.thewrap.com/wp-content/uploads/2015/11/Donald-Trump.jpg");
//        stackImageModelArrayList.add("http://img.timeinc.net/time/daily/2010/1011/poy_nomination_agassi.jpg");
//        stackImageModelArrayList.add("https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg");
//        stackImageModelArrayList.add("http://www.tiig-eg.com/uploads/d991c-person.jpg");


//        StackImageView stackImageView = findViewById(R.id.stackImageView);
//        stackImageView.setImageUrlArrayList(stackImageModelArrayList);
//        stackImageView.setStackImageViewClickListener(new StackImageViewClickListener()
//        {
//            @Override
//            public void onStackImageViewClick()
//            {
//                Toast.makeText(Main2Activity.this, "onStackImageViewClick() method call.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // This is compulsory.
//        stackImageView.initViews();

    }


//    private void displayMaterialSnackBar() {
//
//        int marginSide = 0;
//        int marginBottom = 1000;
//
//        Snackbar snackbar = Snackbar.make(coordinatorLayout2, "FAB Clicked", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorSnackbarButton));
//        View snackbarView = snackbar.getView();
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams();
//        params.setMargins(
//                params.leftMargin + marginSide,
//                params.topMargin,
//                params.rightMargin + marginSide,
//                params.bottomMargin + marginBottom
//        );
//
//        snackbarView.setLayoutParams(params);
//        snackbar.show();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.bottomappbar_menu_primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag());
                break;

        }
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
