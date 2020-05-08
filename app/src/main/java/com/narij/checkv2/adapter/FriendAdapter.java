package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {

    APIInterface apiInterface;

    //private ArrayList<Duty> duties = new ArrayList<>();
    Context context;

    private ArrayList<Integer> selectedUsers = new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Integer> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(ArrayList<Integer> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dutyView = inflater.inflate(R.layout.recycler_item_friend, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(dutyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final User user = Globals.registeredUsers.get(i);
        myViewHolder.txtName.setText(user.getName());
        myViewHolder.txtPhone.setText(user.getPhone());

        String p = Globals.PROFILE_URL +  user.getId() + "/" +  Globals.loggedInUser.getAvatar();
//        Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);


        Picasso.get()
                .load(p)
                .placeholder(R.drawable.profile_default)
                .into(myViewHolder.imgProfile);


        myViewHolder.btnFriendshipOperate.setText(Globals.friendshipAction.get(user.getFriendshipType().getId()));

        myViewHolder.btnFriendshipOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                apiInterface = APIClient.getClient().create(APIInterface.class);
                myViewHolder.btnFriendshipOperate.setText(myViewHolder.btnFriendshipOperate.getText() + "...");
                int command = Globals.friendshipActionCode.get(user.getFriendshipType().getId());

                Call<WebServiceMessage> call = apiInterface.sendFriendshipSituation(
                        Globals.loggedInUser.getId(),
                        user.getId(),
                        command,
                        System.currentTimeMillis());
                call.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
                        WebServiceMessage webServiceMessage = response.body();
                        if (webServiceMessage.isError() == false) {
                            user.getFriendshipType().setId(command);
                            myViewHolder.btnFriendshipOperate.setText(Globals.friendship.get(command));
                        }
                    }
                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return Globals.registeredUsers.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtName;
        TextView txtPhone;
        Button btnFriendshipOperate;
        CircleImageView imgProfile;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            btnFriendshipOperate = itemView.findViewById(R.id.btnFriendshipOperate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }


}
