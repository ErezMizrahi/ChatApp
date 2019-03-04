package com.erez8.chatpic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erez8.chatpic.ui.main.inMessage;
import com.erez8.chatpic.ui.main.ChatMessagesAdapter;
import com.erez8.chatpic.ui.main.outMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ChatLog extends Fragment {

    private EditText message;
    private Button send;
    FirebaseUser fuser;

    private List<inMessage> messageFireBases = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter chatMessagesAdapter;
    RecyclerView.LayoutManager layoutManager;

    public ChatLog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).setActionBar(getArguments().getString("username"));
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_log, container, false);
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        message = view.findViewById(R.id.edittext_message);
        send = view.findViewById(R.id.send);

        recyclerView = view.findViewById(R.id.recyclerViewChat);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readMessages(fuser.getUid(),getArguments().getString("userId"));


            send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(),getArguments().getString("userId"),msg);
                    message.setText("");
                }
            }
        });

    }

    public void sendMessage(String sender, String reciver, String msg) {

        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference latestMsgfromUser = FirebaseDatabase.getInstance().getReference("/latest-message/"+sender+"/"+reciver);
        DatabaseReference latestMsgtoUser = FirebaseDatabase.getInstance().getReference("/latest-message/"+reciver+"/"+sender);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciver", reciver);
        hashMap.put("message", msg);

        chatReference.child("Chats").push().setValue(hashMap);
        latestMsgtoUser.setValue(hashMap);
        latestMsgfromUser.setValue(hashMap);
    }


    public void readMessages(final String myId, final String userId) {
        messageFireBases = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageFireBases.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    inMessage inMessage = data.getValue(com.erez8.chatpic.ui.main.inMessage.class);

                    if (inMessage.getReciver().equals(myId) && inMessage.getSender().equals(userId) ||
                            inMessage.getReciver().equals(userId) && inMessage.getSender().equals(myId)) {

                        messageFireBases.add(inMessage);
                    }
                    chatMessagesAdapter = new ChatMessagesAdapter(getContext(), messageFireBases);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(chatMessagesAdapter);
                    chatMessagesAdapter.notifyDataSetChanged();


                }
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

        ((MainActivity)getActivity()).setActionBar("ChatPic");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
