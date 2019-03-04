package com.erez8.chatpic.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erez8.chatpic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder> {

    private List<inMessage> messages;
    private static final int IN_MESSAGE = 1;
    private static final int OUT_MESSAGE = 2;

    FirebaseUser ffuser;

    private Context context;

    ViewHolder viewHolder ;


    public ChatMessagesAdapter(Context context, List<inMessage> messages) {
        this.messages = messages;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);

        switch (i)
        {
            case IN_MESSAGE:
                View view = inflater.inflate(R.layout.reciver_message_layout,viewGroup,false);
                viewHolder =  new ViewHolder(view);
                break;
            case OUT_MESSAGE:
                View view2 = inflater.inflate(R.layout.sender_message_layout,viewGroup,false);
                viewHolder = new ViewHolder(view2);
            break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

         inMessage inMessage = messages.get(i);
        viewHolder.msg.setText(inMessage.getMessage());



    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ffuser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(ffuser.getUid())) {
            return OUT_MESSAGE;
        }else{
            return IN_MESSAGE;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                msg = itemView.findViewById(R.id.show_message);


        }

    }
}
