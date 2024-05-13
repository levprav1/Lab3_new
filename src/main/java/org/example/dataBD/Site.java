package org.example.dataBD;

import org.apache.commons.lang3.StringUtils;

public class Site {
    private int id;
    private String name;
    private String location;
    private int country_id;

    public Site(int id, String name, String location, int country_id) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.country_id = country_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }
}
