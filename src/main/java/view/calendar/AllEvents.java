package view.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class AllEvents {
    private final ArrayList<Event> events = new ArrayList<>();

    /**
     * Adds an event to events in numerical order
     * @param event event to add
     */
    public void addEvent(Event event){
        int index = events.size() - 1;
        if(events.contains(event)) return;
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
     * Gives all the events that occurs on the given date
     * @param date Events based on this date
     * @return List of all the events
     */
    public ArrayList<Event> getEvents(LocalDate date){
        ArrayList<Event> occurrence = new ArrayList<>();
        events.stream().filter(o1 -> o1.occurs(date)).forEach(occurrence::add);
        return occurrence;
    }

    /**
     * Gives all the events that occurs on the given range
     * @param date1 Starting date (Will check and fix against date2) cannot be null
     * @param date2 Ending date (Will check and fix against date1) can be null
     * @return Set of all events that occurs in the given range
     */
    public HashSet<Event> getEvents(LocalDate date1, LocalDate date2){
        // Makes sure no value is null
        if(date2 == null){
            return new HashSet<>(getEvents(date1));
        }
        // Makes sure that date1 is always before date2
        if(date2.isBefore(date1)){
            return getEvents(date2, date1);
        }
        HashSet<Event> occurrence = new HashSet<>();
        while(date1.isBefore(date2.plusDays(1))){
            occurrence.addAll(getEvents(date1));
            date1 = date1.plusDays(1);
        }
        return occurrence;
    }

    /**
     * Gives all events that occur on the given time only (one day events).
     * @param date Date in which the events occurs
     * @return Events
     */
    public ArrayList<Event> getSingleEvents(LocalDate date){
        ArrayList<Event> occurrence = new ArrayList<>();
        getEvents(date).stream().filter(o1 -> o1.occurs(date) && o1.getDaysLength() == 1).forEach(o1 -> {
            occurrence.add(o1);
            o1.setOrder(-1);
        });
        return occurrence;
    }

    /**
     * Gives all events that occur on the given time (event can start another day).
     * @param date Date in which the event occurs
     * @return Events
     */
    public ArrayList<Event> getMultiEvents(LocalDate date){
        ArrayList<Event> occurrence = new ArrayList<>();
        getEvents(date).stream().filter(o1 -> o1.occurs(date) && o1.getDaysLength() > 1).forEach(o1 -> {
            occurrence.add(o1);
            o1.setOrder(-1);
        });
        occurrence.sort(new Sort.LongestFirst());
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
