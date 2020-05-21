package com.pendulum.game.state;

import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

public class EventHandler {

    private ArrayList<StateEvent> events;

    public EventHandler() {
        events = new ArrayList<>();
    }

    public void addEvent(StateEvent event) {
        events.add(event);
    }

    public void update() {
        for(int i=0; i<events.size(); i++) {
            events.get(i).triggerEvent();
        }

        events.clear();
    }

}
