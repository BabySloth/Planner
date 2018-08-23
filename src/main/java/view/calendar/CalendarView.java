package view.calendar;

import helper.Colors;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import view.BasicView;
import view.MainHolder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * Include view since Calendar is already a class in java.util.Calendar
 */
public class CalendarView extends HBox implements BasicView{
    private LocalDate firstCalendarDate;
    private final MainHolder parent;
    private boolean containsWarning = false;
    private AllEvents allEvents;

    // Children content
    ScrollPane scrollPane = new ScrollPane();

    public CalendarView(MainHolder parent, AllEvents allEvents){
        this.parent = parent;
        this.allEvents = allEvents;
        setMainDesign();
        generateView();
    }

    @Override
    public void generateView() {
        getChildren().clear();
        // View is divided into two section: left and right.

        // Left view
        // Contains the calendar and the timeline
        // Design
        scrollPane.getStyleClass().add("scrollPane");
        scrollPane.setPrefSize(Measurement.LEFT_WIDTH, helper.Measurement.SCREEN_HEIGHT);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Content
        updateLeftSection();
        getChildren().add(scrollPane);


        // Right view
        // Contains the event details and date details (range of date, amount of days until)

    }

    @Override
    public void setMainDesign() {
        getStylesheets().add(getClass().getClassLoader().getResource("calendarView.css").toExternalForm());
        final double starting_xcor =  helper.Measurement.STARTING_XCOR;
        relocate(starting_xcor, 0);
        setPrefSize(helper.Measurement.SCREEN_WIDTH - starting_xcor, helper.Measurement.SCREEN_HEIGHT);
    }

    /**
     * Updates both the timeline and the calendar display. It does not necessarily mean that there will be a change
     * since the changes in view are based off of their respective starting variable. For example, to update the
     * calendar, you must first update the firstCalendarDate then this method. Missing one or both results in no
     * change
     */
    private void updateLeftSection(){
        scrollPane.setContent(new VBox(new CalendarContent(this)));
    }

    /**
     * This creates the view on the top left portion. It creates the calendar and ways all the other sections.
     */
    private class CalendarShowCase extends VBox implements BasicView{
        private LocalDate displayDate;
        private CalendarBox[] calendarBoxes = new CalendarBox[42]; // 6 weeks, 7 days a week

        CalendarShowCase(){
            setCalendarVariables();
            setMainDesign();
            generateView();
        }

        /**
         * Initializes variables if they haven't been done so, especially the firstCalendarDate when CalendarView is
         * first created
         */
        private void setCalendarVariables(){
            if(firstCalendarDate == null){
                LocalDate now = LocalDate.now();
                firstCalendarDate = firstSunday(LocalDate.of(now.getYear(), now.getMonth(), 1));
            }
             displayDate = firstCalendarDate.plusWeeks(1);
        }

        @Override
        public void setMainDesign() {
            setPrefWidth(CalendarView.Measurement.LEFT_WIDTH);
        }

        @Override
        public void generateView() {
            getChildren().addAll(generateTopSection(), generateBlank(0, 5), generateBottomSection());
        }

        /**
         * This provides the controls to manipulate what is being shown on the calendar.
         * @return Controls for the calendar
         */
        private HBox generateTopSection(){
            double height = 40;
            double width = Measurement.cellWidth;
            double fontSize = 15;

            Button year = generateButton(String.valueOf(displayDate.getYear()), width, height, fontSize,
                                         sameYear(displayDate) ? Colors.YELLOW : Colors.LIGHT_GRAY);
            Button month = generateButton(capitalize(displayDate.getMonth().toString()), width, height, fontSize,
                                          sameYearMonth(displayDate) ? Colors.YELLOW : Colors.LIGHT_GRAY);
            Rectangle empty = generateBlank(570, 1);
            Button previousWeek = generateButton("<", 40, 40, fontSize, Colors.LIGHT_GRAY);
            previousWeek.setOnMouseClicked(e -> changeFirstCalendarDateByWeek(-1));
            Button nextWeek = generateButton(">", 40, 40, fontSize, Colors.LIGHT_GRAY);
            nextWeek.setOnMouseClicked(e -> changeFirstCalendarDateByWeek(1));

            return new HBox(year, month, empty, previousWeek, nextWeek);
        }

        /**
         * Changes the first day of the month view by a week. Will call {@link #updateLeftSection()} to show the user
         * the new changes.
         * @param amount Negative or positive
         */
        private void changeFirstCalendarDateByWeek(int amount){
            firstCalendarDate = firstCalendarDate.plusWeeks(amount);
            updateLeftSection();
        }

