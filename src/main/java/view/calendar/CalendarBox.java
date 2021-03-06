package view.calendar;

import helper.Blank;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarBox extends VBox {
    private final CalendarContent parent;
    private final LocalDate date;
    private final int position;
    private final LocalDate firstDate;
    private int longOrder = 0;
    private final ArrayList<Event> longEvents = new ArrayList<>();

    CalendarBox(CalendarContent parent, LocalDate date, int position, LocalDate firstDate){
        this.parent = parent;
        this.date = date;
        this.position = position;
        this.firstDate = firstDate;
        this.setMinWidth(130);
        this.setPrefWidth(130);
        this.setMaxWidth(130);
        setAlignment(Pos.TOP_LEFT);

        setOnMouseClicked(this::clickedOn);
    }

    private void clickedOn(MouseEvent e){
        if(e.isShortcutDown() || e.isShiftDown() || e.getButton() == MouseButton.SECONDARY) {
            parent.setSecondSelection(date);
        } else if (e.getButton() == MouseButton.PRIMARY){
            parent.setFirstSelection(date);
        }
        parent.getEventManipulator().generateView();
    }

    public void addShortEvent(ArrayList<Event> events){
        ObservableList<Node> children = getChildren();

        mainLoop: for(Event event : events){
            EventDisplay display = new EventDisplay(event, true, false, 1);

            for(int i = 0; i < children.size(); i++){
                Node node = children.get(i);
                if(node instanceof Blank){
                    children.remove(node);
                    children.add(i, display);
                    event.setOrder(i);
                    continue mainLoop;
                }
            }

            children.add(display);
            event.setOrder(longOrder);
            longOrder++;
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

        LocalDate temp = date;
        do{
            temp = temp.plusDays(1);
            cellWraps++;
        }while(temp.getDayOfWeek() != DayOfWeek.SUNDAY && event.occurs(temp));

        // Fill gaps with empty spaces
        while(order != longOrder){
            getChildren().add(new Blank(1, 20));
            longOrder++;
        }
        getChildren().add(new EventDisplay(event, needsTitle, isContinuation, cellWraps));
        longEvents.add(event);

        longOrder++;
    }

    public LocalDate getDate(){
        return date;
    }

    private class Measurements{
        static final double width = 130;
    }
}
