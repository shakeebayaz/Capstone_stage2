package com.digital.ayaz.Utils;

import java.util.ArrayList;

public class PlaceListDetail {

    Double place_rating;
    private String place_name, place_id;
    private ArrayList<String> photo_endPointUrl;
    private String place_address, icon_url;

    public PlaceListDetail() {

    }

    public Double getPlace_rating() {
        return place_rating;
    }

    public void setPlace_rating(Double place_rating) {
        this.place_rating = place_rating;
    }

    public void setPhoto_url(ArrayList<String> photo_reference) {
        this.photo_endPointUrl = photo_reference;
    }

    public ArrayList<String> getPhoto_endPointUrl() {
        return photo_endPointUrl;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
