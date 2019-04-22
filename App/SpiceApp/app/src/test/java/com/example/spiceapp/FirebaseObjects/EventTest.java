package com.example.spiceapp.FirebaseObjects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EventTest {
    private Event sampleEvent;
    private String sampleEventName;
    private List<String> users;
    private List<String> attendees;

    @Before
    public void before() {
        sampleEventName = "Dinner Party";
        users = new ArrayList<>();
        attendees = new ArrayList<>();
        users.add("Michael Scott");
        users.add("Jan Levinson");
        users.add("Jim Halpert");
        users.add("Pam Beasley");
        sampleEvent = new Event(sampleEventName, users);
        attendees.add("Dwight K. Schrute");
        attendees.add("Angela Martin");
        String expectedEventName = "Dinner Party Part 2";
        Event expectedEvent = new Event(expectedEventName, attendees);
    }

    @Test
    public void getUsers() { assert(sampleEvent).getUsers().equals(users); }

    @Test
    public void setUsers() {
        List<String> people = new ArrayList<>();

        for (String name : users) {
            people.add(name);
        }

        for (String name : attendees) {
            people.add(name);
        }

        sampleEvent.setUsers(people);

        assert(sampleEvent.getUsers().equals(people));
    }

    @Test
    public void getEventName() { assert(sampleEvent).getEventName().equals(sampleEventName); }

    @Test
    public void setEventName() {
        sampleEvent.setEventName("New Awkward Dinner Party");

        assert(sampleEvent.getEventName().equals("New Awkward Dinner Party"));
    }
}