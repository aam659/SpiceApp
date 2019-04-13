package com.example.spiceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.User;
import com.example.spiceapp.MessageActivity;
import com.example.spiceapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewGroupAdapter extends RecyclerView.Adapter<NewGroupAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private ArrayList<String> checkedUsers = new ArrayList<>();


    public NewGroupAdapter(Context mContext, List<User> mUsers){
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_checkbox, parent,false);
        return new NewGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        String setName = user.getfName() + " " +user.getlName();
        holder.userName.setText(setName);

        //TODO: Somehow add profile picture

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sparseBooleanArray.get(position, false)){
                    holder.checkBox.setChecked(true);
                    sparseBooleanArray.put(position, true);
                    checkedUsers.add(user.getEmail());
                }
                else {
                    holder.checkBox.setChecked(false);
                    sparseBooleanArray.put(position, false);
                    checkedUsers.remove(user.getEmail());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public ImageView profileImage;
        public CheckBox checkBox;

        public ViewHolder(View itemView){
            super(itemView);

            userName = itemView.findViewById(R.id.contactName);
            profileImage = itemView.findViewById(R.id.profileImg);
            checkBox = itemView.findViewById(R.id.contactCheckbox);
        }


    }

    public ArrayList<String> getCheckedUsers(){
        return checkedUsers;
    }

}
