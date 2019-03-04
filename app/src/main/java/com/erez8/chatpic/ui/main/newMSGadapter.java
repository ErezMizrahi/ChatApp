package com.erez8.chatpic.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erez8.chatpic.R;
import com.erez8.chatpic.Users;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class newMSGadapter extends RecyclerView.Adapter<newMSGadapter.ViewHolder> {
    private List<Users> users;

    public newMSGadapter( List<Users> users) {
    this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext().getApplicationContext()).inflate(R.layout.single_message,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.username.setText(users.get(i).getEmail());
        Picasso.get().load(users.get(i).getUserImage()).into(holder.userIMG);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date, username;
        private CircleImageView userIMG;
        private  String userid;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            date = itemView.findViewById(R.id.msg_date);
            username = itemView.findViewById(R.id.user_name);
            userIMG = itemView.findViewById(R.id.user_img);

                itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            {
                myClickListener.onItemClick(getAdapterPosition(),v);

        }

    }

}

    public void setOnItemClickListener(myClickListener myClickListener)
    {
        this.myClickListener=myClickListener;

    }
    public interface myClickListener{
        void onItemClick(int position,View v);
    }
    //our click listener that will be sent by method (interface)
    private myClickListener myClickListener;


}

