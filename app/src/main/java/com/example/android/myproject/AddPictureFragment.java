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
import android.widget.Toast;

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
public class AddPictureFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    RecyclerView mRecyclerView;
    EditText txt_Search2;
    List<Record> myRecord;
    String key;
    String CurrentUser;
    FloatingActionButton upPicbtn;
    MyAdapter2 myAdapter2 = new MyAdapter2(getContext(), myRecord);
    private Context mContext;
    private View v;

    public AddPictureFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_picture, container, false);
        ChildDataActivity activity = (ChildDataActivity) getActivity();
        assert activity != null;
        key = activity.getMyData();

        txt_Search2 = v.findViewById(R.id.txt_searchtext2);
        mRecyclerView = v.findViewById(R.id.recyclerView2);
        upPicbtn = v.findViewById(R.id.uploadPicbtn);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        myRecord = new ArrayList<>();
        myAdapter2 = new MyAdapter2(getActivity(), myRecord);
        mRecyclerView.setAdapter(myAdapter2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Pictures").child(CurrentUser).child(key);
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRecord. clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Record addChilddata1 = itemSnapshot.getValue(Record.class);
                    addChilddata1.setKey(itemSnapshot.getKey());
                    myRecord.add(addChilddata1);
                }
                myAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        upPicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadPicture.class);
                intent.putExtra("keyValue", key);
                Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        txt_Search2.addTextChangedListener(new TextWatcher() {
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
        myAdapter2.filteredList(filterList);
    }


    }