        /**
         * Creates the actual calendar to see the events and what day(s) they occur on.
         * @return Calendar view
         */
        private GridPane generateBottomSection(){
            GridPane content = generateGridPane(6, 7, Measurement.cellWidth, Measurement.cellHeight);

            // Days of week
            content.getRowConstraints().add(0, new RowConstraints(20));
            final String[] DAY_OF_WEEK_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for(int i = 0; i < 7; i++){
                String name = DAY_OF_WEEK_NAMES[i];
                Label label = generateLabel(name, sameYearMonthDayWeek(displayDate, name) ? Colors.YELLOW : Colors.LIGHT_GRAY,
                                            15, 130, 20);
                content.add(label, i, 0);
            }

            // Events
            int position = 0;
            for(int row = 1; row < 7; row++){
                for(int column = 0; column < 7; column++){
                    LocalDate boxDate = firstCalendarDate.plusDays(position);
                    calendarBoxes[position] = new CalendarBox(position, boxDate, allEvents.getEventForDay(boxDate));
                    content.add(calendarBoxes[position], column, row);
                    position++;
                }
            }

            return content;
        }

        /**
         * Creating the GridPane.
         * See {@link #generateBottomSection()}
         * see {@link } //TODO: generate the changing year and changing month
         * see {@link }
         * @param rows Amount of rows
         * @param columns Amount of columns
         * @param width Width of one column
         * @param height Height of one row
         * @return GridPane with new RowConstraints and ColumnConstraints
         */
        private GridPane generateGridPane(int rows, int columns, double width, double height){
            GridPane pane = new GridPane();
            for(int row = 0; row < rows; row++){
                pane.getRowConstraints().add(new RowConstraints(height));
            }
            for(int column = 0; column < columns; column++){
                pane.getColumnConstraints().add(new ColumnConstraints(width));
            }
            return pane;
        }

        /**
         * Creates a button with design.
         * @param text Text displayed on the button (Added space to prevent cramping)
         * @param width Width of the button
         * @param height Height of the button
         * @param fontSize Use a double value for {@link Font}
         * @param color Using {@link Colors}
         * @return A new button
         */
        private Button generateButton(String text, double width, double height, double fontSize, Color color){
            Button button = new Button(" " + text);
            button.setPrefSize(width, height);
            button.setFont(Font.font(fontSize));
            button.setTextFill(color);
            button.getStyleClass().add("buttonCalendar");

            return button;
        }

        /**
         * Generates a label with design.
         * @param text Text on the label
         * @param color Color on the label using {@link Colors}
         * @param fontSize Double value for font using {@link Font}
         * @param width Width of label
         * @param height Height of label
         * @return A new button
         */
        private Label generateLabel(String text, Color color, double fontSize, double width, double height){
            Label label = new Label(" " + text); // Leading space to prevent jumbled text
            label.setTextFill(color);
            label.setFont(Font.font(fontSize));
            label.setMaxSize(width, height);
            label.setMinSize(width, height);
            return label;
        }

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
         *If today's date and given date and day of week are the same (see {@link #sameYear(LocalDate)} and {@link #sameYearMonth(LocalDate)}
         * @param date Comparison date
         * @param dayOfWeek String text for day of week. ISO-8601 standard says Monday is 1 but the program makes Sunday 0.
         * @return Same year, month, and day of week
         */
        private boolean sameYearMonthDayWeek(LocalDate date, String dayOfWeek){
            LocalDate now = LocalDate.now();
            return sameYearMonth(date) && now.getDayOfWeek().toString().equalsIgnoreCase(dayOfWeek);
        }

        private class CalendarBox extends VBox implements BasicView{
            private final int position;
            private final LocalDate date;
            private final ArrayList<Event> events;
            private final TreeMap<Integer, Event> orders = new TreeMap<>();

            CalendarBox(int position, LocalDate date, ArrayList<Event> events){
                this.position = position;
                this.date = date;
                this.events = events;
                this.setAlignment(Pos.TOP_RIGHT);

                setMainDesign();
                generateView();
            }

            @Override
            public void setMainDesign() {
                setPrefSize(CalendarShowCase.Measurement.cellWidth, CalendarShowCase.Measurement.cellHeight);
            }

