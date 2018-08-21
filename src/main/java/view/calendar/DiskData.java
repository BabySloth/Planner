package view.calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class DiskData {
    private Document document;

    public DiskData(){
        final File fileLocation = new File("/Users/BabySloth/Desktop/exampleCalendar.xml");

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(fileLocation);
        }catch(Exception err){
            System.out.println("Error reading data in view.calendar.DiskData");
            err.printStackTrace();
        }

    }

    public void calendarRead(AllEvents data){
        NodeList nodes = document.getElementsByTagName("event");

        for(int index = 0; index < nodes.getLength(); index++) {
            String id, title, description, date, time, color, repeat, related;
            Element event = (Element) nodes.item(index);

            id = event.getAttribute("id");
            title = event.getElementsByTagName("title").item(0).getTextContent();
            description = event.getElementsByTagName("description").item(0).getTextContent();
            date = event.getElementsByTagName("date").item(0).getTextContent();
            time = event.getElementsByTagName("time").item(0).getTextContent();
            color = event.getElementsByTagName("color").item(0).getTextContent();

            data.addEvent(new Event(id, title, description, date, time, color));
        }
    }

    public void calendarWrite(){

    }

}
