package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private ArrayList<Message> messages = new ArrayList<>();
    Context context;

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dutyView = null;
//        Message message = messages.get(i);
//        android.util.Log.d(Globals.LOG_TAG, "m id :" + message.getId() + " u id:" + message.getUser().getId());
        if (i == 1) {
            dutyView = inflater.inflate(R.layout.recycler_item_message_right, viewGroup, false);
        } else {
            dutyView = inflater.inflate(R.layout.recycler_item_message_left, viewGroup, false);
        }
        MyViewHolder viewHolder = new MyViewHolder(dutyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Message message = messages.get(i);
        myViewHolder.txtContent.setText(message.getContent());
        myViewHolder.txtUser.setText(message.getUser().getUsername());
        myViewHolder.txtDate.setText(new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(message.getDate())));

//        if(message.getUser().getId() == Globals.loggedInUser.getId())
//        {
//            myViewHolder.txtUser.setText(Globals.loggedInUser.getUsername());
//        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, duty.getId() + " " + duty.getTitle() , Toast.LENGTH_LONG).show();
            }
        });

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });


//        setFadeAnimation(myViewHolder.itemView);
//        setScaleAnimation(myViewHolder.itemView);
    }

    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if (message.getUser().getId() == Globals.loggedInUser.getId()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        TextView txtUser;
        TextView txtDate;
        CircleImageView imgProfile;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }


}
