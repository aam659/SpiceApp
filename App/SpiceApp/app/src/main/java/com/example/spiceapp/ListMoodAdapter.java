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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ListMoodAdapter extends RecyclerView.Adapter<ListMoodAdapter.ListMoodViewHolder> {

    private Context mContext; //Current context
    private List<Mood> moodList; //Our list of moods

    //Constructor of our adapter
    public ListMoodAdapter(Context mContext, List<Mood> moodList){
        this.mContext = mContext;
        this.moodList = moodList;
    }

    //Creates a connection between the adapter and a new view holder
    @Override
    @NonNull
    public ListMoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_moods, parent, false);
        System.out.println(moodList.get(0).Name + " MOOD NAME3");
        return new ListMoodViewHolder(view);
    }

    //Determines what is shown in the object
    @Override
    public void onBindViewHolder(@NonNull ListMoodViewHolder holder, int position) {
        Mood mood = moodList.get(position);
        holder.textViewName.setText(mood.Name);
//        holder.textViewGenre.setText("Genre: " + mood.genre);
//        holder.textViewAge.setText("Age: " + artist.age);
//        holder.textViewCountry.setText("Country: " + artist.country);
    }

    @Override //Helper function
    public int getItemCount() {
        return moodList.size();
    }

    //Class definition of view holder
    class ListMoodViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        public ListMoodViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textRecyclerName);
//            textViewGenre = itemView.findViewById(R.id.text_view_genre);
//            textViewAge = itemView.findViewById(R.id.text_view_age);
//            textViewCountry = itemView.findViewById(R.id.text_view_country);
        }
    }





}
