package view.calendar;

import helper.Blank;
import helper.Colors;
import helper.ElementGenerator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.BasicView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class EventManipulator extends VBox implements BasicView {
    private final CalendarView controller;
    private final AllEvents data;
    private CalendarContent calendarContent;
    private LocalDate firstSelection = LocalDate.now();  // Never null
    private LocalDate secondSelection = null;  // Can be null
    private ArrayList<Event> events;

    EventManipulator(CalendarView controller, AllEvents data){
        this.controller = controller;
        this.data = data;
        setMainDesign();
        generateView();
    }

    @Override
    public void setMainDesign() {

    }

    @Override
    public void generateView() {
        getChildren().clear();
        events = new ArrayList<>(data.getEvents(firstSelection, secondSelection));
        events.sort(new Sort.ChronoOrder());
        getChildren().addAll(getTopBasicInformation());
        getChildren().addAll(getEventView());
    }

    // ---------------------
    // Top basic information
    // ---------------------

    private Node[] getTopBasicInformation(){
        double width = Measurements.total_width;
        double height = Measurements.node_height;
        LocalDate now = LocalDate.now();
        String[] style = {"labelFont18"};

        // Label for the entire right side
        Label headLabel = ElementGenerator.label("Details", Colors.LIGHT_GRAY, width, height, style);
        headLabel.setAlignment(Pos.CENTER);

        // Top most is for the first selection
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");

        String firstText = String.format("First selected: %s", firstSelection.format(dateFormat));
        Color firstColor = now.equals(firstSelection) ? Colors.YELLOW : Colors.LIGHT_GRAY;
        Label firstLabel = ElementGenerator.label(firstText, firstColor, width, height, style);
        firstLabel.setOnMouseClicked(e -> {
            setFirstSelection(LocalDate.now());
            calendarContent.setFirstSelection(LocalDate.now());
            setSecondSelection(null);
        });

        // Second is for the second selected day
        String secondText = "None selected";
        Color secondColor = Colors.LIGHT_GRAY;
        if(secondSelection != null){
            secondText = String.format(" Second selected: %s", secondSelection.format(dateFormat));
            secondColor = now.equals(secondSelection) ? Colors.YELLOW : secondColor;
        }
        Label secondLabel = ElementGenerator.label(secondText, secondColor, width, height, style);
        secondLabel.setOnMouseClicked(e -> {
            setSecondSelection(null);
            calendarContent.setSecondSelection(null);
        });

        // Amount of events that occurs
        int numberEvents = events.size();
        String numberEventsText = numberEvents == 1 ? String.format("There is %d event", numberEvents) :
                String.format("There are %d events", numberEvents);
        Label thirdLabel = ElementGenerator.label(numberEventsText, Colors.LIGHT_GRAY, width, height, style);

        // Count down days
        Label fourthLabel = ElementGenerator.label(countdownText(), Colors.LIGHT_GRAY, width, height, style);

        return new Node[]{headLabel, firstLabel, secondLabel, thirdLabel, fourthLabel};
    }

    private String countdownText(){
        if(secondSelection == null){
            return "0 days until";
        }else{
            if(firstSelection.isBefore(secondSelection)){
                return String.format("%d more day(s) (excluding first date)", firstSelection.until(secondSelection, ChronoUnit.DAYS));
            }else{
                return String.format("%d day(s) past (excluding first date)", secondSelection.until(firstSelection, ChronoUnit.DAYS));
            }
        }
    }

    // ----------------------
    // Event view and changer
    // ----------------------

    private int eventPointer = 0;
    private boolean makingNewEvent = false;

    private Node[] getEventView(){
        double height = Measurements.node_height;
        String[] styleButton = {"labelFont15", "buttonCalendar"};

        Button decrement = ElementGenerator.decrementButton(40, Colors.LIGHT_GRAY, styleButton);
        decrement.setOnAction(e -> {
            if(makingNewEvent){
                makingNewEvent = false;
                return;
            }
            int newValue = eventPointer - 1;
            eventPointer = newValue >= 0 ? newValue : 0;
            generateView();
        });
        Button increment = ElementGenerator.incrementButton(40, Colors.LIGHT_GRAY, styleButton);
        increment.setOnAction(e -> {
            if(makingNewEvent){
                makingNewEvent = false;
                return;
            }
            int newValue = eventPointer + 1;
            eventPointer = newValue > events.size() - 1 ? eventPointer : newValue;
            generateView();
        });
        Button newEventButton = ElementGenerator.button("New Event", makingNewEvent ? Colors.PURPLE : Colors.LIGHT_GRAY, 200, height, styleButton);
        newEventButton.setOnAction(e ->{
            makingNewEvent = true;
            generateView();
        });
        HBox container1 = new HBox(decrement, increment, newEventButton);
        ElementGenerator.resize(container1, Measurements.total_width, height);

        Blank blank = new Blank(1, 30);
        Event selectedEvent = makingNewEvent ? null : events.get(eventPointer);
        Color color = Colors.LIGHT_GRAY;
        String[] styleText = {"labelFont18", "text-field"};

        TextField title = ElementGenerator.textField(selectedEvent == null ? "" : selectedEvent.getTitle(), "Title", color, 0, height, styleText);

        return new Node[]{blank, container1, title};
    }

    private class Measurements{
        final static double total_width = 370;
        final static double total_height = 800;

        final static double node_height = 40;
    }

    // -------------------
    // Getters and setters
    // -------------------

    void setCalendarContent(CalendarContent calendarContent){
        this.calendarContent = calendarContent;
    }

    void setFirstSelection(LocalDate firstSelection) {
        this.firstSelection = firstSelection;
        generateView();
    }

    void setSecondSelection(LocalDate secondSelection) {
        if(!firstSelection.equals(secondSelection)){
            this.secondSelection = secondSelection;
        }
        generateView();
    }
}