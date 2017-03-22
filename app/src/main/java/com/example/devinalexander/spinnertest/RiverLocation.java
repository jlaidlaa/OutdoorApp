package com.example.devinalexander.spinnertest;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class RiverLocation implements Serializable, Comparable<RiverLocation>{
    private URL url;
    private String name;
    private String fork;
    private String location;
    private String pictureCode;
    private String siteId;



    public RiverLocation(String name, String location, URL url, String code){
        this.name = name;
        this.location = location;
        this.url = url;
        this.pictureCode = code;
        this.siteId = "";
    }

    public RiverLocation(String name, String fork, String location, URL url, String code){
        this.name = name;
        this.fork = fork;
        this.location = location;
        this.url = url;
        this.pictureCode = code;
        this.siteId = "";
    }
    public RiverLocation(String siteId, String name, String state){
        this.url = null;
        this.fork = "";
        this.pictureCode = "";
        this.siteId = siteId;
        this.name = name;
        this.location = state;
    }



    public URL getUrl() {
        return url;
    }
    public void setUrl(URL url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFork() {
        return fork;
    }
    public void setFork(String fork) {
        this.fork = fork;
    }
    public String getPictureCode() {
        return pictureCode;
    }
    public void setPictureCode(String pictureCode) {
        this.pictureCode = pictureCode;
    }
    public String getSiteId() {return siteId;}
    public void setSiteId() {this.siteId = siteId;}

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    public String toString(){
        if (!(fork == null)){
            return name + " - " + fork + " (" + location + ")";
        }
        else{
            return name + " (" + location + ")";
        }
    }

    public String getImageURLString(){
        return "http://water.weather.gov/resources/hydrographs/" + "rmdv2_hg" + ".png";
                //"http://water.weather.gov/resources/hydrographs/" + pictureCode + ".png";
    }

    public int compareTo(RiverLocation river){
        if (this.name.compareTo(river.getName()) < 0){
            return -1;
        }
        else if (this.name.compareTo(river.getName()) > 0){
            return 1;
        }
        else {
            if (this.getLocation().compareTo(river.getLocation()) < 0){
                return -1;
            }
            else if (this.getLocation().compareTo(river.getLocation()) > 0){
                return 1;
            }
            else {
                return 0;
            }
        }

    }

}
