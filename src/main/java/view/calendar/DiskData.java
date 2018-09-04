package view.calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class DiskData {
    final File fileLocation = new File("/Users/BabySloth/Desktop/exampleCalendar.xml");
    private Document document;

    public DiskData(){
        String userName = System.getProperty("user.name");
        

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

    public void calendarWrite(AllEvents data){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Root elements
            Document document = builder.newDocument();
            Element rootElement = document.createElement("calendarEvents");
            document.appendChild(rootElement);

            Event[] events = data.getAllEvents();
            for(Event event : events){
                // Child
                Element child = document.createElement("event");
                child.setAttribute("id", event.getId());
                rootElement.appendChild(child);

                // Subchild
                String[] dataPoints = event.getData();
                String[] dataNames = {"title", "description", "date", "time", "color"};
                for (int i = 0; i < 5; i++) {
                    Element subChild = document.createElement(dataNames[i]);
                    subChild.appendChild(document.createTextNode(dataPoints[i]));
                    child.appendChild(subChild);
                }
            }

            document.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(fileLocation);
            transformer.transform(source, result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
