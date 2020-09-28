package com.example.android.myproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Myadapter3 extends RecyclerView.Adapter<RecordViewHolder> {
    private Context mContext;
    private List<Record> myRecord;

    public Myadapter3(Context mContext, List<Record> myRecord) {
        this.mContext = mContext;
        this.myRecord = myRecord;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_row_item3, parent, false);
        return new RecordViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordViewHolder holder, int position) {



        holder.mTitle.setText(myRecord.get(position).getTitle());
        holder.mDescription.setText(myRecord.get(position).getDescription());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailOfRecord.class);
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


class RecordViewHolder extends RecyclerView.ViewHolder {

    TextView mDescription;
    TextView mTitle;
    CardView mCardView;

    public RecordViewHolder(View itemView) {

        super(itemView);
        mDescription = itemView.findViewById(R.id.txtDescription3);
        mTitle = itemView.findViewById(R.id.txtTitle3);
        mCardView = itemView.findViewById(R.id.mycardview3);

    }
}