package com.example.android.myproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecordFragment extends Fragment {

    private DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView mRecyclerView;
    EditText txt_Search3;
    List<Record> myRecord;
    String key;
    String CurrentUser;
    FloatingActionButton upPicbtn2;
    Myadapter3 myAdapter3 = new Myadapter3(getContext(), myRecord);
    private Context mContext;
    private View v;

    public AddRecordFragment() {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_record, container, false);
        ChildDataActivity activity = (ChildDataActivity) getActivity();
        assert activity != null;
        key = activity.getMyData();
        txt_Search3 = v.findViewById(R.id.txt_searchtext3);
        mRecyclerView = v.findViewById(R.id.recyclerView3);
        upPicbtn2 = v.findViewById(R.id.uploadPicbtn2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        myRecord = new ArrayList<>();
        myAdapter3 = new Myadapter3(getActivity(), myRecord);
        mRecyclerView.setAdapter(myAdapter3);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Records").child(CurrentUser).child(key);
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRecord.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Record addChilddata1 = itemSnapshot.getValue(Record.class);
                    addChilddata1.setKey(itemSnapshot.getKey());
                    myRecord.add(addChilddata1);
                }
                myAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        upPicbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadRecord.class);
                intent.putExtra("keyValue", key);
                startActivity(intent);
            }
        });
        txt_Search3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return v;
    }

    private void filter(String text) {
        ArrayList<Record> filterList = new ArrayList<>();
        for (Record item : myRecord) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }
        myAdapter3.filteredList(filterList);
    }

}
