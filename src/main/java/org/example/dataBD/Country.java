package org.example.dataBD;

import org.apache.commons.lang3.StringUtils;

public class Country {
    private int id;
    private String country;
    private int region_id;

    public Country(int id, String country, int region_id) {
        this.id = id;
        this.country = StringUtils.trimToNull(country);
        this.region_id = region_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
