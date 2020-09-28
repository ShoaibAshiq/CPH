package com.example.android.myproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<ChildViewHolder>{
    private Context mContext;
    String key,CurrentUser;
    private List<Child> childList;
    private int lastPosition = -1;

    public MyAdapter(Context mContext, List<Child> childList) {
        this.mContext = mContext;
        this.childList = childList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_row_item,parent,false);
        return new ChildViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildViewHolder holder, final int position) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }
        holder.cName.setText(childList.get(position).getName());
        holder.cDob.setText(childList.get(position).getDob());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = childList.get(holder.getAdapterPosition()).getKey();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Childs").child(CurrentUser);
                databaseReference.child(key).removeValue();
//                childList.remove(holder.getAdapterPosition());
                final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Pictures").child(CurrentUser);
                databaseRef.child(key).removeValue();
                final DatabaseReference databaseRefe = FirebaseDatabase.getInstance().getReference("Records").child(CurrentUser);
                databaseRefe.child(key).removeValue();
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),childList.size());

            }
        });

        holder.mcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChildDataActivity.class);
                intent.putExtra("keyValue",childList.get(holder.getAdapterPosition()).getKey());
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
        return childList.size();
    }

    public void filteredList(ArrayList<Child> filterList) {
        childList = filterList;
        notifyDataSetChanged();
    }
}

class ChildViewHolder extends RecyclerView.ViewHolder {

    TextView cName,cDob;
    CardView mcardView;
    ImageView delete;
    public ChildViewHolder( View itemView) {
        super(itemView);
        cName = itemView.findViewById(R.id.textViewName);
        cDob = itemView.findViewById(R.id.textViewDOB);
        delete = itemView.findViewById(R.id.deleteChildRBtn);
        mcardView = itemView.findViewById(R.id.mycardview);
    }
}
