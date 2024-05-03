package org.example.reactorParser;

import org.example.reactors.Reactor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public interface ReactorParser {
    public ArrayList<Reactor> parse(String filePath) throws IOException, ParserConfigurationException, SAXException;
}
