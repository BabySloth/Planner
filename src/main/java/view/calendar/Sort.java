package view.calendar;

import java.time.LocalDate;
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

    public static class ChronoOrder implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2){
            LocalDate d1 = o1.getDateStart();
            LocalDate d2 = o2.getDateStart();

            if(d1.isAfter(d2)){
                return 1;
            }else if(d1.isEqual(d2)){
                return 0;
            }else{
                return -1;
            }
        }
    }
}
