package com.narij.checkv2.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.narij.checkv2.R;
import com.narij.checkv2.activity.AccountActivity;
import com.narij.checkv2.activity.AddDuty2Activity;
import com.narij.checkv2.activity.Duty2Activity;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Expert;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DutyCardViewAdapter extends RecyclerView.Adapter<DutyCardViewAdapter.MyViewHolder> implements Filterable {


    //private ArrayList<Duty> duties = new ArrayList<>();
    Context context;
    Activity activity;
    private List<Duty> contactListFiltered;
    private ContactsAdapterListener listener;

    public DutyCardViewAdapter(ArrayList<Duty> duties) {
        Globals.duties = duties;
        this.contactListFiltered = duties;

    }

    public ArrayList<Duty> getDuties() {
        return Globals.duties;
    }

    public void setDuties(ArrayList<Duty> duties) {
        Globals.duties = duties;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dutyView = inflater.inflate(R.layout.recycler_duty_card_view_item, viewGroup, false);

        MyViewHolder viewHolder = new MyViewHolder(dutyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final Duty duty = contactListFiltered.get(i);
        myViewHolder.txtUser.setText(duty.getCreator().getName());
        myViewHolder.txtTitle.setText(StringUtils.capitalize(duty.getTitle().toLowerCase().trim()));
        myViewHolder.txtLogsCount.setText(duty.getLogs().size() + "");
        myViewHolder.txtRemainingTime.setText("0");


        String p = Globals.PROFILE_URL + duty.getCreator().getId() + "/" + Globals.loggedInUser.getAvatar();
        Picasso.get().load(p).placeholder(R.drawable.profile_default).into(myViewHolder.imgProfile);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountActivity.class);
                intent.putExtra("user", duty.getCreator().getId());
                ActivityCompat.startActivity(context, intent, null);

            }
        };

        myViewHolder.imgProfile.setOnClickListener(onClickListener);
        myViewHolder.txtUser.setOnClickListener(onClickListener);


        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(Globals.dateTimeFormat);
        Date d1 = new Date(duty.getStartDate());
        long now = new Date().getTime();
        String s1 = dateFormat.format(d1);
        long start = duty.getStartDate();
        long end = start + duty.getDuration();
        long diff = end - now;
        long diffd = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        long d = diff % Globals.milliseconds.get("Day");
        long diffh = TimeUnit.HOURS.convert(d, TimeUnit.MILLISECONDS);

        if (duty.getFinishType() == 0) {
            if (now >= start && now < end) {

                String txt = (diffd == 0 ? "" : diffd + " days & ") + diffh + " hours";

                if (duty.isExactTime() == false) {

                    txt += " left";
                    myViewHolder.txtRemainingTime.setText(txt);
                } else {

                    txt = "do in" + txt;
                    myViewHolder.txtRemainingTime.setText(txt);
                }
//                if (diffd == 0) {
//                    myViewHolder.txtRemainingTime.setTextColor(ContextCompat.getColor(context, R.color.tokyoColorAccent));
//                } else {
//                    myViewHolder.txtRemainingTime.setTextColor(ContextCompat.getColor(context, R.color.tokyoColorNormal));
//                }

            } else if (now >= end) {


                if (duty.isCanContinueAfterTimeout() == false) {
                    myViewHolder.txtRemainingTime.setText("finished");
//                    myViewHolder.txtRemainingTime.setTextColor(ContextCompat.getColor(context, R.color.tokyoColorNormal));
                } else {

                    String txt = (diffd == 0 ? "" : Math.abs(diffd) + " days & ") + Math.abs(diffh) + " hours";
                    txt = "passed " + txt;
                    myViewHolder.txtRemainingTime.setText(txt);
//                    myViewHolder.txtRemainingTime.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                }


            } else if (now < start) {

                myViewHolder.txtRemainingTime.setText("not started yet");
            }
        } else if (duty.getFinishType() == 1) {
            String x = "succeed at ";
            x += new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(duty.getFinishTime())) + "";
            myViewHolder.txtRemainingTime.setText(x);
//            myViewHolder.txtRemainingTime.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else if (duty.getFinishType() == 2) {
            String x = "failed at ";
            x += new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(duty.getFinishTime())) + "";
            myViewHolder.txtRemainingTime.setText(x);
//            myViewHolder.txtRemainingTime.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }

        myViewHolder.imbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_duty_more_items);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnEdit = dialog.findViewById(R.id.btnEdit);
                Button btnShare = dialog.findViewById(R.id.btnShare);


                if (duty.getCreator().getId() != Globals.loggedInUser.getId()) {
                    btnEdit.setEnabled(false);
                } else {
                    btnEdit.setEnabled(true);
                }

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, AddDuty2Activity.class);
                        intent.putExtra("duty", duty.getId());
                        ActivityCompat.startActivity(context, intent, null);
                        dialog.dismiss();
                    }
                });

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        try {
            myViewHolder.pnlDutyData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Globals.selectedDutyIndex = i;
                        Globals.selectedDutyId = duty.getId();
                        Intent intent = new Intent(context, Duty2Activity.class);
                        intent.putExtra("id", duty.getId());
                        ActivityCompat.startActivity(context, intent,
                                ActivityOptionsCompat
                                        .makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight())
                                        .toBundle()
                        );
                    } catch (Exception e) {

                    }
                }
            });

        } catch (Exception e) {
        }


        for (Expert expert : duty.getExperts()) {

            Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.chip_card_expert, null);
            chip.setText(expert.getTitle());
            chip.setTag(expert.getId() + "");
            myViewHolder.chgExperts.addView(chip);

        }

    }

    @Override
    public int getItemCount() {
        return contactListFiltered != null ? contactListFiltered.size() : 0;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.d(Globals.LOG_TAG, "FILTER : " + charString);
                if (charString.isEmpty()) {
                    contactListFiltered = getDuties();
                } else {
                    List<Duty> filteredList = new ArrayList<>();
                    for (Duty duty : getDuties()) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (duty.getGroupids().trim().equals(charSequence)) {
//                            Log.d(Globals.LOG_TAG, "GROUP ID : " + duty.getGroupids() + "");
                            filteredList.add(duty);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Duty>) filterResults.values;
//                Log.e(Globals.LOG_TAG, "DOOOONNNNENENNENE");
//                notifyDataSetChanged();
            }
        };

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        //        TextView txtCreator;
        TextView txtTitle;
        TextView txtRemainingTime;
        TextView txtLogsCount;
        TextView txtLocation;
        TextView txtUser;
        CircleImageView imgProfile;
        AppCompatImageButton imbMore;
        ChipGroup chgExperts;
        ViewGroup pnlUserData;
        ViewGroup pnlDutyData;


        public MyViewHolder(View itemView) {
            super(itemView);
//            txtCreator = itemView.findViewById(R.id.txtCreator);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRemainingTime = itemView.findViewById(R.id.txtRemainingTime);
            txtLogsCount = itemView.findViewById(R.id.txtLogsCount);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtUser = itemView.findViewById(R.id.txtUser);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            imbMore = itemView.findViewById(R.id.imbMore);
            chgExperts = itemView.findViewById(R.id.chgExperts);
            pnlUserData = itemView.findViewById(R.id.pnlUserData);
            pnlDutyData = itemView.findViewById(R.id.pnlDutyData);
        }
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Duty duty);
    }

}
