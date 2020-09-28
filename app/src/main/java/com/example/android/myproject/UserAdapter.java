package com.example.android.myproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{
    private Context mContext;
    private List<Users> usersList;
    private int lastPosition = -1;

    public UserAdapter(Context mContext, List<Users> usersList) {
        this.mContext = mContext;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_recycle_row_item,parent,false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        holder.userName.setText(usersList.get(position).getName());
        holder.mcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UserDetail.class);
                intent.putExtra("username",usersList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("email",usersList.get(holder.getAdapterPosition()).getEmail());
                intent.putExtra("phone",usersList.get(holder.getAdapterPosition()).getPhone());
                intent.putExtra("gender",usersList.get(holder.getAdapterPosition()).getGender());
                intent.putExtra("key",usersList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("status",usersList.get(holder.getAdapterPosition()).getStatus());
                mContext.startActivity(intent);
            }
        });
        setAnimation(holder.itemView,position);
    }
    public void setAnimation(View viewToAnimate,int position){

        if (position>lastPosition)
        {
            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    @Override
    public int getItemCount() {
        return usersList.size(); }
    public void filteredList(ArrayList<Users> filterList) {
        usersList = filterList;
        notifyDataSetChanged();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder{

    TextView userName;
    CardView mcardView;
    public UserViewHolder( View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.textUserName);
        mcardView = itemView.findViewById(R.id.mycardviewAdminPanel);
    }
}
