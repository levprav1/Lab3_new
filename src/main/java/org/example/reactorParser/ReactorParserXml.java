package org.example.reactorParser;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.example.reactors.Reactor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

public class ReactorParserXml implements ReactorParser {
    @Override
    public ArrayList<Reactor> parse(String filePath) throws IOException, ParserConfigurationException, SAXException {
        ArrayList<Reactor> result = new ArrayList<>();
        File file = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        NodeList nodeList = doc.getElementsByTagName("reactors");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String name = element.getFirstChild().getNextSibling().getNodeName();
                String classReactor = element.getElementsByTagName("class").item(0).getTextContent();
                double burnup = Double.parseDouble(element.getElementsByTagName("burnup").item(0).getTextContent());
                double kpd = Double.parseDouble(element.getElementsByTagName("kpd").item(0).getTextContent());
                double enrichment = Double.parseDouble(element.getElementsByTagName("enrichment").item(0).getTextContent());
                double thermal_capacity = Double.parseDouble(element.getElementsByTagName("termal_capacity").item(0).getTextContent());
                double electrical_capacity = Double.parseDouble(element.getElementsByTagName("electrical_capacity").item(0).getTextContent());
                int life_time = Integer.parseInt(element.getElementsByTagName("life_time").item(0).getTextContent());
                double first_load = Double.parseDouble(element.getElementsByTagName("first_load").item(0).getTextContent());

                Reactor reactor = new Reactor(name, classReactor, burnup, kpd, enrichment, thermal_capacity, electrical_capacity, life_time, first_load, "xml");
                result.add(reactor);
            }
        }
        return result;
    }
}
