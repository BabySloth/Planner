package view.calendar;

import javafx.scene.paint.Color;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Event {
    private final String id; // Event id; just for writing to disk purposes
    private String title;  // What appears on the calendar
    private String description;  //Extra information when clicked on
    private LocalDate dateStart;
    private LocalDate dateEnd = null;
    private LocalTime timeStart;  //Time uses 24h clock
    private LocalTime timeEnd = null;
    private String color;  // Hexadecimal

    public Event(String id, String title, String description, String date, String time, String color){
        this.id = id;
        this.title = title;
        this.description = description;
        processDate(date);
        processTime(time);
        this.color = color;
    }

    // Helper functions for initialization
    private void processDate(String date){
        int[] information = stringToIntArray(date);  // Always length 4
        dateStart = LocalDate.ofYearDay(information[0], information[1]);
        dateEnd = LocalDate.ofYearDay(information[2], information[3]);
    }

    private void processTime(String time){
        int[] information = stringToIntArray(time);  // Always length 4
        if(information == null){
            timeStart = null;
            timeEnd = null;
        }else{
            timeStart = LocalTime.of(information[0], information[1]);
            timeEnd = LocalTime.of(information[2], information[3]);
        }
    }

    private int[] stringToIntArray(String string){
        if(string.equals("")) return null;
        String[] stringArray = string.split("\\.");
        int length = stringArray.length;
        if(length == 0) return null;
        int[] intArray = new int[length];
        for(int i = 0; i < length; i++){
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }

    public int getDaysLength() {
        return (int) dateStart.until(dateEnd, ChronoUnit.DAYS) + 1;  // Add 1 because there are no 0 day events
    }

    /**
     * Gets amount of days until Saturday
     * @return
     */
    public int spanDays(){
        int length = getDaysLength();
        int counter = 0;
        LocalDate tempDate = dateStart;
        do{
            tempDate = tempDate.plusDays(1);
            counter++;
            length--;
        }while(tempDate.getDayOfWeek() != DayOfWeek.SATURDAY && length != 0);

        return counter;
    }

    int getHourLength() {
        return (int) timeStart.until(timeEnd, ChronoUnit.HOURS);
    }

    public boolean occurs(LocalDate check){
        return check.isAfter(dateStart.minusDays(1)) && check.isBefore(dateEnd.plusDays(1));
    }

    public boolean occurs(LocalTime check){
        // Seconds to be safe even though smallest accuracy is minute
        return check.isAfter(timeStart.minusSeconds(1)) && check.isBefore(timeEnd.plusSeconds(1));
    }

    boolean occurs(LocalDateTime check){
        if(timeStart == null) return false;
        LocalDateTime start = LocalDateTime.of(dateStart, timeStart).minusSeconds(1);
        LocalDateTime end = LocalDateTime.of(dateEnd, timeEnd).plusSeconds(1);
        return check.isAfter(start) && check.isBefore(end);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }
    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }
    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }
    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Color getColor() {
        return Color.web(color, 1);
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String[] getData(){
        String[] data = new String[5];
        data[0] = title;
        data[1] = description;
        data[2] = String.format("%d.%d.%d.%d", dateStart.getYear(), dateStart.getDayOfYear(),
                                dateEnd.getYear(), dateEnd.getDayOfYear());
        data[3] = dataGetTime();
        data[4] = color;
        return data;
    }
    private String dataGetTime(){
        if(timeStart == null){
            return "";
        }
        return String.format("%d.%d.%d.%d", timeStart.getHour(), timeStart.getMinute(),
                             timeEnd.getHour(), timeEnd.getMinute());
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Event){
            Event test = (Event) obj;
            return test.getId().equals(this.id);
        }
        return false;
    }
}
