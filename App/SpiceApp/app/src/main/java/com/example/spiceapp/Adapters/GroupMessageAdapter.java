package com.example.spiceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.Chat;
import com.example.spiceapp.FirebaseObjects.User;
import com.example.spiceapp.MessageActivity;
import com.example.spiceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChats;
    private String imageURL;
    private FirebaseUser mUser;
    private DatabaseReference reference;

    public GroupMessageAdapter(Context mContext, List<Chat> mChats, String imageURL){
        this.mContext = mContext;
        this.mChats = mChats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public GroupMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_chat_right, parent, false);
            return new GroupMessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_chat_left, parent, false);
            return new GroupMessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChats.get(position);
        holder.show_message.setText(chat.getMessage());

        reference = FirebaseDatabase.getInstance().getReference("users").child(chat.getSender());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String nameSet = user.getfName() + " " + user.getlName();
                holder.senderName.setText(nameSet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO: ADD PROFILE PICTURE

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public TextView senderName;
        public ImageView profileImage;

        public ViewHolder(View itemView){
            super(itemView);

            senderName = itemView.findViewById(R.id.senderName);
            show_message = itemView.findViewById(R.id.showMessage);
            profileImage = itemView.findViewById(R.id.profileImg);
        }

    }

    @Override
    public int getItemViewType(int position) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(mUser.getEmail()
                .replace('.','_'))){
            return MSG_TYPE_RIGHT;
        }
        else
            return MSG_TYPE_LEFT;
    }
}