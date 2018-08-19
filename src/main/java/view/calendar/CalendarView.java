package view.calendar;

import helper.Colors;
import helper.Measurement;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import view.BasicView;

import java.time.DayOfWeek;
import java.time.LocalDate;


/**
 * Include view since Calendar is already a class in java.util.Calendar
 */
public class CalendarView extends HBox implements BasicView {
    private static CalendarView self = null;
    private LocalDate firstCalendarDate;

    private CalendarView(){
        setMainDesign();
        generateView();
    }

    public static CalendarView getInstance(){
        if(self == null){
            self = new CalendarView();
        }
        return self;
    }

    @Override
    public void generateView() {
        getChildren().clear();
        // View is divided into two section: left and right.

        // Left view
        // Contains the calendar and the timeline

        ScrollPane scrollPane = new ScrollPane();
        // Design
        scrollPane.getStyleClass().add("scrollPane");
        scrollPane.setPrefSize(Measurement.LEFT_WIDTH, helper.Measurement.SCREEN_HEIGHT);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Content
        scrollPane.setContent(new VBox(new CalendarShowCase(), new Timeline()));
        getChildren().add(scrollPane);


        // Right view
        // Contains the event details and date details (range of date, amount of days until)

    }

    @Override
    public void setMainDesign() {
        getStyleClass().add(getClass().getClassLoader().getResource("calendarView.css").toExternalForm());
        final double starting_xcor =  helper.Measurement.STARTING_XCOR;
        relocate(starting_xcor, 0);
        setPrefSize(helper.Measurement.SCREEN_WIDTH - starting_xcor, helper.Measurement.SCREEN_HEIGHT);
    }

    private class CalendarShowCase extends VBox implements BasicView{
        CalendarShowCase(){
            setCalendarVariables();
            setMainDesign();
            generateView();
        }

        private void setCalendarVariables(){
            if(firstCalendarDate == null){
                LocalDate now = LocalDate.now();
                firstCalendarDate = firstSunday(LocalDate.of(now.getYear(), now.getMonth(), 1));
            }
        }

        @Override
        public void setMainDesign() {
            setPrefWidth(CalendarView.Measurement.LEFT_WIDTH);
        }

        @Override
        public void generateView() {
            getChildren().addAll(generateTopSection(), generateBottomSection());
        }

        private HBox generateTopSection(){
            LocalDate displayDate = firstCalendarDate.plusWeeks(1);
            LocalDate now = LocalDate.now();
            boolean sameYear = displayDate.getYear() == now.getYear();
            boolean sameMonthYear = sameYear && displayDate.getMonthValue() == now.getMonthValue();
            double height = 40;
            double width = Measurement.cellWidth;
            double fontSize = 15;

            Button year = generateButton(String.valueOf(displayDate.getYear()), width, height, fontSize,
                                         sameYear ? Colors.YELLOW : Colors.LIGHT_GRAY);
            Button month = generateButton(capitalize(displayDate.getMonth().toString()), width, height, fontSize,
                                          sameMonthYear ? Colors.YELLOW : Colors.LIGHT_GRAY);
            Rectangle empty = generateBlank(570, 1);
            Button previousWeek = generateButton("<", 40, 40, fontSize, Colors.LIGHT_GRAY);
            Button nextWeek = generateButton(">", 40, 40, fontSize, Colors.LIGHT_GRAY);

            return new HBox(year, month, empty, previousWeek, nextWeek);
        }

        private GridPane generateBottomSection(){
            GridPane content = generateGridPane(6, 7, Measurement.cellWidth, Measurement.cellHeight);

            return content;
        }

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

        private Button generateButton(String text, double width, double height, double fontSize, Color color){
            Button button = new Button(text);
            button.setPrefSize(width, height);
            button.setFont(Font.font(fontSize));
            button.setTextFill(color);
            button.getStyleClass().add("buttonCalendar");

            return button;
        }

        private LocalDate firstSunday(LocalDate date){
            while(date.getDayOfWeek() != DayOfWeek.SUNDAY)
                date = date.minusDays(1);
            return date;
        }

        private class Measurement{
            final static double totalWidth = CalendarView.Measurement.LEFT_WIDTH;
            final static double cellWidth = 130;
            final static double cellHeight = 100;
        }
    }

    private class Timeline extends VBox{

    }

    private class EventDetail{

    }

    private class DatesInformation{

    }

    private String capitalize(String string){
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1, string.length());
    }

    private Rectangle generateBlank(double width, double height){
        return new Rectangle(width, height, Color.TRANSPARENT);
    }

    private class Measurement{
        final static double LEFT_WIDTH = 910;
        final static double RIGHT_WIDTH = 370;
    }
}
