package view.calendar;

import java.util.Comparator;

/**
 * Sorts {@link view.calendar.Event} by duration of days with shortest days first.
 */
public class Sort {
    public static class ShortestFirst implements Comparator<Event>{
        @Override
        public int compare(Event o1, Event o2) {
            return Integer.compare(o1.getDaysLength(), o2.getDaysLength());
        }
    }

    public static class LongestFirst implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2) {
            return -1 * Integer.compare(o1.getDaysLength(), o2.getDaysLength());
        }
    }
}
