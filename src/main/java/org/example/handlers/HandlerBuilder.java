package org.example.handlers;

import org.example.reactors.Reactor;

import java.util.ArrayList;

public class HandlerBuilder {
    private Handler xml;
    private Handler yaml;
    private Handler json;

    public ArrayList<Reactor> getData(String path){
        setParam();
        return json.readFile(path);
    }

    private void setParam() {
        this.xml = new HandlerXML();
        this.yaml = new HandlerYAML();
        this.json = new HandlerJSON();

        json.setNeighbour(yaml);
        yaml.setNeighbour(xml);

    }


}
