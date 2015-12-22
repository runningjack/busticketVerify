package com.busticket.amedora.busticketverify.model;

/**
 * Created by Amedora on 12/3/2015.
 */
public class Terminal {
    public int id;
    public int terminal_id;
    public String short_name;
    public int route_id;
    public String name;
    public String description;
    public String geodata;
    public String distance;
    public double one_way_to_fare;
    public double one_way_from_fare;

    public Terminal(){

    }

    public void setId(int id){this.id = id;}

    public void setTerminal_id(int terminal_id) {
        this.terminal_id = terminal_id;
    }

    public void setShort_name(String short_name){this.short_name = short_name;}
    public void setRoute_id(int route_id){this.route_id = route_id;}
    public void setName(String name){this.name=name;}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeodata(String geodata) {
        this.geodata = geodata;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setOne_way_from_fare(double one_way_from_fare) {
        this.one_way_from_fare = one_way_from_fare;
    }

    public void setOne_way_to_fare(double one_way_to_fare) {
        this.one_way_to_fare = one_way_to_fare;
    }

    public int getId() {
        return id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public int getTerminal_id() {
        return terminal_id;
    }

    public String getDescription() {
        return description;
    }

    public String getDistance() {
        return distance;
    }

    public String getGeodata() {
        return geodata;
    }

    public String getName() {
        return name;
    }

    public String getShort_name() {
        return short_name;
    }

    public double getOne_way_from_fare() {
        return one_way_from_fare;
    }

    public double getOne_way_to_fare() {
        return one_way_to_fare;
    }
}
