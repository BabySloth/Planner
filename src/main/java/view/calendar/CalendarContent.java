package view.calendar;

import helper.Colors;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import view.BasicView;

import java.time.DayOfWeek;
import java.time.LocalDate;

class CalendarContent extends VBox implements BasicView {
    private LocalDate firstDate;
    private LocalDate displayDate;  // One week ahead of first date
    final private CalendarView headController;

    CalendarContent(CalendarView headController){
        this.headController = headController;
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
        getChildren().clear();
        getChildren().addAll(generateManipulatorControls(),
                             generateDaysOfWeekDisplay(),
                             generateCalendarDisplay());
    }

    /**
     * Changes what is being shown
     * @return First view section (of three)
     */
    private HBox generateManipulatorControls(){
        double width = Measurements.manipulatorWidth;
        double height = Measurements.manipulatorHeight;
        double fontSize = Measurements.manipulatorFontSize;

        Button year = generateButton(String.valueOf(displayDate.getYear()), width, height, fontSize,
                                     sameYear(displayDate) ? Colors.YELLOW : Colors.LIGHT_GRAY);
        year.setOnAction(e -> System.out.println("Ran year"));
        Button month = generateButton(capitalize(displayDate.getMonth().toString()), width, height, fontSize,
                                      sameYearMonth(displayDate) ? Colors.YELLOW : Colors.LIGHT_GRAY);
        month.setOnAction(e -> System.out.println("Ran month"));
        Rectangle empty = generateBlank(Measurements.manipulatorBlank, 1);

        double weekWidthHeight = Measurements.manipulatorWeekWidthHeight;
        Button previousWeek = generateButton("<", weekWidthHeight, weekWidthHeight, fontSize, Colors.LIGHT_GRAY);
        previousWeek.setOnAction(e -> changeFirstDateByWeek(-1));
        Button nextWeek = generateButton(">", weekWidthHeight, weekWidthHeight, fontSize, Colors.LIGHT_GRAY);
        nextWeek.setOnAction(e -> changeFirstDateByWeek(1));

        return (HBox) generateContainer(new HBox(year, month, empty, previousWeek, nextWeek),
                                        Measurements.width, height);
    }

    /**
     * Shows what day of the week each column represents. Highlights in yellow if the calendar is showing the current
     * day and corresponds to the day of week display.
     * @return Second view section (of three)
     */
    private HBox generateDaysOfWeekDisplay(){
        HBox container = (HBox) generateContainer(new HBox(), Measurements.width, Measurements.daysOfWeekHeight);

        double fontSize = Measurements.daysOfWeekFontSize;
        double width = Measurements.daysOfWeekWidth;
        double height = Measurements.daysOfWeekHeight;
        final String[] daysOfWeekNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for(String name : daysOfWeekNames){
            Label label = generateLabel(name, sameYearMonthDayWeek(name) ? Colors.YELLOW : Colors.LIGHT_GRAY,
                                        fontSize, width, height);
            container.getChildren().add(label);
        }

        return container;
    }

    private VBox generateCalendarDisplay(){
        double width = Measurements.width;
        double height = Measurements.height;
        VBox container = (VBox) generateContainer(new VBox(), width, height);

        for(int i = 0; i < 6; i++){  // 6 rows
            LocalDate startingDate = firstDate.plusWeeks(i);
            container.getChildren().addAll(generateDayOfMonthDisplay(startingDate),
                                           generateDateEventDisplay(startingDate));
        }

        return container;
    }

    private HBox generateDayOfMonthDisplay(LocalDate startingDate){

    }

    private ScrollPane generateDateEventDisplay(LocalDate startingDate){

    }

    private Pane generateContainer(Pane container, double width, double height){
        container.setMinSize(width, height);
        container.setPrefSize(width, height);
        container.setMaxSize(width, height);

        return container;
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
     * A transparent rectangle to act as a space taker up.
     * @param width Width of the invisible rectangle.
     * @param height Height of the invisible rectangle.
     * @return A new space taker
     */
    private Rectangle generateBlank(double width, double height){
        return new Rectangle(width, height, Color.TRANSPARENT);
    }

    /**
     * Changes the first day of the month view by a week. Will call {@link #generateView()} to show the user the new
     * changes.
     * @param amount Negative or positive
     */
    private void changeFirstDateByWeek(int amount){
        firstDate = firstDate.plusWeeks(amount);
        displayDate = firstDate.plusWeeks(1);
        generateView();
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
        final static double manipulatorBlank = width - (2 * manipulatorWidth) - (manipulatorWeekWidthHeight * 2);

        final static double daysOfWeekWidth = cellWidth;
        final static double daysOfWeekHeight = 20;
        final static double daysOfWeekFontSize = 15;
    }
}
