package org.example.reactorParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.reactors.Reactor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class ReactorParserJson implements ReactorParser {
    public ArrayList<Reactor> parse(String filePath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Reactor> result = new ArrayList<>();

        JsonNode rootNode = mapper.readTree(new File(filePath));

        for (JsonNode reactorNode : rootNode) {
            String name = reactorNode.fieldNames().next();
            JsonNode reactor = reactorNode.get(name);

            Reactor r = new Reactor();
            r.setName(name);
            r.setClassReactor(reactor.get("class").asText());
            r.setBurnup(reactor.get("burnup").asDouble());
            r.setKpd(reactor.get("kpd").asDouble());
            r.setEnrichment(reactor.get("enrichment").asDouble());
            r.setThermal_capacity(reactor.get("termal_capacity").asDouble());
            r.setElectrical_capacity(reactor.get("electrical_capacity").asDouble());
            r.setLife_time(reactor.get("life_time").asInt());
            r.setFirst_load(reactor.get("first_load").asDouble());
            r.setSource("json");

            result.add(r);
        }
        return result;
    }
}