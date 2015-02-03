package gomokucontest;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class EngineConf {

    private String engineName = "";
    private String engineClass = "";
    private String engineDesc = "";
    private String engineAuthor = "";
    private String engineVersion = "";

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
        } catch (Exception e) {
            System.out.println("Error load engine: " + file.getPath() + ", error: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return engineName;
    }

    public IGomokuEngine createEngine() {
        IGomokuEngine engine = null;
        try {
            engine = (IGomokuEngine)Class.forName(engineClass).newInstance();
            engine.init(GomokuContest.getInstance().gobanPanel);
        } catch (Exception e) {
            System.out.println("Error create engine: "+engineName);
        }
        return engine;
    }
}
