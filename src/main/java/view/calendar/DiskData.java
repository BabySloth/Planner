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
    private File fileLocation;

    public DiskData(){
        String home = System.getProperty("user.home");
        fileLocation = new File(home + "/Desktop/calendarData.xml");
        if(!fileLocation.exists()) {
            createXMLFIle("calendarEvents");
        }
    }

    /**
     * Creates a new document if the xml file doesn't exist or returns a document of the existing xml file.
     * @param fileExists  If the user ran the program for the first time / deleted needed files, return a blank document
     * @return New document if file doesn't exit
     */
    private Document createDocument(boolean fileExists){
        Document document = null;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = fileExists ? builder.parse(fileLocation) : builder.newDocument();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            return document;
        }
    }

    public void calendarRead(AllEvents data){
        Document document = createDocument(true);
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
            Document document = createDocument(true);
            // Root elements
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

    /**
     * Creates the xml file if it doesn't exists
     * @param parentNodeName name of the root node
     */
    private void createXMLFIle(String parentNodeName){
        try{
            Document document = createDocument(false);
            Element root = document.createElement(parentNodeName);
            document.appendChild(root);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(fileLocation);
            transformer.transform(domSource, streamResult);
        }catch(Exception e){
            System.out.println("doesn't work");
            e.printStackTrace();
        }
    }

}
