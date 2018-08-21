import org.junit.Test;
import view.calendar.AllEvents;
import view.calendar.Event;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalendarTest {
    @Test
    public void eventTesting(){
        LocalDate now = LocalDate.now();

        String id = "1";
        String title = "Testing 0";
        String description = "Description 0";
        String date = String.format("%d.%d.%d.%d", now.getYear(), now.getDayOfYear(), now.getYear(), now.getDayOfYear());
        String time = "";
        String color = "ffffff";

        Event event = new Event(id, title, description, date, time, color);
        assertTrue(event.occurs(now));
        assertFalse(event.occurs(now.plusDays(1)));

        LocalDate date1 = LocalDate.of(2018, 8, 19);
        LocalDate date1End = LocalDate.of(2018, 8, 25);
        String id2 = "2";
        String title2 = "Testing 1";
        String description2 = "Description 1";
        String date2 = String.format("%d.%d.%d.%d", date1.getYear(), date1.getDayOfYear(), date1End.getYear(), date1End.getDayOfYear());
        String time2 = "";
        String color2 = "ffffff";

        Event event2 = new Event(id2, title2, description2, date2, time2, color2);
        assertTrue(event2.occurs(date1));
        assertTrue(event2.occurs(date1End));
        assertTrue(event2.occurs(date1.plusDays(4)));
        assertFalse(event2.occurs(LocalDate.of(2018, 8, 5)));

        AllEvents data = new AllEvents();
        data.addEvent(event);
        data.addEvent(event2);

        assertEquals(2, data.getAllEvents().length);
    }
}
