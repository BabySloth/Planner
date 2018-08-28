package view.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

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
    public ArrayList<Event> getSingleEvents(LocalDate date){
        ArrayList<Event> occurrence = new ArrayList<>();
        events.stream().filter(o1 -> o1.occurs(date) && o1.getDaysLength() == 1).forEach(o1 -> {
            occurrence.add(o1);
            o1.setOrder(-1);  // Reset value
        });
        return occurrence;
    }

    public ArrayList<Event> getMultiEvents(LocalDate date){
        ArrayList<Event> occurrence = new ArrayList<>();
        events.stream().filter(o1 -> o1.occurs(date) && o1.getDaysLength() > 1).forEach(o1 -> {
            occurrence.add(o1);
            o1.setOrder(-1);  // Reset value
        });
        return occurrence;
    }

    /**
     * Gives all the events for the entire calendar
     * @param startingDate First Sunday of the calendar
     * @return TreeMap of events for the calendar
     */
    public TreeMap<Integer, ArrayList<Event>> getMultiEventsMap(LocalDate startingDate){
        // Keys are never equal, so we don't need 0 for comparator
        TreeMap<Integer, ArrayList<Event>> map = new TreeMap<>();
        for(int i = 0; i < 42; i++)
            map.put(i, getMultiEvents(startingDate.plusDays(i)));
        return map;
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
