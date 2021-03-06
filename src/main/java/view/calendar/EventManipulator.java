package view.calendar;

import helper.Blank;
import helper.Colors;
import helper.ElementFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.BasicView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class EventManipulator extends VBox implements BasicView {
    private final CalendarView controller;
    private final AllEvents data;
    private CalendarContent calendarContent = null;
    private LocalDate firstSelection = LocalDate.now();
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
        if(calendarContent != null){
            firstSelection = calendarContent.getFirstSelection();
            secondSelection = calendarContent.getSecondSelection();
        }
        events = new ArrayList<>(data.getEvents(firstSelection, secondSelection));
        events.sort(new Sort.OrderNumber());
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
        Label headLabel = ElementFactory.label("Details", Colors.LIGHT_GRAY, width, height, style);
        headLabel.setAlignment(Pos.CENTER);

        // Top most is for the first selection
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");

        String firstText = String.format("First selected: %s", firstSelection.format(dateFormat));
        Color firstColor = now.equals(firstSelection) ? Colors.YELLOW : Colors.LIGHT_GRAY;
        Label firstLabel = ElementFactory.label(firstText, firstColor, width, height, style);
        firstLabel.setOnMouseClicked(e -> {
            setFirstSelection(LocalDate.now());
            calendarContent.setFirstSelection(LocalDate.now());
            setSecondSelection(null);
        });

        // Second is for the second selected day
        String secondText = "None selected";
        Color secondColor = Colors.LIGHT_GRAY;
        if(secondSelection != null){
            secondText = String.format("Second selected: %s", secondSelection.format(dateFormat));
            secondColor = now.equals(secondSelection) ? Colors.YELLOW : secondColor;
        }
        Label secondLabel = ElementFactory.label(secondText, secondColor, width, height, style);
        secondLabel.setOnMouseClicked(e -> {
            setSecondSelection(null);
            calendarContent.setSecondSelection(null);
        });

        // Amount of events that occurs
        int numberEvents = events.size();
        String numberEventsText = numberEvents == 1 ? String.format("There is %d event", numberEvents) :
                String.format("There are %d events", numberEvents);
        Label thirdLabel = ElementFactory.label(numberEventsText, Colors.LIGHT_GRAY, width, height, style);

        // Count down days
        Label fourthLabel = ElementFactory.label(countdownText(), Colors.LIGHT_GRAY, width, height, style);

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
        double total_width = Measurements.total_width;
        String[] styleButton = {"labelFont15", "buttonCalendar"};
        if(events.size() == 0) makingNewEvent = true;
        Event selectedEvent = makingNewEvent ? null : events.get(eventPointer);

        Button decrement = ElementFactory.decrementButton(40, Colors.LIGHT_GRAY, styleButton);
        decrement.setOnAction(e -> {
            if(makingNewEvent){
                makingNewEvent = false;
                return;
            }
            int newValue = eventPointer - 1;
            eventPointer = newValue <= 0 ?  events.size() - 1 : newValue;
            generateView();
        });
        Button increment = ElementFactory.incrementButton(40, Colors.LIGHT_GRAY, styleButton);
        increment.setOnAction(e -> {
            if(makingNewEvent){
                makingNewEvent = false;
                return;
            }
            int newValue = eventPointer + 1;
            eventPointer = newValue > events.size() - 1 ? 0 : newValue;
            generateView();
        });
        Button newEventButton = ElementFactory.button("New Event", makingNewEvent ? Colors.PURPLE : Colors.LIGHT_GRAY, 200, height, styleButton);
        newEventButton.setOnAction(e ->{
            makingNewEvent = true;
            generateView();
        });
        HBox container1 = new HBox(decrement, increment, newEventButton);
        ElementFactory.resize(container1, Measurements.total_width, height);

        Blank blank = new Blank(1, 30);

        String[] styleTextField = {"labelFont18", "text-field"};
        String[] styleTextLabel = {"labelFont18"};

        //Title
        Label titleLabel = ElementFactory.label("Title:", Colors.LIGHT_GRAY, total_width, height, styleTextLabel);
        TextField titleField = ElementFactory.textField(selectedEvent == null ? "" : selectedEvent.getTitle(), "Enter", total_width, height, styleTextField);

        Label descLabel = ElementFactory.label("Description:", Colors.LIGHT_GRAY, total_width, height, styleTextLabel);
        TextArea descArea = ElementFactory.textArea(selectedEvent == null ? "" : selectedEvent.getDescription(), "Enter", total_width, height * 2, "textArea", "labelFont15");

        Label colorLabel = ElementFactory.label("Color:", Colors.LIGHT_GRAY, total_width, height, styleTextLabel);
        ArrayList<String> colorChoices = new ArrayList<>(Arrays.asList("Blue", "Red", "Green"));
        ComboBox<String> colorChoice = ElementFactory.comboBox(total_width, height, colorChoices, "comboBox");

        Blank blank2 = new Blank(1, 30);

        Button save = ElementFactory.button("Save", Colors.LIGHT_GRAY, total_width, height, "buttonCalendar");
        save.setOnAction(e -> {
            String title = titleField.getText();
            if(title.isEmpty()) return;

            String desc = descArea.getText();
            String color = userColorChoice(colorChoice.getSelectionModel().getSelectedItem()).substring(1); // removes #

            if(selectedEvent == null){
                Event newEvent = new Event(data.availableId(), title, desc, firstSelection, secondSelection, color);
                data.addEvent(newEvent);

            }else{
                selectedEvent.setTitle(title);
                selectedEvent.setDescription(desc);
                selectedEvent.setDateStart(firstSelection);
                selectedEvent.setDateEnd(secondSelection);
                selectedEvent.setColor(color);
            }
            eventPointer = 0;
            calendarContent.generateView();
            makingNewEvent = false;
            generateView();
        });
        Button delete = ElementFactory.button("Delete (cmd + click)", Colors.LIGHT_GRAY, total_width, height, "buttonCalendar");
        delete.setOnMouseClicked(e -> {
            if ((e.isShortcutDown() || e.isShiftDown()) && selectedEvent != null){
                data.removeEvent(selectedEvent);
                eventPointer = 0;
                calendarContent.generateView();
                generateView();
            }
        });

        return new Node[]{blank, container1, titleLabel, titleField, descLabel, descArea, colorLabel, colorChoice, blank2, save, delete};
    }

    private String userColorChoice(String colorString){
        switch(colorString){
            case "Blue":
                return Colors.toHex(Colors.BLUE);
            case "Red":
                return Colors.toHex(Colors.LIGHT_RED);
            case "Green":
                return Colors.toHex(Colors.LIGHT_GREEN);
        }
        return Colors.toHex(Colors.BLUE);
    }

    private class Measurements{
        final static double total_width = 370;

        final static double node_height = 40;
        final static double node_title_width = 55;
    }

    // -------------------
    // Getters and setters
    // -------------------

    void setCalendarContent(CalendarContent calendarContent){
        this.calendarContent = calendarContent;
    }

    void setFirstSelection(LocalDate firstSelection) {
        this.firstSelection = firstSelection;
        eventPointer = 0;
        makingNewEvent = false;
        generateView();
    }

    void setSecondSelection(LocalDate secondSelection) {
        this.secondSelection = secondSelection;
        eventPointer = 0;
        generateView();
    }
}