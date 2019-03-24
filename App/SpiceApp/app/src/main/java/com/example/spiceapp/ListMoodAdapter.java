package com.example.spiceapp;

/*This is a class to be used in conjuction with the ListMoods.class file
    This describes how the list items used in the recycler view that is used
    in the ListMoods.class will look. It is also in charge of populating the
    list and making sure that the information being shown to the user in the list is correct
    and accurately reflects the realtime database.
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.Mood;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ListMoodAdapter extends RecyclerView.Adapter<ListMoodAdapter.ListMoodViewHolder> {

    private Context mContext; //Current context
    private List<Mood> moodList; //Our list of moods
    private OnNoteListener onNoteListener;


    //Every ViewHolder will have an onclick listener


    //Constructor of our adapter
    public ListMoodAdapter(Context mContext, List<Mood> moodList, OnNoteListener onNoteListener){
        this.mContext = mContext;
        this.moodList = moodList;
        this.onNoteListener = onNoteListener;
    }

    //Creates a connection between the adapter and a new view holder
    @Override
    @NonNull
    public ListMoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_moods, parent, false);
        return new ListMoodViewHolder(view, onNoteListener);
    }

    //Determines what is shown in the object
    @Override
    public void onBindViewHolder(@NonNull ListMoodViewHolder holder, int position) {
        Mood mood = moodList.get(position);
        holder.textViewName.setText(mood.getName());
//        holder.textViewGenre.setText("Genre: " + mood.genre);
//        holder.textViewAge.setText("Age: " + artist.age);
//        holder.textViewCountry.setText("Country: " + artist.country);
    }

    @Override //Helper function
    public int getItemCount() {
        return moodList.size();
    }

    //Class definition of view holder
    class ListMoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        OnNoteListener onNoteListener;

        public ListMoodViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.onNoteListener = onNoteListener;
            textViewName = itemView.findViewById(R.id.textRecyclerName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    //Interface used in ListMoods that lets us interact with the recyclerview objects
    public interface OnNoteListener{
        void onNoteClick(int position);
    }





}
