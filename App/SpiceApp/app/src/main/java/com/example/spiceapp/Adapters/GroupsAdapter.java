package com.example.spiceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.Group;
import com.example.spiceapp.FirebaseObjects.User;
import com.example.spiceapp.GroupMessageActivity;
import com.example.spiceapp.MessageActivity;
import com.example.spiceapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private Context mContext;
    private List<Group> mGroups;
    private StorageReference storageReference;
    private FirebaseStorage storage;




    public GroupsAdapter(Context mContext, List<Group> mGroups){
        this.mContext = mContext;
        this.mGroups = mGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent,false);
        return new GroupsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = mGroups.get(position);
        System.out.println("GROUP NAME " + group.getGroupName());
        holder.groupName.setText(group.getGroupName());

        // add profile picture
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Task<Uri> task = storageReference.child("images/"+group.getGroupName()).getDownloadUrl();
        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                System.out.println("Failed to load pic");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupMessageActivity.class);
                intent.putExtra("groupName", group.getGroupName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView groupName;
        public ImageView profileImage;

        public ViewHolder(View itemView){
            super(itemView);

            groupName = itemView.findViewById(R.id.contactName);
            profileImage = itemView.findViewById(R.id.profileImg);
        }


    }
}
