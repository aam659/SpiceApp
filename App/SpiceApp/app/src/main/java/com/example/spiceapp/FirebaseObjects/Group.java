package com.example.spiceapp.FirebaseObjects;

import java.util.HashMap;
import java.util.List;

public class Group {

    private String groupName;
    private List<String> Users;
    private HashMap<String, Chat> Chats;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getUsers() {
        return Users;
    }

    public void setUsers(List<String> users) {
        Users = users;
    }


    public Group(){

    }

    public Group(String groupName, List<String> users, List<Chat> chats) {
        this.groupName = groupName;
        Users = users;
    }
}
