package org.example.convertFiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.*;

public class ConvertJson2types {

    public ConvertJson2types() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/Reactors_json.json");

        OutputStream outputStream = new FileOutputStream("./Reactors_json.json");

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    public void convert2xml() throws IOException {
        // Создание объекта ObjectMapper для чтения JSON
        ObjectMapper jsonMapper = new ObjectMapper();

        // Чтение JSON из файла
        JsonNode rootNode = jsonMapper.readTree(new File("./Reactors_json.json"));
        // Создание объекта ObjectMapper для записи XML
        XmlMapper xmlMapper = new XmlMapper();

        // Установка настроек для генерации XML
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        // Преобразование JSON в XML
        ObjectNode xmlNode = xmlMapper.createObjectNode();
        xmlNode.set("reactors", rootNode);
        xmlMapper.writeValue(new File("./Reactors_xml.xml"), xmlNode);
    }

    public void convert2yaml() throws IOException {
        // Создание объекта ObjectMapper для чтения JSON
        ObjectMapper jsonMapper = new ObjectMapper();

        // Чтение JSON из файла
        JsonNode rootNode = jsonMapper.readTree(new File("./Reactors_json.json"));

        // Создание объекта YAMLMapper для записи YAML
        YAMLMapper yamlMapper = new YAMLMapper();

        // Преобразование JSON в YAML
        yamlMapper.writeValue(new File("./Reactors_yaml.yaml"), rootNode);

    }
}
