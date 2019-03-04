package com.erez8.chatpic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erez8.chatpic.ui.main.newMSGadapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;


public class NewMessageFrag extends Fragment {

            DatabaseReference firebaseDatabase;
            DatabaseReference ref;
            RecyclerView recyclerview;
            RecyclerView.Adapter adapter;
            RecyclerView.LayoutManager layoutManager;
    final List<Users> users = new ArrayList<>();

    public NewMessageFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_new_message, container, false);
        recyclerview = view.findViewById(R.id.newMSGrecview);
        recyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
    recyclerview.setLayoutManager(layoutManager);


        fethUsers();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void fethUsers() {

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("/Users");
    ref = firebaseDatabase.getRef();
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            users.clear();

            for (DataSnapshot item: dataSnapshot.getChildren()) {
                Users usersClass = item.getValue(Users.class);

                users.add(new Users(usersClass.getName(),usersClass.getUserImage(),usersClass.getUid()));

            }

            adapter = new newMSGadapter(users);

            recyclerview.setAdapter(adapter);

            ((newMSGadapter)adapter).setOnItemClickListener(new newMSGadapter.myClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId",users.get(position).getUid());
                    bundle.putString("username", users.get(position).getEmail());
                    Navigation.findNavController(v).navigate(R.id.fromNewMSGtoChatLog,bundle);
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
