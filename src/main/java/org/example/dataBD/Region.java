package org.example.dataBD;


import org.apache.commons.lang3.StringUtils;

public class Region {

    private int id;
    private String region;

    public Region(int id, String region) {
        this.id = id;
        this.region = StringUtils.trimToNull(region);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion_name() {
        return region;
    }

    public void setRegion_name(String region) {
        this.region = region;
    }
}
