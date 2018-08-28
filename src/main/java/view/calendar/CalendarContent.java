package view.calendar;

import helper.Blank;
import helper.Colors;
import helper.Measurement;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.BasicView;
import helper.ElementGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

class CalendarContent extends VBox implements BasicView {
    private LocalDate firstDate;
    private LocalDate displayDate;  // One week ahead of first date
    final private CalendarView headController;
    final private AllEvents data;
    private ArrayList<CalendarBox> calendarBoxes;
    private Type showing = Type.CALENDAR;

    CalendarContent(CalendarView headController, AllEvents data){
        this.headController = headController;
        this.data = data;
        firstDate = firstSunday(LocalDate.now());
        displayDate = firstDate.plusWeeks(1);

        setMainDesign();
        generateView();
    }

    @Override
    public void setMainDesign() {

    }

    @Override
    public void generateView() {
        // Reset
        getChildren().clear();
        calendarBoxes = new ArrayList<>(42);
        displayDate = firstDate.plusWeeks(1);

        getChildren().add(generateManipulatorControls());
        switch(showing){
            case CALENDAR:
                getChildren().addAll(generateDaysOfWeekDisplay(), generateCalendarDisplay());
                break;
            case YEAR:
                getChildren().add(generateYearChanger());
                break;
            case MONTH:
                getChildren().add(generateMonthChanger());
                break;
        }
    }

    // ----------------
    // View manipulator
    // ----------------

    /**
     * Changes what is being shown
     * @return First view section (of three)
     */
    private HBox generateManipulatorControls(){
        double width = Measurements.manipulatorWidth;
        double height = Measurements.manipulatorHeight;
        String[] style = {"labelFont15", "buttonCalendar"};
        LocalDate now = LocalDate.now();

        int yearNow = now.getYear();
        int yearDecrementNumber = displayDate.getYear() - 1;
        Button yearDecrement = generateDecrementButton(yearDecrementNumber == yearNow ? Colors.YELLOW : Colors.LIGHT_GRAY, style);
        yearDecrement.setOnAction(e -> changeYear(yearDecrementNumber));
        Button year = ElementGenerator.button(String.valueOf(displayDate.getYear()), sameYear(displayDate) ? Colors.YELLOW : Colors.LIGHT_GRAY,
                                              width, height, style);
        year.setOnAction(e -> {
            showing = Type.YEAR;
            generateView();
        });
        int yearIncrementNumber = displayDate.getYear() + 1;
        Button yearIncrement = generateIncrementButton(yearIncrementNumber == now.getYear() ? Colors.YELLOW : Colors.LIGHT_GRAY, style);
        yearIncrement.setOnAction(e -> changeYear(yearIncrementNumber));

        LocalDate previousMonth = displayDate.minusMonths(1);
        Button monthDecrement = generateDecrementButton(sameYearMonth(previousMonth) ? Colors.YELLOW : Colors.LIGHT_GRAY, style);
        monthDecrement.setOnAction(e -> changeFirstDate(previousMonth));
        Button month = ElementGenerator.button(capitalize(displayDate.getMonth().toString()), sameYearMonth(displayDate) ? Colors.YELLOW : Colors.LIGHT_GRAY,
                                               width, height, style);
        month.setOnAction(e -> {
            showing = Type.MONTH;
            generateView();
        });
        LocalDate nextMonth = displayDate.plusMonths(1);
        Button monthIncrement = generateIncrementButton(sameYearMonth(nextMonth) ? Colors.YELLOW : Colors.LIGHT_GRAY, style);
        monthIncrement.setOnAction(e -> changeFirstDate(nextMonth));
        Rectangle empty = new Blank(Measurements.manipulatorBlank, 1);

        // Change to today
        Button today = ElementGenerator.button("Today", Colors.YELLOW, width, height, style);
        today.setOnAction(e -> changeFirstDate(now));

        // Use firstDate because you change week depending on the first day of the month. displayingDate is always
        // one week after firstDate.
        LocalDate previousWeekDate = firstDate.minusWeeks(1);
        Button previousWeek = generateDecrementButton(sameWeekOfYear(previousWeekDate) ? Colors.YELLOW : Colors.LIGHT_GRAY, style);
        previousWeek.setOnAction(e -> changeFirstDate(previousWeekDate));
        LocalDate nextWeekDate = firstDate.plusWeeks(1);
        Button nextWeek = generateIncrementButton(sameWeekOfYear(nextWeekDate) ? Colors.YELLOW : Colors.LIGHT_GRAY, style);
        nextWeek.setOnAction(e -> changeFirstDate(nextWeekDate));

        return (HBox) ElementGenerator.pane(new HBox(yearDecrement, year, yearIncrement, monthDecrement, month, monthIncrement, today,
                                                     empty, previousWeek, nextWeek), Measurements.width, height);
    }

