package com.example.spiceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiceapp.EventDisplay;
import com.example.spiceapp.FirebaseObjects.Event;
import com.example.spiceapp.FirebaseObjects.User;
import com.example.spiceapp.MessageActivity;
import com.example.spiceapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private Context mContext;
    private List<Event> mEvents;

    public EventsAdapter(Context mContext, List<Event> mEvents){
        this.mContext = mContext;
        this.mEvents = mEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent,false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.eventName.setText(event.getEventName());

        //TODO: Somehow add profile picture

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "EVENTNAME", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EventDisplay.class);
                intent.putExtra("eventName", event.getEventName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView eventName;

        public ViewHolder(View itemView){
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
        }


    }
}