            @Override
            public void generateView() {
                getChildren().clear();

                // Label text
                getChildren().add(generateDateDisplay());

                // Events populate
                positionEvents();
                displayEvents();
            }

            private void positionEvents(){
                int order = 0;
                ArrayList<Event> starter = new ArrayList<>();
                for(Event event : events){
                    // First view has the highest of all calendarBoxes priority of positioning
                    if(position == 0){
                        orders.put(order, event);
                        // Add event to view depending on order
                        order++;
                    }else{
                        TreeMap<Integer, Event> previousOrder = calendarBoxes[position - 1].getOrders();

                        if(previousOrder.containsValue(event)){
                            for(Integer previousPosition : previousOrder.keySet()){
                                if(previousOrder.get(previousPosition).equals(event)){
                                    orders.put(previousPosition, event);
                                }
                            }
                        }else{
                            starter.add(event);
                        }
                    }

                    // Optimize to show as many events as possible
                    // We want to shove in as many long events as possible
                }
            }

            private void displayEvents(){
                /*// EventDisplay information
                boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;
                boolean needsTitle = isSunday || event.getDaysLength() == 1 || event.getDateStart().equals(date);
                boolean isContinuation = !needsTitle || (isSunday && event.occurs(date.minusDays(1)));

                EventDisplay display = new EventDisplay(event, event.spanDays(), isContinuation, needsTitle);*/
            }

            private StackPane moreEvents(int amountMore){
                Rectangle rectangle = new Rectangle(130, 20, Colors.LIGHT_BLUE);
                Label label = new Label(String.format("%d more", amountMore));

                StackPane stackPane = new StackPane(rectangle, label);
                stackPane.setMinSize(130, 20);
                stackPane.setPrefSize(130, 20);
                stackPane.setMaxSize(130, 20);
                return stackPane;
            }

            private Label generateDateDisplay(){
                StringBuilder text = new StringBuilder();
                int day = date.getDayOfMonth();
                text.insert(0, day).append(
                        position == 0 || day == 1 ? String.format(" - %s", capitalize(date.getMonth().toString())) : "");
                return generateLabel(text.toString(), date.equals(LocalDate.now()) ? Colors.YELLOW : Colors.LIGHT_GRAY,
                                     15, 130, 20);
            }

            /**
             * Ensures that TreeMap is immutable when another object accesses this object even if they are of the same
             * type
             * @return Copy of orders
             */
            public TreeMap<Integer, Event> getOrders(){
                return (TreeMap<Integer, Event>) orders.clone();
            }
        }

        private class Measurement{
            final static double totalWidth = CalendarView.Measurement.LEFT_WIDTH;
            final static double cellWidth = 130;
            final static double cellHeight = 108;
        }
    }

    private class EventDisplay extends StackPane{
        final private Event event;
        private double spanAmount;
        private boolean isContinuation;
        private boolean needsTitle;
        private double width = 130;
        final private double height = 20;

        EventDisplay(Event event, int spanAmount, boolean isContinuation, boolean needsTitle) {
            this.event = event;
            this.spanAmount = spanAmount * width - 10;
            this.isContinuation = isContinuation;
            this.needsTitle = needsTitle;

            // Sizing
            width = isContinuation ? 130 : 120;
            setMinSize(width, height);
            setPrefSize(width, height);
            setMaxSize(width, height);

            generateBackground();
            if(needsTitle)
                generateText(); // Depends on position
        }

        private void generateBackground(){
            Rectangle rectangle = new Rectangle(width, height, event.getColor());
            this.getChildren().add(rectangle);
        }

        private void generateText(){
            Label label = new Label(event.getTitle());
            label.setMinSize(width, height);
            label.setPrefSize(width, height);
            label.setMaxSize(width, height);
            label.setTextFill(Colors.DARK_GRAY);
            label.setAlignment(Pos.CENTER);
            this.getChildren().add(label);
        }
    }

    /**
     * Capitalizes the given string
     * @param string Needs to be capitalize
     * @return Capitalized String of the given
     */
    private String capitalize(String string){
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1, string.length());
    }

    /**
     * A transparent rectangle to act as a space taker up.
     * @param width Width of the invisible rectangle.
     * @param height Height of the invisible rectangle.
     * @return A new space taker
     */
    private Rectangle generateBlank(double width, double height){
        return new Rectangle(width, height, Color.TRANSPARENT);
    }

    private class Measurement{
        final static double LEFT_WIDTH = 915;
        final static double RIGHT_WIDTH = 365;
    }
}
