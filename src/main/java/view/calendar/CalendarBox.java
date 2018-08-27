package view.calendar;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarBox extends VBox {
    private final LocalDate date;
    private final int position;
    private final LocalDate firstDate;
    private int longOrder = 0;
    private final ArrayList<Event> longEvents = new ArrayList<>();

    public CalendarBox(LocalDate date, int position, LocalDate firstDate){
        this.date = date;
        this.position = position;
        this.firstDate = firstDate;
        this.setMinWidth(130);
        this.setPrefWidth(130);
        this.setMaxWidth(130);
        setAlignment(Pos.TOP_LEFT);

        setOnMouseClicked(e -> System.out.println(position));
    }

    public void addShortEvent(int maxShortOrder, ArrayList<Event> events){
        int shortOrder = 0;
        for(Event event : events){
            getChildren().add(new EventDisplay(event, true, false, 1));
            shortOrder++;
        }
        while(shortOrder != maxShortOrder){
            getChildren().add(new Blank(1, 20));
            shortOrder++;
        }
    }

    /**
     * Tries to add event
     * @param order What order to add to
     * @param event Event to add
     */
    public void addLongEvent(int order, Event event){
        LocalDate eventStart = event.getDateStart();

        boolean needsTitle = date.getDayOfWeek() == DayOfWeek.SUNDAY || eventStart.equals(date);
        boolean isContinuation = eventStart.isAfter(firstDate.minusDays(1)) && eventStart.isBefore(date);
        int cellWraps = 0;

        LocalDate temp = eventStart;
        do{
            temp = temp.plusDays(1);
            cellWraps++;
        }while(temp.getDayOfWeek() != DayOfWeek.SUNDAY && event.occurs(temp));

        if(needsTitle){
            System.out.println(cellWraps);
            setStyle("-fx-background-color: yellow;");
        }

        // Fill gaps with empty spaces
        while(order != longOrder){
            getChildren().add(new Blank(1, 20));
            longOrder++;
        }
        getChildren().add(new EventDisplay(event, needsTitle, isContinuation, cellWraps));
        longEvents.add(event);

        longOrder++;
    }

    private class Measurements{
        static final double width = 130;
    }
}
