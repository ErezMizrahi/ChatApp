package com.erez8.chatpic;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.erez8.chatpic.ui.main.inMessage;
import com.erez8.chatpic.ui.main.latestMSGAdapter;
import com.erez8.chatpic.ui.main.newMSGadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.navigation.Navigation;


public class LatestMessagesFragment extends Fragment {
    private View view;
    private List<inMessage> messages ;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private EditText shortCutMessage;


    private  boolean altLayout;



    public LatestMessagesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     view =  inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);


        recyclerView = view.findViewById(R.id.latest_messages_recview);
        shortCutMessage = view.findViewById(R.id.editText2);



        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        setLatestMessages();




        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }

    private HashMap<String,inMessage> hashMap = new HashMap<>();

    private void setLatestMessages() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/latest-message/"+user);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                inMessage inMessage = dataSnapshot.getValue(inMessage.class);
                hashMap.put(dataSnapshot.getKey(),inMessage);
                refreshRecyclerView();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                inMessage inMessage = dataSnapshot.getValue(inMessage.class);
                hashMap.put(dataSnapshot.getKey(),inMessage);
                refreshRecyclerView();


                NotificationCompat.Builder b = new NotificationCompat.Builder(getContext());

                b
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.com_facebook_button_icon)
                        .setTicker("Hearty365")
                        .setContentTitle("Default notification")
                        .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                        .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);


                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, b.build());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void refreshRecyclerView() {
        messages.clear();
        for (inMessage m :
                hashMap.values()   ) {
                messages.add(m);
            adapter = new latestMSGAdapter( messages,getContext());
            recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        ((latestMSGAdapter)adapter).setOnItemClickListener(new latestMSGAdapter.myClickListener() {
            @Override
            public void onItemClick(int position, View v, ConstraintLayout layout,ConstraintSet constraintSetOld, ConstraintSet constraintSetNew) {

                Transition changeBounds = new ChangeBounds();

                changeBounds.setInterpolator(new OvershootInterpolator());

                TransitionManager.beginDelayedTransition(layout,changeBounds);

                if (!altLayout){
                    constraintSetNew.applyTo(layout);
                    altLayout = true;
                }else{
                     constraintSetOld.applyTo(layout);
                     altLayout = false;
                }

            }
        });

    }


            @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.newMessage:

                Navigation.findNavController(view).navigate(R.id.toNewMessageFrag);
        break;

            case R.id.SignOut:
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.backtoHome);
        default:
            break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
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
