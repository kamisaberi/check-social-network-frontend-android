package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Comment;
import com.narij.checkv2.model.Conversation;
import com.narij.checkv2.model.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ConversationAdapter extends RecyclerView.Adapter {

    ArrayList<Conversation> conversations = new ArrayList<>();
    Context context;

    public ConversationAdapter(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case Conversation.COMMENT:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_log, viewGroup, false);
                return new CommentViewHolder(view);
            case Conversation.LOG:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_log, viewGroup, false);
                return new LogViewHolder(view);
            case Conversation.LEFT_MESSAGE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_log, viewGroup, false);
                return new LeftMessageViewHolder(view);
            case Conversation.RIGHT_MESSAGE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_log, viewGroup, false);
                return new RightMessageViewHolder(view);
            case Conversation.FILE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_log, viewGroup, false);
                return new FileViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Conversation conversation = conversations.get(i);
        if (conversation != null) {

            switch (conversation.getType()) {
                case Conversation.COMMENT:
                    ((CommentViewHolder) viewHolder).txtContent.setText(((Comment) conversation).getContent());
                    ((CommentViewHolder) viewHolder).txtTime.setText(new SimpleDateFormat(Globals.dateFormat).format(((Comment) conversation).getDate()));
                    break;
                case Conversation.LOG:
                    ((LogViewHolder) viewHolder).txtLog.setText(((Log) conversation).getLog());
                    ((LogViewHolder) viewHolder).txtTime.setText(new SimpleDateFormat(Globals.dateFormat).format(((Log) conversation).getDate()));
                    ((LogViewHolder) viewHolder).txtCreator.setText(((Log) conversation).getUser().getName());
                    break;
                case Conversation.LEFT_MESSAGE:

                    break;
                case Conversation.RIGHT_MESSAGE:

                    break;
                case Conversation.FILE:

                    break;
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return conversations.get(position).getType();

    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView txtContent;
        TextView txtTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        TextView txtLog;
        TextView txtCreator;
        TextView txtTime;

        public LogViewHolder(View itemView) {
            super(itemView);
            txtLog = itemView.findViewById(R.id.txtLog);
            txtCreator = itemView.findViewById(R.id.txtCreator);
            txtTime = itemView.findViewById(R.id.txtTime);
        }


    }

    class LeftMessageViewHolder extends RecyclerView.ViewHolder {


        TextView txtContent;
        TextView txtUser;
        TextView txtDate;
        CircleImageView imgProfile;

        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }


    }

    class RightMessageViewHolder extends RecyclerView.ViewHolder {

        TextView txtContent;
        TextView txtUser;
        TextView txtDate;
        CircleImageView imgProfile;

        public RightMessageViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }

    class FileViewHolder extends RecyclerView.ViewHolder {

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