    /**
     * Creates the view to showcase the 12 months to choose from
     * @return view
     */
    private GridPane generateMonthChanger(){
        double columnWidth = Measurements.width / 4;
        double rowHeight = Measurements.height / 3;
        GridPane container = ElementGenerator.gridPane(3, 4, rowHeight, columnWidth);

        int counter = 1;
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 4; column++){
                int monthNumber = counter;
                boolean isCurrentMonth = monthNumber == LocalDate.now().getMonthValue();
                String text = capitalize(Month.of(monthNumber).toString());
                Color color = isCurrentMonth ? Colors.YELLOW : Colors.LIGHT_GRAY;
                Button button = ElementGenerator.button(text, color, columnWidth, rowHeight, "buttonCalendar", "labelFont30");
                button.setOnAction(e -> changeMonth(monthNumber));
                container.add(button, column, row);
                counter++;
            }
        }
        return container;
    }

    /**
     * Change calendar view to show the given month number
     * Value of 0 and 13 are from {@link #generateDecrementButton(Color, String...)} and
     * {@link #generateIncrementButton(Color, String...)} respectively.
     * @param monthNumber What month to change to (Only values 0 to 13 inclusive)
     */
    private void changeMonth(int monthNumber){
        if(monthNumber == 13){ // Increment
            changeFirstDate(firstDate.plusMonths(1));
        }else if(monthNumber < 1){ // Decrement
            changeFirstDate(firstDate = firstDate.minusMonths(1));
        }else{
            changeFirstDate(firstDate = LocalDate.of(firstDate.getYear(), monthNumber, 1));
        }
    }

    /**
     * Creates a view to change the year
     * @return New view
     */
    private VBox generateYearChanger(){
        LocalDate now = LocalDate.now();
        int firstYearChoice = now.minusYears(2).getYear();

        // Quick change month
        HBox yearContainer = (HBox) ElementGenerator.pane(new HBox(), Measurements.width, Measurements.cellHeight);
        for(int additionalYear = 0; additionalYear < 7; additionalYear++){
            int year = firstYearChoice + additionalYear;
            String text = " " + String.valueOf(year);
            Color color = year == now.getYear() ? Colors.YELLOW : Colors.LIGHT_GRAY;

            Button button = ElementGenerator.button(text, color, Measurements.cellWidth, Measurements.cellHeight, "buttonCalendar", "labelFont30");
            button.setOnAction(e -> changeYear(year));

            yearContainer.getChildren().add(button);
        }

        // Increment decrement changer


        return (VBox) ElementGenerator.pane(new VBox(yearContainer), Measurements.width, Measurements.cellHeight * 2);
    }

    /**
     * Change the calendar view to the given year maintaining the day of year if possible
     * @param year
     */
    private void changeYear(int year){
        // Tries to maintain the first sunday displayed, but in every year, the date changes day of week
        changeFirstDate(LocalDate.ofYearDay(year, firstDate.getDayOfYear()));
    }

    /**
     * If you want to change the first date of the year, call this function
     */
    private void changeFirstDate(LocalDate newDate){
        firstDate = firstSunday(newDate);
        showing = Type.CALENDAR;
        generateView();
    }

    /**
     * Generates a button to decrement something
     * @param color Color of text
     * @param style Styleclass to add
     * @return New button
     */
    private Button generateDecrementButton(Color color, String... style) {
        return ElementGenerator.button("<", color, Measurements.manipulatorWeekWidthHeight,
                                       Measurements.manipulatorWeekWidthHeight, style);
    }

    /**
     * Generates a button to increment something
     * @param color Color of text
     * @param style Styleclass to add
     * @return New button
     */
    private Button generateIncrementButton(Color color, String... style){
        return ElementGenerator.button(">", color, Measurements.manipulatorWeekWidthHeight,
                                       Measurements.manipulatorWeekWidthHeight, style);
    }

    // ----------------
    // Calendar contents
    // ----------------

    /**
     * Shows what day of the week each column represents. Highlights in yellow if the calendar is showing the current
     * day and corresponds to the day of week display.
     * @return Second view section (of three)
     */
    private HBox generateDaysOfWeekDisplay(){
        HBox container = (HBox) ElementGenerator.pane(new HBox(), Measurements.width, Measurements.daysOfWeekHeight);

        double width = Measurements.daysOfWeekWidth;
        double height = Measurements.daysOfWeekHeight;
        final String[] daysOfWeekNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for(String name : daysOfWeekNames){
            // Leading space to prevent jumbled text
            Label label = ElementGenerator.label(" " + name, sameYearMonthDayWeek(name) ? Colors.YELLOW : Colors.LIGHT_GRAY,
                                        width, height, "labelFont15");
            container.getChildren().add(label);
        }

        return container;
    }

    /**
     * Generates the main body of the calendar
     * @return Main view
     */
    private VBox generateCalendarDisplay(){
        double width = Measurements.width;
        double height = Measurements.height;
        VBox container = (VBox) ElementGenerator.pane(new VBox(), width, height);

        for(int i = 0; i < 6; i++){  // 6 rows
            LocalDate startingDate = firstDate.plusWeeks(i);
            container.getChildren().addAll(generateDayOfMonthDisplay(startingDate),
                                           generateDateEventDisplay());
        }

        // Set actual content to calendarBoxes
        setCalendarBoxesContent();
        return container;
    }

    /**
     * Gives the day of month for the week (Ex. 1 - December
     * @param startingDate Date of the first Sunday of the week
     * @return Container of labels
     */
    private HBox generateDayOfMonthDisplay(LocalDate startingDate){
        LocalDate date = startingDate;
        HBox container = (HBox) ElementGenerator.pane(new HBox(), Measurements.width, Measurements.eventHeight);

        for(int i = 0; i < 7; i++){
            int dayOfMonth = date.getDayOfMonth();
            boolean hasMonthText = dayOfMonth == 1 || date.equals(firstDate);
            String monthText = hasMonthText ? " - " + capitalize(date.getMonth().toString()) : "";
            String dateString = String.format("  %d%s", dayOfMonth, monthText); // Leading space to prevent jumbled text
            Color color = date.equals(LocalDate.now()) ? Colors.YELLOW : Colors.LIGHT_GRAY;
            container.getChildren().add(
                    ElementGenerator.label(dateString, color, Measurements.eventWidth, Measurements.eventHeight, "labelFont15"));

            date = date.plusDays(1);
        }

        return container;
    }

    /**
     * Backbone of calendarBoxes. Doesn't actually show anything until it calls {@link #setCalendarBoxesContent()}
     * @return Container
     */
    private ScrollPane generateDateEventDisplay() {
        HBox mainContainer = (HBox) ElementGenerator.pane(new HBox(), Measurements.width, Measurements.eventTotalHeight);

        for(int i = 0; i < 7; i++){
            int position = calendarBoxes.size();
            CalendarBox box = new CalendarBox(firstDate.plusDays(position), position, firstDate);
            calendarBoxes.add(box);
            mainContainer.getChildren().add(box);
        }

        // Container and styling
        ScrollPane overhead = new ScrollPane(mainContainer);
        overhead.setMinSize(Measurements.width, Measurements.eventTotalHeight);
        overhead.setPrefSize(Measurements.width, Measurements.eventTotalHeight);
        overhead.setMaxSize(Measurements.width, Measurements.eventTotalHeight);
        overhead.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        overhead.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        overhead.getStyleClass().add("scrollPane");

        return overhead;
    }

    /**
     * Adds content to the CalendarBoxes
     */
    private void setCalendarBoxesContent(){
        // First add all the long events
        TreeMap<Integer, ArrayList<Event>> longEvents = data.getMultiEventsMap(firstDate);
        int maxLongOrder = maxLongOrder(longEvents);

        for(int order = 0; order < maxLongOrder; order++){
            int mapKey = 0;
            for(CalendarBox box : calendarBoxes){
                if(longEvents.get(mapKey).size() == 0) {
                    mapKey++;
                    continue;
                }
                ArrayList<Event> events = longEvents.get(mapKey);
                Event event = events.get(0);

                if(event.getOrder() == -1){
                    if(firstOccurrence(event, longEvents, mapKey)){
                        event.setOrder(order);
                    }else if(events.size() > 1){
                        int position = mapKey;
                        Optional<Event> possibleEvent = events.stream().filter(o1 -> firstOccurrence(o1, longEvents, position)).findFirst();
                        if(possibleEvent.isPresent()){
                            event = possibleEvent.get();
                            event.setOrder(order);
                        }else{
                            mapKey++;
                            continue;
                        }
                    }else{
                        mapKey++;
                        continue;
                    }
                }
                box.addLongEvent(event.getOrder(), event);
                longEvents.get(mapKey).remove(event);  // Remove event from list of events to add

                mapKey++;
            }
        }

        // Add short events
        calendarBoxes.forEach(o1 -> o1.addShortEvent(data.getSingleEvents(o1.getDate())));
    }

    /**
     * Determines if the event starts at an earlier date in the calendar display
     * @param event What event to check for
     * @param events What events are shown
     * @param position What position in the calendar
     * @return If the event first occurrence is at the given position
     */
    private boolean firstOccurrence(Event event, TreeMap<Integer, ArrayList<Event>> events, int position){
        for(int i = 0; i < position; i++){
            if(events.get(i).contains(event)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gives the maximum amount of long events in a day given a map.
     * @param events Map linking position and amount of long days of each position
     * @return The amount of times each {@link view.calendar.CalendarBox} should have long events added
     */
    private int maxLongOrder(TreeMap<Integer, ArrayList<Event>> events){
        int max = 0;
        for(ArrayList<Event> eventList : events.values())
            max = eventList.size() > max ? eventList.size() : max;
        return max;
    }

    // Helper functions

    /**
     * Returns the most recent past Sunday; can be itself.
     * @param date The date that needs the most recent past Sunday.
     * @return The most recent past Sunday.
     */
    private LocalDate firstSunday(LocalDate date){
        while(date.getDayOfWeek() != DayOfWeek.SUNDAY)
            date = date.minusDays(1);
        return date;
    }

    /**
     * If the date and today's date is the same year.
     * @param date Comparison date
     * @return Same year or not
     */
    private boolean sameYear(LocalDate date){
        LocalDate now = LocalDate.now();
        return date.getYear() == now.getYear();
    }

    /**
     * If today's date and given date occurs on the same month and year (see {@link #sameYear(LocalDate)}
     * @param date Comparison date
     * @return Same year and month or not
     */
    private boolean sameYearMonth(LocalDate date){
        LocalDate now = LocalDate.now();
        return sameYear(date) && date.getMonthValue() == now.getMonthValue();
    }

    /**
     * Checks If the current day is part of the week of the given date
     * @param date First date of the week
     * @return If the current day is part of the week of the given date
     */
    private boolean sameWeekOfYear(LocalDate date){
        for(int i = 1; i < 7; i++)
            if(date.plusDays(i).equals(LocalDate.now()))
                return true;
        return false;
    }

    /**
     * If the calendar is showing the current date and matches up with {@link #generateManipulatorControls()}.
     * @param dayOfWeek String text for day of week. ISO-8601 standard says Monday is 1 but the program makes Sunday 0.
     * @return Same year, month, and day of week
     */
    private boolean sameYearMonthDayWeek(String dayOfWeek){
        LocalDate test;
        for(int i = 0; i < 42; i++){  // 42 days are shown
            test = firstDate.plusDays(i);
            if(test.equals(LocalDate.now()) && test.getDayOfWeek().toString().equalsIgnoreCase(dayOfWeek)){
                return true;
            }
        }
        return false;
    }

    /**
     * Capitalizes the given string
     * @param string Needs to be capitalize
     * @return Capitalized String of the given
     */
    private String capitalize(String string){
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1, string.length());
    }

    private class Measurements{
        // Measurements are based off the calendar
        final static double width = 910;
        final static double height = 600;
        final static double cellWidth = 130;
        final static double cellHeight = 100;

        final static double manipulatorWidth = cellWidth;
        final static double manipulatorHeight = 40;
        final static double manipulatorFontSize = 15;
        final static double manipulatorWeekWidthHeight = 40;
        final static double manipulatorBlank = width - (3 * manipulatorWidth) - (manipulatorWeekWidthHeight * 6);

        final static double daysOfWeekWidth = cellWidth;
        final static double daysOfWeekHeight = 20;

        final static double eventWidth = cellWidth;
        final static double eventHeight = 20;
        final static double eventTotalHeight = 80;
    }

    public enum Type{
        CALENDAR,
        MONTH,
        YEAR
    }

    public void setShowing(Type showing){
        this.showing = showing;
    }
    
}
