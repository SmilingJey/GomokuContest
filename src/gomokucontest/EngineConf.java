package gomokucontest;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class EngineConf {

    public String engineName = "";
    public String engineClass = "";
    public String engineDesc = "";
    public String engineAuthor = "";
    public String engineVersion = "";

    public EngineConf() {
    }

    public EngineConf(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(file);
            engineName = doc.getElementsByTagName("Name").item(0).getTextContent();
            engineClass = doc.getElementsByTagName("Class").item(0).getTextContent();
            engineDesc = doc.getElementsByTagName("Desc").item(0).getTextContent();
            engineAuthor = doc.getElementsByTagName("Author").item(0).getTextContent();
            engineVersion = doc.getElementsByTagName("Version").item(0).getTextContent();
            System.out.println("Load engine: " + engineName + ". Main class: " + engineClass);
        } catch (Exception ex) {
            System.out.println("Error engine load: " + file.getPath() + ", error: " + ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return engineName;
    }

    public IGomokuEngine createEngine() {
        IGomokuEngine o=null;
        try {
            o = (IGomokuEngine)Class.forName(engineClass).newInstance();
        } catch (Exception ex) {
            System.out.println("Error engine create: "+engineName+", error "+ex.getMessage());
        }
        return o;
    }
}
