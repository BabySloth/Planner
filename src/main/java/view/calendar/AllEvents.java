package view.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

public class AllEvents {
    private final ArrayList<Event> events = new ArrayList<>();

    /**
     * Adds an event to events in numerical order
     * @param event event to add
     */
    public void addEvent(Event event){
        int index = events.size() - 1;
        if(index == -1) {
            events.add(event);
            return;
        }

        while(toInt(event.getId()) < toInt(events.get(index).getId())){
            index--;
        }
        events.add(index + 1, event);
    }

    /**
     * Removes an event
     */
    public void removeEvent(Event event){
        events.remove(event);
    }

    /**
     * Gives all events that occurs on the given time.
     * @param date Date in which the events occur
     * @return Multiple events in id order
     */
    public ArrayList<Event> getEventForDay(LocalDate date){
        ArrayList<Event> occurrence = new ArrayList<>();
        for(Event event : events){
            if(event.occurs(date)){
                occurrence.add(event);
            }
        }
        // Longest events on top of shorter ones
        occurrence.sort((o1, o2) -> -1 * Integer.compare(o1.getDaysLength(), o2.getDaysLength()));
        return occurrence;
    }

    /**
     * Smallest integer or 0 available without repeating
     * @return Id for the newly created event
     */
    public String availableId(){
        int i = 0;
        for(Event event : events){
            if(i != toInt(event.getId())){
                return String.valueOf(i);
            }
            i++;
        }
        return String.valueOf(events.size());
    }

    /**
     * Gets an array for all the events. Primarily used for writing to disk {@link view.calendar.DiskData};
     * @return An array list events
     */
    public Event[] getAllEvents(){
        return events.toArray(new Event[0]);
    }


    private int toInt(String string){
        return Integer.parseInt(string);
    }
}
