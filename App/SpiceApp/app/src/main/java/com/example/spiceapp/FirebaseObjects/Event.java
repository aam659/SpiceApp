package com.example.spiceapp.FirebaseObjects;

import java.util.List;

public class Event {

    private String eventName;
    private List<String> users;


    public Event(String eventName, List<String> users) {
        this.eventName = eventName;
        this.users = users;
    }

    public Event(){

    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
