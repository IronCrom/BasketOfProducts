import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Config {
    protected enum Format {json, text}

    protected enum config {load, save, log}

    private boolean enabled;
    private String fileName;
    private Format format;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Config() {

    }

    public Config config(File file, config set) throws ParserConfigurationException, IOException, SAXException {
        Config config = new Config();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builderXml = factory.newDocumentBuilder();
        Document document = builderXml.parse(file);
        NodeList settings = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < settings.getLength(); i++) {

            if (settings.item(i).getNodeType() == Node.ELEMENT_NODE && settings.item(i).getNodeName().equals(set.toString())) {
                NodeList configParameter = settings.item(i).getChildNodes();
                for (int j = 0; j < configParameter.getLength(); j++) {
                    if (configParameter.item(j).getNodeType() == Node.ELEMENT_NODE &&
                            configParameter.item(j).getNodeName().equals("enabled")) {
                        config.setEnabled(Boolean.parseBoolean(configParameter.item(j).getTextContent()));
                    }
                    if (configParameter.item(j).getNodeType() == Node.ELEMENT_NODE &&
                            configParameter.item(j).getNodeName().equals("fileName")) {
                        config.setFileName(configParameter.item(j).getTextContent());
                    }
                    if (configParameter.item(j).getNodeType() == Node.ELEMENT_NODE &&
                            configParameter.item(j).getNodeName().equals("format")) {
                        config.setFormat(Format.valueOf(configParameter.item(j).getTextContent()));
                    }
                }
            }
        }
        return config;
    }
}