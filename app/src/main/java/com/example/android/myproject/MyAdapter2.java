package com.example.android.myproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<PictureViewHolder> {

    String id="";
    private Context mContext;
    private List<Record> myRecord;
    private int lastPosition = -1;

    public MyAdapter2(Context mContext, List<Record> myRecord) {
        this.mContext = mContext;
        this.myRecord = myRecord;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_row_item2, parent, false);
        return new PictureViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PictureViewHolder holder, int position) {


        Glide.with(mContext)
                .load(myRecord.get(position).getImage())
                .into(holder.imageView);
        holder.mTitle.setText(myRecord.get(position).getTitle());
        holder.mDescription.setText(myRecord.get(position).getDescription());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailOfPictures.class);
                intent.putExtra("image",myRecord.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("title",myRecord.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("descrip",myRecord.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("key",myRecord.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("userkey",myRecord.get(holder.getAdapterPosition()).getUserkey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myRecord.size();
    }

    public void filteredList(ArrayList<Record> filterList) {
        myRecord = filterList;
        notifyDataSetChanged();
    }

}

class PictureViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView mDescription;
    TextView mTitle;
    CardView mCardView;

    public PictureViewHolder(View itemView) {

        super(itemView);
        imageView = itemView.findViewById(R.id.ivImage);
        mDescription = itemView.findViewById(R.id.txtDescription);
        mTitle = itemView.findViewById(R.id.txtTitle);
        mCardView = itemView.findViewById(R.id.mycardview2);
    }
}
