package view.calendar;

import java.util.Comparator;

/**
 * Sorts {@link view.calendar.Event} by duration of days with longest days first.
 */
public class SortLength implements Comparator<Event>{
    @Override
    public int compare(Event o1, Event o2) {
        // -1 to reverse
        return -1 * Integer.compare(o1.getDaysLength(), o2.getDaysLength());
    }
}
