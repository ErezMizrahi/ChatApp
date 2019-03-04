package com.erez8.chatpic.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erez8.chatpic.R;
import com.erez8.chatpic.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class latestMSGAdapter  extends RecyclerView.Adapter<latestMSGAdapter.ViewHolder> {

    private List<inMessage> messages;
    private Context context;

    public latestMSGAdapter(List<inMessage> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sinle_latest_msg,viewGroup,false);
         return  new ViewHolder(view);

    }

 private List <Users> usersList = new ArrayList<>();
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        usersList.clear();
        final inMessage inMessage = messages.get(i);

        String userID = "";

        if (inMessage.getSender() .equals(FirebaseAuth.getInstance().getUid())){
            userID = inMessage.getReciver();
        }else{
            userID = inMessage.getSender();
        }

        viewHolder.msg.setText(inMessage.getMessage());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/Users/" + userID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Users users = dataSnapshot.getValue(Users.class);

                viewHolder.username.setText(users.getName());
                Picasso.get().load(users.getUserImage()).into(viewHolder.imageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView username, msg;
        private ConstraintLayout layout;
        ConstraintSet constraintSetOld = new ConstraintSet();
        ConstraintSet constraintSetNew= new ConstraintSet();
        private EditText quickMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.userImage_latestMessage);
            username = itemView.findViewById(R.id.username);
            msg = itemView.findViewById(R.id.msg);
            layout = itemView.findViewById(R.id.layout);

            constraintSetOld.clone(layout);
            constraintSetNew.clone(itemView.getContext(),R.layout.extended_sinle_latest_msg);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(),v,layout,constraintSetOld,constraintSetNew);
        }
    }

    public void setOnItemClickListener(latestMSGAdapter.myClickListener myClickListener)
    {
        this.myClickListener=myClickListener;

    }
    public interface myClickListener{
        void onItemClick(int position,View v, ConstraintLayout layout, ConstraintSet constraintSetOld, ConstraintSet constraintSetNew);
    }
    //our click listener that will be sent by method (interface)
    private latestMSGAdapter.myClickListener myClickListener;

}
