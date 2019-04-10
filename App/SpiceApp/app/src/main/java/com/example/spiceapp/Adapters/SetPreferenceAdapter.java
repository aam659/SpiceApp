package com.example.spiceapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.Mood;
import com.example.spiceapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SetPreferenceAdapter extends RecyclerView.Adapter<SetPreferenceAdapter.SetPreferenceViewHolder>{

    private Context mContext; //Current context
    private List<Mood> moodList; //Our list of moods
    private SetPreferenceAdapter.OnNoteListener onNoteListener;


    //Every ViewHolder will have an onclick listener


    //Constructor of our adapter
    public SetPreferenceAdapter(Context mContext, List<Mood> moodList, SetPreferenceAdapter.OnNoteListener onNoteListener){
        this.mContext = mContext;
        this.moodList = moodList;
        this.onNoteListener = onNoteListener;
    }

    //Creates a connection between the adapter and a new view holder
    @NonNull
    public SetPreferenceAdapter.SetPreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_pref, parent, false);
        return new SetPreferenceAdapter.SetPreferenceViewHolder(view, onNoteListener);
    }

    //Determines what is shown in the object
    public void onBindViewHolder(@NonNull SetPreferenceAdapter.SetPreferenceViewHolder holder, int position) {
        Mood mood = moodList.get(position);
        holder.textViewName.setText(mood.getName());
//        holder.textViewGenre.setText("Genre: " + mood.genre);
//        holder.textViewAge.setText("Age: " + artist.age);
//        holder.textViewCountry.setText("Country: " + artist.country);
    }

     //Helper function
    public int getItemCount() {
        return moodList.size();
    }

    //Class definition of view holder
    class SetPreferenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        SetPreferenceAdapter.OnNoteListener onNoteListener;

        public SetPreferenceViewHolder(@NonNull View itemView, SetPreferenceAdapter.OnNoteListener onNoteListener) {
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

    //Interface used in SetPreferences that lets us interact with the recyclerview objects
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
