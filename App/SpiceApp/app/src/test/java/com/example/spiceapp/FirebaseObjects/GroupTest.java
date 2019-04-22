package com.example.spiceapp.FirebaseObjects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GroupTest {
    private Group sampleGroup;
    private String sampleGroupName;
    private List<String> sampleUsers;
    private HashMap<String, Chat> chats;

    @Before
    public void before() {
        sampleGroupName = "Spice Team";
        sampleUsers = new ArrayList<>();
        sampleUsers.add("Alan");
        sampleUsers.add("Don");
        sampleUsers.add("Logan");
        sampleUsers.add("Ryan");
        sampleGroup = new Group(sampleGroupName, sampleUsers, chats);
    }

    @Test
    public void getGroupName() { assert(sampleGroup.getGroupName().equals(sampleGroupName));}

    @Test
    public void setGroupName() {
        sampleGroup.setGroupName("Spicy");

        assert(sampleGroup.getGroupName().equals("Spicy"));
    }
}